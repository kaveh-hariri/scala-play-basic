package controllers

import models.{count, top}
import play.api.libs.json._
import play.api.mvc.{Action, BodyParsers, Controller}
import play.api.libs.functional.syntax._

import scala.util.{Failure, Success, Try}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class Home extends Controller{


  implicit val countWrites = new Writes[models.count]{
    override def writes(o: count) = Json.obj(
      "city" -> o.city,
      "count" -> o.count
    )
  }


  implicit val topWrites = new Writes[models.top]{
    override def writes(o: top) = Json.obj(
      "sightings" -> o.sightings
    )
  }

  implicit val sightingsWrites = new Writes[models.sightings]{
    override def writes(o: models.sightings) = Json.obj(
      "id" -> o.id
      , "occurred_at" -> o.occurred_at
      , "city" -> o.city
      , "state" -> o.state
      , "country" -> o.country
      , "shape" -> o.shape
      , "duration" -> o.duration
      , "duration_text" -> o.duration_text
      ,
    )
  }

  def ufo_sightings  = Action.async { request => {
    val params = request.queryString.map{case (k,v) => k -> v.mkString}
    val action = params.getOrElse("action","").toLowerCase

    action match {
      case "count" => {
        Try(utilities.Data.count(params))
        match {
          case Success(s) => Future(Ok(Json.toJson(s)))
          case Failure(f) => Future(BadRequest(Json.obj("status" -> "Bad Request", "message" -> f.getMessage)))
        }
      }
      case "topcity" => {
        Try(utilities.Data.topCity(params))
        match {
          case Success(s) => Future(Ok(Json.toJson(s)))
          case Failure(f) =>   Future(BadRequest(Json.obj("status" -> "Bad Request", "message" -> f.getMessage)))
        }
      }
      case _ =>   Future(BadRequest(Json.obj("status" -> "Bad Request", "message" -> "Exception: bad action in query string")))
    }

   }
  }

}
