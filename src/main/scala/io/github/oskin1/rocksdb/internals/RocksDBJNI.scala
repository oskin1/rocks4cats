package io.github.oskin1.rocksdb.internals

import cats.effect.{Resource, Sync}
import io.github.oskin1.rocksdb.RocksDB
import org.{rocksdb => jrocks}

class RocksDBJNI[F[_]](db: jrocks.RocksDB)(implicit F: Sync[F]) extends RocksDB[F] {

  def get(key: Array[Byte]): F[Option[Array[Byte]]] = F.delay(Option(db.get(key)))

  def delete(key: Array[Byte]): F[Unit] = F.delay(db.delete(key))

  def put(key: Array[Byte], value: Array[Byte]): F[Unit] = F.delay(db.put(key, value))
}

object RocksDBJNI {

  def make[I[_], F[_]: Sync](path: String)(implicit I: Sync[I]): Resource[I, RocksDBJNI[F]] =
    Resource.make(I.delay(jrocks.RocksDB.open(path)))(db => I.delay(db.closeE())).map(db => new RocksDBJNI[F](db))
}
