import org.specs2.mutable._

import java.net.URI
import mrs.models._
import org.json4s._
import org.json4s.JsonDSL._
import org.json4s.native.JsonMethods._

object ReviewSpec extends Specification {
  "review" should {
    "serialize as JSON-LD" in {
      pretty(render(Review(
        new URI("http://pitchfork.com/reviews/albums/10088-remixes-compiled/"),
        Album(
          "Remixes Compiled",
          "Telefon Tel Aviv"
        ),
        Reviewer(
          new URI("http://pitchfork.com/staff/marc-hogan/"),
          "Marc Hogan"
        ),
        Rating(6.6, 10)
      ).toJSON)) mustEqual pretty(render(
        (
          ("@context" -> ("@vocab" -> "tag:ericmoritz@gmail.com,2015:vocabs/mrs#")) ~
          ("@id" -> "http://pitchfork.com/reviews/albums/10088-remixes-compiled/") ~
          ("album" -> 
              ("@id" -> "tag:ericmoritz@gmail.com,2015:mrs/albums/Telefon+Tel+Aviv-Remixes+Compiled") ~
              ("title" -> "Remixes Compiled") ~
              ("artist" -> "Telefon Tel Aviv")
          ) ~
          ("reviewer" -> 
            ("@id" -> "http://pitchfork.com/staff/marc-hogan/") ~
            ("name" -> "Marc Hogan")
          ) ~
         ("rating" ->
           ("score" -> 6.6) ~
           ("maxScore" -> 10.0)
         )
        )
      ))
    }
  }
}
