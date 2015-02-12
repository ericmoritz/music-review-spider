import org.specs2.mutable._

import mrs.models._
import java.net.{URI, URL}
import mrs.miners.pitchfork._
import scala.io.Source
import org.joda.time.{DateTime, DateTimeZone}



object PitchforkPageUriMiner extends Specification {
  val htmlSrc = Source.fromURL(getClass.getResource("/pitchfork/index.html")).mkString
  "pitchforkPageUriMiner" should {
    "extract the review index page URLs from the index page" in {
      pitchforkPageUriMiner(new URL("http://pitchfork.com/reviews/albums/1/"), htmlSrc).sortBy(_.toString) mustEqual List(
        new URL("http://pitchfork.com/reviews/albums/1/"),
        new URL("http://pitchfork.com/reviews/albums/2/"),
        new URL("http://pitchfork.com/reviews/albums/3/"),
        new URL("http://pitchfork.com/reviews/albums/4/"),
        new URL("http://pitchfork.com/reviews/albums/5/"),
        new URL("http://pitchfork.com/reviews/albums/6/"),
        new URL("http://pitchfork.com/reviews/albums/7/"),
        new URL("http://pitchfork.com/reviews/albums/8/")
      ).sortBy(_.toString)
    }
  }
}


object PitchforkReviewUriMiner extends Specification {
  val htmlSrc = Source.fromURL(getClass.getResource("/pitchfork/index.html")).mkString
  "pitchforkReviewPageMiner" should {
    "extract the review page URLs from the index page" in {
      pitchforkReviewUriMiner(new URL("http://pitchfork.com/reviews/albums/1/"), htmlSrc).sortBy(_.toString) mustEqual List(
        new URL("http://pitchfork.com/reviews/albums/20245-sleeping-tapes/"), 
        new URL("http://pitchfork.com/reviews/albums/20142-a-flourish-and-a-spoil/"), 
        new URL("http://pitchfork.com/reviews/albums/20221-mount-eerie-sauna/"), 
        new URL("http://pitchfork.com/reviews/albums/20091-john-carpenter-lost-themes/"), 
        new URL("http://pitchfork.com/reviews/albums/20146-joey-bada-b4da/"), 
        new URL("http://pitchfork.com/reviews/albums/20190-shadows-in-the-night/"), 
        new URL("http://pitchfork.com/reviews/albums/20168-boogalou-reed/"), 
        new URL("http://pitchfork.com/reviews/albums/20025-matana-roberts-coin-coin-chapter-three-river-run-thee/"), 
        new URL("http://pitchfork.com/reviews/albums/20193-teaspoon-to-the-ocean/"), 
        new URL("http://pitchfork.com/reviews/albums/20194-big-dark-love/"), 
        new URL("http://pitchfork.com/reviews/albums/20106-depersonalisation/"), 
        new URL("http://pitchfork.com/reviews/albums/20192-home/"), 
        new URL("http://pitchfork.com/reviews/albums/20144-flyaway-garden/"), 
        new URL("http://pitchfork.com/reviews/albums/20101-revisionist/"), 
        new URL("http://pitchfork.com/reviews/albums/20205-dance-mania-ghetto-madness/"), 
        new URL("http://pitchfork.com/reviews/albums/20191-fantastic-planet/"), 
        new URL("http://pitchfork.com/reviews/albums/20187-fight-for-yourself/"), 
        new URL("http://pitchfork.com/reviews/albums/20145-africa-express-africa-express-presents-terry-rileys-in-c-mali/"), 
        new URL("http://pitchfork.com/reviews/albums/20134-signs-under-test/"), 
        new URL("http://pitchfork.com/reviews/albums/20094-scar-sighted/")
      ).sortBy(_.toString)
    }
  }

}


object PitchforkReviewMinerSpec extends Specification {
  val htmlSrc = Source.fromURL(getClass.getResource("/pitchfork/testData.html")).mkString

  "miner" should {
    "parse dates correctly" in {
      pitchforkReviewMiner.parseDate("May 15, 2007") mustEqual DayOfYear(2007, 5, 15)
    }
    "parse pitchfork review HTML" in {
      pitchforkReviewMiner(htmlSrc) mustEqual Some(Review(
        new URI("http://pitchfork.com/reviews/albums/10088-remixes-compiled/"),
        DayOfYear(2007, 5, 15),
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
          6.6, 10.0
        )
      ))
    }
  }
}
