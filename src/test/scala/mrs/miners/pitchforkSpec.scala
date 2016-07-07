import org.specs2.mutable._

import mrs.models._
import java.net.{URI, URL}
import mrs.miners.pitchfork.pitchforkReviewsMiner
import scala.io.Source
import org.joda.time.{DateTime, DateTimeZone}


object PitchforkReviewsMinerSpec extends Specification {
  jsonSrc = Source.fromURL(getClass.getResource("/pitchfork/testData.json")).mkString
  "miner" should {
    "parse pitchfork reviews JSON" in {
      pitchforkReviewsMiner(htmlSrc) mustEqual List(
        Review(
          new URI("http://pitchfork.com/reviews/albums/22072-aphex-twin-cheetah"),
          DayOfYear(2016, 7, 7),
          Album(
            "Aphex Twin",
            "Cheetah"
          ),
          Reviewer(
            new URI("http://pitchfork.com/staff/philip-sherburne"),
            "Philip Sherburne"
          ),
          Rating(
            new URI("http://pitchfork.com/reviews/albums/10088-remixes-compiled/#rating"),
            8.2, 10.0
          )
        )
      )
    }
  }
}
