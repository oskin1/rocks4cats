package io.github.oskin1.rocksdb.internals

import cats.effect.{Concurrent, Resource, Sync}
import io.github.oskin1.rocksdb.TxRocksDB
import org.rocksdb.TransactionDB
import org.{rocksdb => jrocks}

final class TxRocksDBJNI[F[_]: Concurrent](db: jrocks.TransactionDB) extends RocksDBJNI[F](db) with TxRocksDB[F] {

  def beginTransaction: Resource[F, Transaction[F]] =
    Transaction.begin[F, F](db, new jrocks.WriteOptions())
}

object TxRocksDBJNI {

  def make[I[_], F[_]: Concurrent](path: String)(implicit I: Sync[I]): Resource[I, TxRocksDBJNI[F]] = {
    val openI = open[I](path, new jrocks.Options(), new jrocks.TransactionDBOptions())
    Resource.make(openI)(db => I.delay(db.closeE())).map(db => new TxRocksDBJNI[F](db))
  }

  private def open[F[_]](
    path: String,
    opts: jrocks.Options,
    txOpts: jrocks.TransactionDBOptions
  )(implicit F: Sync[F]): F[TransactionDB] = F.delay(jrocks.TransactionDB.open(opts, txOpts, path))
}
