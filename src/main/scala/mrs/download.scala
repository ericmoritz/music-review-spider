package mrs.download
import java.net.URL
import org.apache.commons.io.IOUtils

object download {
  def apply(url: URL): String = IOUtils.toString(url, "UTF-8")
}
