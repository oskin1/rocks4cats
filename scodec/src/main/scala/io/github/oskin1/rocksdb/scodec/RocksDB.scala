package io.github.oskin1.rocksdb.scodec

import cats.data.OptionT
import cats.effect.{Resource, Sync}
import cats.syntax.flatMap._
import cats.syntax.functor._
import cats.{MonadThrow, Show}
import io.github.oskin1.rocksdb.scodec.utils._
import io.github.oskin1.rocksdb.{internals, RocksDB => RawRocksDB}
import scodec.Codec

trait RocksDB[F[_]] {

  def get[K: Codec: Show, V: Codec](key: K): F[Option[V]]

  def delete[K: Codec: Show](key: K): F[Unit]

  def put[K: Codec: Show, V: Codec: Show](key: K, value: V): F[Unit]
}

object RocksDB {

  def make[I[_]: Sync, F[_]: Sync](path: String, createIfMissing: Boolean = true): Resource[I, RocksDB[F]] =
    internals.RocksDBJNI.make[I, F](path, createIfMissing).map(new Live(_))

  private[rocksdb] class Live[F[_]: MonadThrow](db: RawRocksDB[F]) extends RocksDB[F] {

    def get[K: Codec: Show, V: Codec](key: K): F[Option[V]] =
      (for {
        k     <- OptionT.liftF(encode(key))
        raw   <- OptionT(db.get(k))
        value <- OptionT.liftF(decode[F, V](raw))
      } yield value).value

    def delete[K: Codec: Show](key: K): F[Unit] =
      for {
        k <- encode(key)
        _ <- db.delete(k)
      } yield ()

    def put[K: Codec: Show, V: Codec: Show](key: K, value: V): F[Unit] =
      for {
        k <- encode(key)
        v <- encode(value)
        _ <- db.put(k, v)
      } yield ()
  }
}
