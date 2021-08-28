package io.github.oskin1.rocksdb.scodec

import cats.syntax.applicative._
import cats.syntax.applicativeError._
import cats.syntax.either._
import cats.syntax.functor._
import cats.syntax.show._
import cats.{MonadThrow, Show}
import io.github.oskin1.rocksdb.scodec.errors.{BinaryDecodingFailed, BinaryEncodingFailed}
import scodec.Codec
import scodec.bits.{BitVector, ByteVector}

private[rocksdb] object utils {

  @inline def encode[F[_]: MonadThrow, A: Codec: Show](v: A): F[Array[Byte]] =
    Codec[A]
      .encode(v)
      .toEither
      .leftMap(err => BinaryEncodingFailed(v.show, err.messageWithContext))
      .fold(_.raiseError, _.pure)
      .map(_.toByteArray)

  @inline def decode[F[_]: MonadThrow, A: Codec](raw: Array[Byte]): F[A] =
    Codec[A]
      .decode(BitVector(raw))
      .toEither
      .map(_.value)
      .leftMap(err => BinaryDecodingFailed(ByteVector(raw).toBase16, err.messageWithContext))
      .fold(_.raiseError, _.pure)
}
