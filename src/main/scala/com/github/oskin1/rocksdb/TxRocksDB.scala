package com.github.oskin1.rocksdb

import cats.effect.{Concurrent, Resource, Sync}
import com.github.oskin1.rocksdb.internals.Transaction

trait TxRocksDB[F[_]] extends RocksDB[F] {

  /** Create new Transaction instance as a resource.
    */
  def beginTransaction: Resource[F, Transaction[F]]
}

object TxRocksDB {

  def make[I[_]: Sync, F[_]: Concurrent](path: String): Resource[I, TxRocksDB[F]] =
    internals.TxRocksDBJNI.make[I, F](path)
}
