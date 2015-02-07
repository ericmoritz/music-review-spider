package mrs.models

import java.net.{URI, URLEncoder}
import org.json4s._

class RDFObject

case class Review(
  uri: URI,
  album: Album,
  reviewer: Reviewer,
  rating: Rating
) extends RDFObject {
  def toJSON(): JObject = {
    import org.json4s.JsonDSL._
    import org.json4s.DefaultFormats
    import org.json4s.FieldSerializer._

    val rdfObjectSerializer = FieldSerializer[RDFObject](
      {
        case ("uri", x: URI) => Some("@id", x.toString)
        case (key, x: URI) => Some(key, x.toString)
      }
    )

    implicit val formats = DefaultFormats + rdfObjectSerializer

    val ns = "tag:ericmoritz@gmail.com,2015:vocabs/mrs#"

    ("@context" -> ("@vocab" -> ns)) ~ Extraction.decompose(this).asInstanceOf[JObject]
  }
}

case class Rating ( 
  score: Double,
  maxScore: Double
) extends RDFObject


case class Album (
  uri: URI, 
  title: String,
  artist: String
) extends RDFObject 

object Album {
  private def encode(str: String): String = URLEncoder.encode(str, "UTF-8")

  def apply(title: String, artist: String) = new Album(
      new URI(s"tag:ericmoritz@gmail.com,2015:mrs/albums/${encode(artist)}-${encode(title)}"),
      title, 
      artist
  )
}

case class Reviewer(uri: URI, name: String) extends RDFObject

