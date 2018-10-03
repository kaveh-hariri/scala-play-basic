package utilities

import com.github.martincooper.datatable.{DataColumn, DataRow, DataTable}
import it.unimi.dsi.fastutil._
import org.joda.time.DateTime
import play.api.{Environment, Play}
import play.api.Play.current
import java.util.concurrent._
import scala.collection.JavaConversions._
import scala.collection.mutable
import scala.util.{Failure, Success, Try}
import scala.reflect.runtime.universe

object Data {

  //make data available
  private val data: DataTable = createDataTable().get
  val finData: DataTable = data
  //load column names
  val ocolumns: List[String] = data.columns.map(a => a.name).toList
  val cache = new ConcurrentHashMap[String, Any]()
  val index = new ConcurrentHashMap[String,Long]()


  //create data table, future: compare performance against each column in its own map(id,value)
  def createDataTable(): Try[DataTable] = {
    //get data from resource file -- in prod would be coming from a durable datasource
    val rwd = play.api.Play.resourceAsStream("ufo-sightings.csv").get
    val listData = scala.io.Source.fromInputStream(rwd, "UTF-8").getLines().toList
    val columns = listData.head.split(",").toList

    //create columns with values

    val clmn0 = new DataColumn[Int](columns(0), listData.drop(1).map(a => a.split(",")(0).toInt))
    val clmn1 = new DataColumn[String](columns(1), listData.drop(1).map(a => a.split(",")(1).trim))
    val clmn2 = new DataColumn[String](columns(2), listData.drop(1).map(a => a.split(",")(2).trim))
    val clmn3 = new DataColumn[String](columns(3), listData.drop(1).map(a => a.split(",")(3).trim))
    val clmn4 = new DataColumn[String](columns(4), listData.drop(1).map(a => a.split(",")(4).trim))
    //clean data, found in shape empty string as well as unknown
    val clmn5 = new DataColumn[String](columns(5), listData.drop(1).map(a => a.split(",")(5).trim.replace("unknown", "")))
    //clean data, to double conversion fails due to bad data
    val clmn6 = new DataColumn[Double](columns(6), listData.drop(1).map(a => {
      var v = a.split(",")(6).replaceAll("[^\\d.]", "").replace(" ", "")
      Try(v.toDouble)
      match {
        case Success(s) => s.toDouble
        case Failure(f) => -10000
      }
    }))
    val clmn7 = new DataColumn[String](columns(7), listData.drop(1).map(a => a.split(",")(7).trim))
    val clmn8 = new DataColumn[String](columns(8), listData.drop(1).map(a => a.split(",")(8).trim))
    val clmn9 = new DataColumn[String](columns(9), listData.drop(1).map(a => a.split(",")(9).trim))
    val clmn10 = new DataColumn[Double](columns(10), listData.drop(1).map(a => {
      var v = a.split(",")(10).replaceAll("[^\\d.-]", "").replace(" ", "")
      Try(v.toDouble)
      match {
        case Success(s) => s.toDouble
        case Failure(f) => -10000
      }
    }))
    val clmn11 = new DataColumn[Double](columns(11), listData.drop(1).map(a => {
      var v = a.split(",")(11).replaceAll("[^\\d.-]", "").replace(" ", "")
      Try(v.toDouble)
      match {
        case Success(s) => s.toDouble
        case Failure(f) => -10000
      }
    }))
    //create data table
    DataTable("data_table", Seq(clmn0, clmn1, clmn2, clmn3, clmn4, clmn5, clmn6, clmn7, clmn8, clmn9, clmn10, clmn11))
  }

  // count requests
  def count(params: Map[String, String]): Map[String, Int] = {
    val actionType = params.getOrElse("actiontype", "").toLowerCase

    actionType match {
      case "total" => {
        val key = "count:" + params.map(v => s"${v._1};${v._2})").mkString(":")
        val tmp = cache.getOrElse(key ,  null).asInstanceOf[Map[String,Int]]
        if(tmp != null) {
          index.put(key,System.currentTimeMillis())
          tmp
        }
        else {
          val fin = Map("count" -> data.size)
          cache.put(key,fin)
          index.put(key,System.currentTimeMillis())
          CacheClean.cleanCache(cache,index)
          fin
        }
      }
      case "distinct" => {
        val column = params.getOrElse("column", "").toLowerCase

        if (!ocolumns.contains(column)) {
          throw new IllegalStateException("Exception: proper column not provided")
        }
        val key = "count:" + params.map(v => s"${v._1};${v._2})").mkString(":")
        val tmp = cache.getOrElseUpdate(key,null).asInstanceOf[Map[String,Int]]
        if(tmp != null){
          index.put(key,System.currentTimeMillis())
          tmp
        }
        else {
          val fin = Map("count" -> data.map(rei => rei.as[Any](column)).filterNot(rei2 => rei2.toString.equalsIgnoreCase("")).distinct.size)
          cache.put(key,fin)
          index.put(key,System.currentTimeMillis())
          CacheClean.cleanCache(cache,index)
          fin
        }

      }
      //total and distinct are supported actionTypes for action count, return error otherwise
      case _ => throw new IllegalStateException("Exception: proper actiontype not provided")
    }

  }

