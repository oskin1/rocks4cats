package io.github.oskin1.rocksdb.scodec

object errors {

  final case class BinaryEncodingFailed(showValue: String, reason: String)
    extends Exception(s"Failed to binary encode value {$showValue}. $reason")

  final case class BinaryDecodingFailed(showValue: String, reason: String)
    extends Exception(s"Failed to decode value {$showValue}. $reason")
}
