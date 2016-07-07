package mrs.spark.pitchfork
import org.apache.spark.rdd.RDD
import org.apache.spark.SparkContext
import mrs.miners.pitchfork.pitchforkReviewMiner
import mrs.download.download
import mrs.models.Review
import java.net.URL

// Ew nasty IO stuff
object pitchforkReviewRDD {
  def apply(sc: SparkContext): RDD[Review] = pitchforkReviewMiner(
    download(new URL("http://pitchfork.com/api/v1/albumreviews/?limit=1000")
    )
  )
}
