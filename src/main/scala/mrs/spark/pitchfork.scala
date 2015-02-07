package mrs.spark.pitchfork
import org.apache.spark.rdd.RDD
import org.apache.spark.SparkContext
import mrs.miners.pitchfork._
import mrs.download.download
import mrs.models.Review
import java.net.URL

// Ew nasty IO stuff
object pitchforkReviewRDD {
  def pageUrlRdd(sc: SparkContext): RDD[URL] = {
    var seedUrl = new URL("http://pitchfork.com/reviews/albums/1/")
    val seedHtml = download(seedUrl)
    sc.parallelize(
        pitchforkPageUriMiner(seedUrl, seedHtml)
    )
  }

  def reviewUrlRdd(pageUrlRdd: RDD[URL]): RDD[URL] = pageUrlRdd.flatMap { 
    pageUrl => {
      val x = pitchforkReviewUriMiner(pageUrl, download(pageUrl))
      println((pageUrl, x))
      x
    }
  }

  def reviewHtmlRdd(reviewUrlRdd: RDD[URL]): RDD[(URL, String)] = reviewUrlRdd.map { 
    reviewUrl => (reviewUrl, download(reviewUrl))
  }

  def apply(sc: SparkContext): RDD[Review] = reviewHtmlRdd(
    reviewUrlRdd(
      pageUrlRdd(sc)
    )
  ).flatMap {
    x => pitchforkReviewMiner(x._2).toList
  }
}
