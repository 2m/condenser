package lt.dvim.condenser.vdf

import scodec.{Attempt, Codec, DecodeResult, Decoder, Encoder, Err, SizeBound}
import scodec.bits.BitVector
import scodec.codecs.KnownDiscriminatorType

object UnknownDiscriminatorBoundList {
  def udblist[A](codec: Codec[A]): Codec[List[A]] = new UnknownDiscriminatorBoundListCodec(codec)

  final class UnknownDiscriminatorBoundListCodec[A](codec: Codec[A], limit: Option[Int] = None) extends Codec[List[A]] {

    def sizeBound = limit match {
      case None      => SizeBound.unknown
      case Some(lim) => codec.sizeBound * lim.toLong
    }

    def encode(list: List[A]) = Encoder.encodeSeq(codec)(list)

    def decode(buffer: BitVector) = decodeCollectUntilUnknownDiscriminator[List, A](codec, limit)(buffer)

    override def toString = s"list($codec)"
  }

  /**
    * Repeatedly decodes values of type `A` from the specified vector and returns a collection of the specified type.
    * Terminates when no more bits are available in the vector or when `limit` is defined and that many records have been
    * decoded or when UnknownDiscriminator error is encountered. Exits upon first decoding error.
    * @group conv
    */
  final def decodeCollectUntilUnknownDiscriminator[F[_], A](dec: Decoder[A], limit: Option[Int])(buffer: BitVector)(
      implicit cbf: collection.generic.CanBuildFrom[F[A], A, F[A]]): Attempt[DecodeResult[F[A]]] = {
    val bldr = cbf()
    var remaining = buffer
    var count = 0
    var maxCount = limit getOrElse Int.MaxValue
    var error: Option[Err] = None
    var continue = true
    while (count < maxCount && remaining.nonEmpty && continue) {
      dec.decode(remaining) match {
        case Attempt.Successful(DecodeResult(value, rest)) =>
          bldr += value
          count += 1
          remaining = rest
        case Attempt.Failure(err: KnownDiscriminatorType[_]#UnknownDiscriminator) =>
          continue = false
        case Attempt.Failure(err) =>
          error = Some(err.pushContext(count.toString))
          remaining = BitVector.empty
      }
    }
    Attempt.fromErrOption(error, DecodeResult(bldr.result, remaining))
  }
}
