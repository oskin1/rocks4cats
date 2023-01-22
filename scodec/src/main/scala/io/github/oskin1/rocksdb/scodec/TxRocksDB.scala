package io.github.oskin1.rocksdb.scodec

import cats.MonadThrow
import cats.effect.kernel.Async
import cats.effect.{Resource, Sync}
import io.github.oskin1.rocksdb.{internals, TxRocksDB => RawTxRocksDB}

trait TxRocksDB[F[_]] extends RocksDB[F] {

  /** Create new Transaction instance as a resource.
    */
  def beginTransaction: Resource[F, Transaction[F]]
}

object TxRocksDB {

  def make[I[_]: Sync, F[_]: Async](path: String, createIfMissing: Boolean = true): Resource[I, TxRocksDB[F]] =
    internals.TxRocksDBJNI.make[I, F](path, createIfMissing).map(new Live(_))

  final private[rocksdb] class Live[F[_]: MonadThrow](db: RawTxRocksDB[F])
    extends RocksDB.Live[F](db)
    with TxRocksDB[F] {
    def beginTransaction: Resource[F, Transaction[F]] = db.beginTransaction.map(Transaction(_))
  }
}