  //top city request
  def topCity(params: Map[String, String]): models.top = {

    val quantity: Int = Try(params.getOrElse("quantity", "").toInt)
    match {
      case Success(s) => s
      case Failure(f) => -1
    }
    val sortOrder = params.getOrElse("sortorder", "").toLowerCase

    if (quantity == -1 || !(sortOrder.equalsIgnoreCase("asc") || sortOrder.equalsIgnoreCase("desc"))) {
      throw new IllegalStateException("Exception: proper quantity/sortorder not provided")
    }
    //depending on the sort order
    sortOrder match {
      case "asc" => {
        val key = "topcity:" + params.map(v => s"${v._1};${v._2})").mkString(":")
        val tmp = cache.getOrElse(key,  null).asInstanceOf[models.top]
        if(tmp != null){
          index.put(key,System.currentTimeMillis())
          tmp
        }
        else {
            val fin = models.top(data.map(rei => {
            val st = rei.as[String]("city")
            // remove extra information enclosed in (
            val sliceEnd = if (st.indexOfSlice("(") == -1) {
              st.length
            } else st.indexOfSlice("(")
            st.slice(0, sliceEnd).trim
          }).groupBy(identity).mapValues(_.size).toSeq.map(a => models.count(a._1, a._2))
            .sorted(Ordering.by((_: models.count).count)).take(quantity))
          cache.put(key, fin)
          index.put(key,System.currentTimeMillis())
          CacheClean.cleanCache(cache,index)
          fin
        }
      }

      case "desc" => {
        val key = "topcity:" + params.map(v => s"${v._1};${v._2})").mkString(":")
        val tmp = cache.getOrElse(key,  null).asInstanceOf[models.top]
        if(tmp != null){
          index.put(key,System.currentTimeMillis())
          tmp
        }
        else {
          val fin = models.top(data.map(rei => {
            val st = rei.as[String]("city")
            // remove extra information enclosed in (
            val sliceEnd = if (st.indexOfSlice("(") == -1) {
              st.length
            } else st.indexOfSlice("(")
            st.slice(0, sliceEnd).trim
          }).groupBy(identity).mapValues(_.size).toSeq.map(a => models.count(a._1, a._2))
            .sorted(Ordering.by((_: models.count).count).reverse).take(quantity))
          cache.put(key, fin)
          index.put(key,System.currentTimeMillis())
          CacheClean.cleanCache(cache,index)
          fin
        }
      }

    }
  }

    def closestLocs(params: Map[String, String]): models.closest = {

      val quantity: Int = Try(params.getOrElse("quantity", "").toInt)
      match {
        case Success(s) => s
        case Failure(f) => -1
      }
      val lon1 = Try(params.getOrElse("longitude", "").toDouble)
      match {
        case Success(s) => s
        case Failure(f) => 10000
      }
      val lat1 = Try(params.getOrElse("latitude", "").toDouble)
      match {
        case Success(s) => s
        case Failure(f) => 10000
      }

      if (quantity == -1 || lon1 == 10000 || lat1 == 10000) {
        throw new IllegalStateException("Exception: proper quantity/longitude/latitude not provided")
      }
      //Possible performance improvements -- distinct list of lon/lat, and possibly round lon/lat to int and take the distinct.  Not needed
      val key = "closestlocs:" + params.map(v => s"${v._1};${v._2})").mkString(":")
      val tmp = cache.getOrElse(key,null).asInstanceOf[models.closest]
      if(tmp != null){
        index.put(key,System.currentTimeMillis())
        tmp
      }
      else {
        val a: List[(DataRow, Float)] = data.map(rei => {
          rei -> List(rei.as[Double]("latitude"), rei.as[Double]("longitude"))
        })
          .map((z: (DataRow, List[Double])) => z._1 -> HaversineSecond.distance(lat1, lon1, z._2(0), z._2(1))).toList
          .sortBy(s => s._2).take(quantity)

        val fin = a.map(rei => models.sightings(Option(rei._1.as[Int](0)), Option(rei._1.as[String](1)), Option(rei._1.as[String](2))
          , Option(rei._1.as[String](3)), Option(rei._1.as[String](4)), Option(rei._1.as[String](5)), Option(rei._1.as[Double](6))
          , Option(rei._1.as[String](7)), Option(rei._1.as[String](8)), Option(rei._1.as[String](9)), Option(rei._1.as[Double](10))
          , Option(rei._1.as[Double](11)), Option(rei._2)))
        cache.put(key,models.closest(fin))
        index.put(key,System.currentTimeMillis())
        CacheClean.cleanCache(cache,index)
        models.closest(fin)
      }
    }
}
