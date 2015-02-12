package mrs.app

import org.apache.spark._
import mrs.spark.pitchfork.pitchforkReviewRDD
import org.json4s.native.JsonMethods._
import org.openrdf.repository.sparql.SPARQLRepository
import org.openrdf.model.Statement
import mrs.const.RDFConst
import collection.JavaConversions._


case class Config(
  outPath: Option[String],
  queryURL: Option[String],
  updateURL: Option[String]
)


object MRSApp extends App {
  args.foreach { println }

  val config = 
    if (args.length == 1) {
      Config(Some(args(0)), None, None)
    } else if (args.length == 2) {
      Config(None, Some(args(0)), Some(args(1)))
    } else {
      System.err.println(
        "Usage: MRSApp <out path> | <sparql query URL> <sparql update URL>"
      )
      System.exit(1)
    }

  val sparkConf = new SparkConf().setAppName("MRSApp")
  val sc = new SparkContext(sparkConf)
  val reviews = pitchforkReviewRDD(sc)

  config match {

    case Config(Some(outPath), _, _) =>
      reviews.map { review =>
        compact(render(review.toJSON))
      }.saveAsTextFile(s"$outPath/reviews")

    case Config(_, Some(queryURL), Some(updateURL)) =>

      reviews.foreach { review =>
        val repo = new SPARQLRepository(queryURL, updateURL)
        repo.initialize
        val conn = repo.getConnection
        // I can't seem to get the conn.add(Iterable<Statement>) call to work
        // This is probably really noisy.
        review.toRDF.foreach { statement =>
          conn.add(statement)
        }
      }
  }

}
