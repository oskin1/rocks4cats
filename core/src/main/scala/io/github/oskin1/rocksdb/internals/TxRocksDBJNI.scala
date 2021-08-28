package io.github.oskin1.rocksdb.internals

import cats.effect.{Concurrent, Resource, Sync}
import io.github.oskin1.rocksdb.{Transaction, TxRocksDB}
import org.rocksdb.TransactionDB
import org.{rocksdb => jrocks}

final private[rocksdb] class TxRocksDBJNI[F[_]: Concurrent](db: jrocks.TransactionDB)
  extends RocksDBJNI[F](db)
  with TxRocksDB[F] {

  def beginTransaction: Resource[F, Transaction[F]] =
    Transaction.begin[F, F](db, new jrocks.WriteOptions())
}

object TxRocksDBJNI {

  def make[I[_], F[_]: Concurrent](path: String, createIfMissing: Boolean)(implicit
    I: Sync[I]
  ): Resource[I, TxRocksDBJNI[F]] =
    for {
      opts <- Resource.eval(I.delay(new jrocks.Options().setCreateIfMissing(createIfMissing)))
      openI = open[I](path, opts, new jrocks.TransactionDBOptions())
      db <- Resource.make(openI)(db => I.delay(db.closeE())).map(db => new TxRocksDBJNI[F](db))
    } yield db

  private def open[F[_]](
    path: String,
    opts: jrocks.Options,
    txOpts: jrocks.TransactionDBOptions
  )(implicit F: Sync[F]): F[TransactionDB] = F.delay(jrocks.TransactionDB.open(opts, txOpts, path))
}
