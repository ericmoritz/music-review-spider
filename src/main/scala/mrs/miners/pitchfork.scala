package mrs.miners.pitchfork

import mrs.models._
import java.net.{URI, URL}
import java.util.Date
import org.jsoup.Jsoup
import com.netaporter.uri.dsl._
import com.netaporter.uri.Uri.parse
import com.github.nscala_time.time.Imports._
import org.joda.time.{DateTime, DateTimeZone}
import org.joda.time.format.DateTimeFormat
import org.json4s._


object pitchforkReviewsMiner {
  def apply(reviews: String): List[Review] {
    val json = parse(reviews)
    for {
      JObject(result) <- json \ "results"
      JObject(rating) <- result \ "tombstone" \ "rating"
      JObject(author) <- result \ "authors"
      JObject(artist) <- result \ "artists"

      JString("url", JString(reviewURL)) <- result
      JField("title", JString(albumTitle)) <- result
      JField("timestamp", JLong(timestamp)) <- result
      JField("display_name", JString(artistName)) <- artists

      JField("name", JString(authorName)) <- author
      JField("url", JString(authorURL)) <- author

      JField("rating", JString(rating)) <- rating

    } yeild (
      Review(
        new URI("http://pitchfork.com" + reviewURL),
        parseDate(timestamp),
        Album(
          albumTitle,
          artistName
        ),
        Reviewer(
          new URI("http://pitchfork.com" + authorURL),
          authorName
        ),
        Rating(
          new URI("http://pitchfork.com" + reviewURL + "#rating"),
          Double.parseDouble(rating),
          10.0
        )
      )
    )
  }
}

object parseDate {
  def apply(ts: Long): DayOfYear = {
    val dateTime = DateTime(ts)
    DayOfYear(
      dateTime.year.get,
      dateTime.month.get,
      dateTime.dayOfMonth.get
    )
  }
}
