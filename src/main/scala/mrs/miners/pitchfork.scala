package mrs.miners.pitchfork

import mrs.models._
import java.net.{URI, URL}
import org.jsoup.Jsoup
import com.netaporter.uri.dsl._
import com.netaporter.uri.Uri.parse

object pitchforkPageUriMiner {
  def apply(baseUrl: URL, htmlSrc: String): List[URL] = {
    import collection.JavaConversions._

    val doc = Jsoup.parse(htmlSrc)
    doc.select(
      """.page-numbers a"""
    ).iterator.map { 
      el => baseUrl.toURI.resolve(new URI(el.attr("href"))).toURL
    }.to[List]
  }
}


object pitchforkReviewUriMiner {
  def apply(baseUrl: URL, htmlSrc: String) : List[URL] = {
    import collection.JavaConversions._

    val doc = Jsoup.parse(htmlSrc)
    doc.select(
      """#main > .object-grid  a"""
    ).iterator.map { el =>
      baseUrl.toURI.resolve(new URI(el.attr("href"))).toURL
    }.to[List]
  }
}


object pitchforkReviewMiner {
  def apply(htmlSrc: String): Option[Review] = {
    val doc = Jsoup.parse(htmlSrc)
    for {
      uri <- Option(
        doc.select(
          """meta[property="og:url"]"""
        ).first
      ).map { x => new URI(x.attr("content")) }

      albumTitle <- Option(
        doc.select(
          """.review-meta .info h2"""
        ).first
      ).map { _.text }

      albumArtist <- Option(
        doc.select(
          """.review-meta .info h1 a"""
        ).first
      ).map { _.text }

      reviewerNode <-  Option(
        doc.select(
          """.review-meta .info h4"""
        ).first
      )

      reviewerUri <- Option(
        reviewerNode.select(
          """a"""
        ).first
      ).map { x => uri.resolve(new URI(x.attr("href"))) }

      reviewerName <- Option(
        reviewerNode.select(
          """address"""
        ).first
      ).map { _.text }

      rating <- Option(
        doc.select(
          """.score"""
        ).first
      ).map { _.text.toDouble }

    } yield {
      Review(
        uri,
        Album(
          albumTitle,
          albumArtist
        ),
        Reviewer(
          reviewerUri,
          reviewerName
        ),
        Rating(rating, 10)
      )
    }
  }
}
