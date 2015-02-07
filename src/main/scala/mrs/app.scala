package mrs.app

import org.apache.spark._
import mrs.spark.pitchfork.pitchforkReviewRDD
import org.json4s.native.JsonMethods._

object MRSApp extends App {
  if (args.length < 1) {
    System.err.println(
      "Usage: MRSApp <out path>")
    System.exit(1)
  }
  val outPath = args(0)
  val sparkConf = new SparkConf().setAppName("MRSApp")
  val sc = new SparkContext(sparkConf)
  val reviews = pitchforkReviewRDD(sc)
  reviews.map { review =>
    compact(render(review.toJSON))
  }.saveAsTextFile(s"$outPath/reviews")
}
