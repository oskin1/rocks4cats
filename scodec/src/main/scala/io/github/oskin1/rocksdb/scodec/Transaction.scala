package io.github.oskin1.rocksdb.scodec

import cats.data.OptionT
import cats.effect.{Concurrent, Resource, Sync}
import cats.syntax.flatMap._
import cats.syntax.functor._
import cats.{MonadThrow, Show}
import io.github.oskin1.rocksdb.scodec.utils._
import io.github.oskin1.rocksdb.{Transaction => RawTransaction}
import org.{rocksdb => jrocks}
import scodec.Codec

trait Transaction[F[_]] {

  /** Retrieve a value associated with the provided key.
    */
  def get[K: Codec: Show, V: Codec](key: K): F[Option[V]]

  /** Write a key using this transaction.
    */
  def put[K: Codec: Show, V: Codec: Show](key: K, value: V): F[Unit]

  /** Delete a key using this transaction.
    */
  def delete[K: Codec: Show](key: K): F[Unit]

  /** Commit all the changes using this transaction.
    */
  def commit: F[Unit]

  /** Close the transaction.
    */
  def close: F[Unit]

  /** Rollback all the changes made through this transaction.
    */
  def rollback: F[Unit]
}

object Transaction {

  def apply[F[_]: MonadThrow](tx: RawTransaction[F]): Transaction[F] = new Live(tx)

  def begin[I[_], F[_]: Concurrent](db: jrocks.TransactionDB, opts: jrocks.WriteOptions)(implicit
    I: Sync[I]
  ): Resource[I, Transaction[F]] =
    RawTransaction.begin[I, F](db, opts).map(new Live(_))

  final private[rocksdb] class Live[F[_]: MonadThrow](tx: RawTransaction[F]) extends Transaction[F] {

    def get[K: Codec: Show, V: Codec](key: K): F[Option[V]] =
      (for {
        k     <- OptionT.liftF(encode(key))
        raw   <- OptionT(tx.get(k))
        value <- OptionT.liftF(decode[F, V](raw))
      } yield value).value

    def put[K: Codec: Show, V: Codec: Show](key: K, value: V): F[Unit] =
      for {
        k <- encode(key)
        v <- encode(value)
        _ <- tx.put(k, v)
      } yield ()

    def delete[K: Codec: Show](key: K): F[Unit] =
      for {
        k <- encode(key)
        _ <- tx.delete(k)
      } yield ()

    def commit: F[Unit] = tx.commit

    def close: F[Unit] = tx.close

    def rollback: F[Unit] = tx.rollback
  }
}
