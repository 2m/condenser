package lt.dvim.condenser.vdf

import java.nio.file.{Files, Paths}

import utest._
import scodec.bits._

object VdfCodecSpec extends TestSuite {

  val tests = Tests {
    "codec" - {
      "amccallie-shortcuts" - cycle()
      "dave-shortcuts" - cycle()
      "empty-shortcuts" - cycle()
      "iconchange-shortcuts" - cycle()
      "issue-37-mishugashu-1-shortcuts" - cycle()
      "issue-37-mishugashu-2-shortcuts" - cycle()
      "issue-37-shortcuts" - cycle()
      "issue-37-spitfire-shortcuts" - cycle()
      "issue-37-syd-shortcuts" - cycle()
      "multitag-shortcuts" - cycle()
      "onlyilol-shortcuts" - cycle()
      "recursion_max" - cycle()
      "tag-shortcuts" - cycle()
      "windows-shortcuts" - cycle()
      "wtfis0x0d-shortcuts" - cycle()
    }
  }

  def cycle()(implicit path: framework.TestPath) = {
    val vdf = path.value.last
    val original = vdf.vdf.bits
    val decoded = VdfCodec.codec.decode(original).require.value
    val encoded = VdfCodec.codec.encode(decoded).require
    encoded ==> original
  }

  implicit class StringOps(name: String) {
    def vdf =
      ByteVector(Files.readAllBytes(Paths.get(getClass.getResource(s"/vdf/$name.vdf").toURI)))
  }
}
