
// @GENERATOR:play-routes-compiler
// @SOURCE:/opt/git/scala-play-basic/conf/routes
// @DATE:Wed Sep 26 23:14:53 GMT 2018

package router

import play.core.routing._
import play.core.routing.HandlerInvokerFactory._
import play.core.j._

import play.api.mvc._

import _root_.controllers.Assets.Asset

class Routes(
  override val errorHandler: play.api.http.HttpErrorHandler, 
  // @LINE:6
  Home_0: controllers.Home,
  val prefix: String
) extends GeneratedRouter {

   @javax.inject.Inject()
   def this(errorHandler: play.api.http.HttpErrorHandler,
    // @LINE:6
    Home_0: controllers.Home
  ) = this(errorHandler, Home_0, "/")

  import ReverseRouteContext.empty

  def withPrefix(prefix: String): Routes = {
    router.RoutesPrefix.setPrefix(prefix)
    new Routes(errorHandler, Home_0, prefix)
  }

  private[this] val defaultPrefix: String = {
    if (this.prefix.endsWith("/")) "" else "/"
  }

  def documentation = List(
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """home/ufo_sightings""", """controllers.Home.ufo_sightings"""),
    Nil
  ).foldLeft(List.empty[(String,String,String)]) { (s,e) => e.asInstanceOf[Any] match {
    case r @ (_,_,_) => s :+ r.asInstanceOf[(String,String,String)]
    case l => s ++ l.asInstanceOf[List[(String,String,String)]]
  }}


  // @LINE:6
  private[this] lazy val controllers_Home_ufo_sightings0_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("home/ufo_sightings")))
  )
  private[this] lazy val controllers_Home_ufo_sightings0_invoker = createInvoker(
    Home_0.ufo_sightings,
    HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.Home",
      "ufo_sightings",
      Nil,
      "GET",
      """""",
      this.prefix + """home/ufo_sightings"""
    )
  )


  def routes: PartialFunction[RequestHeader, Handler] = {
  
    // @LINE:6
    case controllers_Home_ufo_sightings0_route(params) =>
      call { 
        controllers_Home_ufo_sightings0_invoker.call(Home_0.ufo_sightings)
      }
  }
}
