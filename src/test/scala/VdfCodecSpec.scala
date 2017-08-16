package lt.dvim.condenser.vdf

import java.nio.file.{Files, Paths}

import VdfCodec._
import scodec.bits._
import org.scalatest.{Matchers, WordSpec}

class VdfCodecSpec extends WordSpec with Matchers {
  import VdfCodecSpec._

  "vdf codec" should {
    Seq(
      "amccallie-shortcuts",
      "dave-shortcuts",
      "empty-shortcuts",
      "iconchange-shortcuts",
      "issue-37-mishugashu-1-shortcuts",
      "issue-37-mishugashu-2-shortcuts",
      "issue-37-shortcuts",
      "issue-37-spitfire-shortcuts",
      "issue-37-syd-shortcuts",
      "multitag-shortcuts",
      "onlyilol-shortcuts",
      "recursion_max",
      "tag-shortcuts",
      "windows-shortcuts",
      "wtfis0x0d-shortcuts"
    ).foreach { file =>
      s"cycle $file" in cycle(file)
    }
  }

  def cycle(vdf: String) = {
    val original = vdf.vdf.bits
    val decoded = VdfCodec.codec.decode(original).require.value
    //println(decoded)
    val encoded = VdfCodec.codec.encode(decoded).require
    //println(encoded.bytes)
    //println(new String(encoded.bytes.toArray))
    //println(original.bytes)
    encoded shouldBe original
  }

}

object VdfCodecSpec {
  implicit class StringOps(name: String) {
    def vdf =
      ByteVector(Files.readAllBytes(Paths.get(getClass.getResource(s"/vdf/$name.vdf").toURI)))
  }
}
