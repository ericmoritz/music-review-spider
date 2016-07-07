package mrs.models

import java.net.{URI, URLEncoder}

import org.json4s.native.JsonMethods._
import org.json4s._
import mrs.const.RDFConst
import org.openrdf.model.impl.ValueFactoryImpl
import org.openrdf.model.{URI => RURI, BNode, Resource, Value, Statement}


case class DayOfYear(year: Integer, month: Integer, day: Integer) {
  override def toString(): String = f"${this.year}-${this.month}%02d-${day}%02d"
}


class RDFObject {
  val factory = ValueFactoryImpl.getInstance()
  def ref(uri: URI): RURI = factory.createURI(uri.toString)
  def ref(p: String): RURI = factory.createURI(s"${RDFConst.Namespace}${p}")
  def stmt(s: Resource, p: RURI, o: Value) = factory.createStatement(s, p, o)
  def date(x: DayOfYear) = factory.createLiteral(
    x.toString,
    factory.createURI("http://www.w3.org/2001/XMLSchema#date")
  )
}

case class Review(
  uri: URI,
  pubDate: DayOfYear,
  album: Album,
  reviewer: Reviewer,
  rating: Rating
) extends RDFObject {

  def toRDF(): List[Statement] = {
    val s = ref(this.uri)

    List(
      stmt(s, ref("album"), ref(this.album.uri)),
      stmt(s, ref("pubDate"), date(this.pubDate)),
      stmt(s, ref("reviewer"), ref(this.reviewer.uri)),
      stmt(s, ref("rating"), ref(this.rating.uri))
    ) ::: this.album.toRDF ::: this.reviewer.toRDF ::: this.rating.toRDF
  }

  def toJSON(): JObject = {
    import org.json4s.JsonDSL._
    import org.json4s.DefaultFormats
    import org.json4s.FieldSerializer._

    val rdfObjectSerializer = FieldSerializer[RDFObject](
      {
        case ("uri", x: URI) => Some("@id", x.toString)
        case (key, x: URI) => Some(key, x.toString)
        case (key, x: DayOfYear) => Some(key, x.toString)
        case ("factory", _) => None
      }
    )

    implicit val formats = DefaultFormats + rdfObjectSerializer

    val ns = RDFConst.Namespace

    ("@context" -> ("@vocab" -> ns)) ~ Extraction.decompose(this).asInstanceOf[JObject]
  }
}

case class Rating (
  uri: URI,
  score: Double,
  maxScore: Double,
  normalizedScore: Double
) extends RDFObject {

  def toRDF(): List[Statement] = {
    val s = ref(this.uri)

    List(
      stmt(s, ref("score"), factory.createLiteral(this.score)),
      stmt(s, ref("maxScore"), factory.createLiteral(this.maxScore)),
      stmt(s, ref("normalizedScore"), factory.createLiteral(this.normalizedScore))
    )
  }

}

object Rating {
  def apply(uri: URI, score: Double, maxScore: Double) = new Rating(
    uri,
    score,
    maxScore,
    score / maxScore * 100.0
  )
}



case class Album (
  uri: URI,
  title: String,
  artist: String
) extends RDFObject {

  def toRDF(): List[Statement] = {
    val s = ref(this.uri)
    List(
      stmt(s, ref("title"), factory.createLiteral(this.title)),
      stmt(s, ref("artist"), factory.createLiteral(this.artist))
    )
  }
}

object Album {
  private def encode(str: String): String = URLEncoder.encode(str, "UTF-8")

  def apply(title: String, artist: String) = new Album(
      new URI(s"tag:ericmoritz@gmail.com,2015:mrs/albums/${encode(artist)}-${encode(title)}"),
      title,
      artist
  )
}

case class Reviewer(uri: URI, name: String) extends RDFObject {

  def toRDF(): List[Statement] = {
    val s = ref(this.uri)

    List(
      stmt(s, ref("name"), factory.createLiteral(this.name))
    )
  }
}
