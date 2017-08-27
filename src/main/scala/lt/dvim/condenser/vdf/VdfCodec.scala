package lt.dvim.condenser.vdf

import scodec.bits._
import scodec.codecs._
import scodec.Codec

import UnknownDiscriminatorBoundList._

object VdfCodec {

  sealed trait Vdf
  case class KeyVdf(key: String, value: List[Vdf]) extends Vdf
  case class KeyString(key: String, value: String) extends Vdf

  val keyString = {
    ("keystring key"      | cstring) ::
    ("keystring contents" | cstring)
  }.as[KeyString]

  val keyVdf = {
    ("keyvdf key"        | cstring) ::
    ("keyvdf contents"   | udblist(lazily(contents))) ::
    ("keyvdf terminator" | constant(hex"08"))
  }.dropUnits.as[KeyVdf]

  val contents: Codec[Vdf] = discriminated[Vdf]
    .by(uint8)
    .subcaseP(0) { case k: KeyVdf => k }(keyVdf)
    .subcaseP(1) { case k: KeyString => k }(keyString)

  val codec = contents <~ constant(hex"08")
}
