package io.github.oskin1.rocksdb.internals

import cats.effect.concurrent.Semaphore
import cats.effect.{Concurrent, Resource, Sync}
import org.{rocksdb => jrocks}

trait Transaction[F[_]] {

  /** Retrieve a value associated with the provided key.
    */
  def get(key: Array[Byte]): F[Option[Array[Byte]]]

  /** Write a key using this transaction.
    */
  def put(key: Array[Byte], value: Array[Byte]): F[Unit]

  /** Delete a key using this transaction.
    */
  def delete(key: Array[Byte]): F[Unit]

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

  def begin[I[_], F[_]: Concurrent](db: jrocks.TransactionDB, opts: jrocks.WriteOptions)(implicit
    I: Sync[I]
  ): Resource[I, Transaction[F]] =
    for {
      semaphore <- Resource.eval(Semaphore.in[I, F](1))
      tx        <- Resource.make(I.delay(db.beginTransaction(opts)))(tx => I.delay(tx.close()))
    } yield new Live[F](semaphore, tx)

  final class Live[F[_]](semaphore: Semaphore[F], tx: jrocks.Transaction)(implicit F: Sync[F]) extends Transaction[F] {
    def get(key: Array[Byte]): F[Option[Array[Byte]]] = delayPermitted(Option(tx.get(new jrocks.ReadOptions(), key)))

    def put(key: Array[Byte], value: Array[Byte]): F[Unit] = delayPermitted(tx.put(key, value))

    def delete(key: Array[Byte]): F[Unit] = delayPermitted(tx.delete(key))

    def commit: F[Unit] = delayPermitted(tx.commit())

    def close: F[Unit] = delayPermitted(tx.close())

    def rollback: F[Unit] = delayPermitted(tx.rollback())

    def delayPermitted[A](thunk: => A): F[A] = semaphore.withPermit(F.delay(thunk))
  }
}
