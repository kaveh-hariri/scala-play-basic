package utilities
import java.lang.instrument._
import java.util.concurrent.ConcurrentHashMap
import java.util.jar.JarFile
import com.madhukaraphatak.sizeof.SizeEstimator
import scala.collection.JavaConversions._
import play.api._
object CacheClean {

  private val cacheSize =  play.Play.application.configuration.getInt("cache.size.bytes")


  def getSizeOfCache(cache: ConcurrentHashMap[String, Any]): Long = {
    SizeEstimator.estimate(cache)
  }

  def cleanCache(cache: ConcurrentHashMap[String, Any], index: ConcurrentHashMap[String, Long]): Unit = {
    while(getSizeOfCache(cache)> cacheSize){
     val key = index.toList.minBy(a => a._2)._1
      cache.remove(key)
      index.remove(key)
    }
  }

}
