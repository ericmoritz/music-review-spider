import org.specs2.mutable._

import java.net.URI

import mrs.models._
import org.json4s._
import org.json4s.JsonDSL._
import org.json4s.native.JsonMethods._
import org.joda.time.{DateTime, DateTimeZone}

object ReviewSpec extends Specification {
  "review" should {
    "serialize as JSON-LD" in {
      pretty(render(Review(
        new URI("http://pitchfork.com/reviews/albums/10088-remixes-compiled/"),
        DayOfYear(2015, 2, 12),
        Album(
          "Remixes Compiled",
          "Telefon Tel Aviv"
        ),
        Reviewer(
          new URI("http://pitchfork.com/staff/marc-hogan/"),
          "Marc Hogan"
        ),
        Rating(
          new URI("http://pitchfork.com/reviews/albums/10088-remixes-compiled/#rating"),
          5.0, 10.0
        )
      ).toJSON)) mustEqual pretty(render(
        (
          ("@context" -> ("@vocab" -> "tag:ericmoritz@gmail.com,2015:vocabs/mrs#")) ~
          ("@id" -> "http://pitchfork.com/reviews/albums/10088-remixes-compiled/") ~
          ("pubDate" -> "2015-02-12") ~
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
           ("@id" -> "http://pitchfork.com/reviews/albums/10088-remixes-compiled/#rating") ~
           ("score" -> 5.0) ~
           ("maxScore" -> 10.0) ~
           ("normalizedScore" -> 50.0) 
         )
        )
      ))
    }
  }
}
