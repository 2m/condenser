package lt.dvim.condenser.vdf

import scodec.Codec
import scodec.bits._
import scodec.codecs._

object VdfCodec {

  sealed trait Node
  case class KeyMap(key: String, value: List[Node]) extends Node
  case class KeyString(key: String, value: String) extends Node
  object Empty extends Node

  val keyString = (
    ("string key"      | cstring) ~
    ("string contents" | cstring)
  ).xmap[KeyString](KeyString, {
    case KeyString(key, string) => (key, string)
  })

  lazy val keyMap = (
    ("map key"      | cstring) ~
    ("map contents" | list(lazily(codec)))
  ).xmap[KeyMap](KeyMap, {
    case KeyMap(key, value) => (key, value)
  })

  lazy val codec: Codec[Node] = discriminated[Node]
    .by(uint8)
    .subcaseP(0) { case k: KeyMap => k }(keyMap)
    .subcaseP(1) { case k: KeyString => k }(keyString)
    .caseP(8) { case k: Empty.type => () }(_ => Empty)(constant(hex"08"))

}
