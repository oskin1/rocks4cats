package io.github.oskin1.rocksdb

import cats.effect.{Concurrent, Resource, Sync}

trait TxRocksDB[F[_]] extends RocksDB[F] {

  /** Create new Transaction instance as a resource.
    */
  def beginTransaction: Resource[F, Transaction[F]]
}

object TxRocksDB {

  def make[I[_]: Sync, F[_]: Concurrent](path: String, createIfMissing: Boolean = true): Resource[I, TxRocksDB[F]] =
    internals.TxRocksDBJNI.make[I, F](path, createIfMissing)
}
