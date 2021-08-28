package io.github.oskin1.rocksdb

import cats.effect.{Resource, Sync}

trait RocksDB[F[_]] {

  def get(key: Array[Byte]): F[Option[Array[Byte]]]

  def delete(key: Array[Byte]): F[Unit]

  def put(key: Array[Byte], value: Array[Byte]): F[Unit]
}

object RocksDB {

  def make[I[_]: Sync, F[_]: Sync](path: String, createIfMissing: Boolean = true): Resource[I, RocksDB[F]] =
    internals.RocksDBJNI.make[I, F](path, createIfMissing)
}
