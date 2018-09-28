
// @GENERATOR:play-routes-compiler
// @SOURCE:/opt/git/scala-play-basic/conf/routes
// @DATE:Fri Sep 28 03:59:52 GMT 2018

import play.api.routing.JavaScriptReverseRoute
import play.api.mvc.{ QueryStringBindable, PathBindable, Call, JavascriptLiteral }
import play.core.routing.{ HandlerDef, ReverseRouteContext, queryString, dynamicString }


import _root_.controllers.Assets.Asset

// @LINE:6
package controllers.javascript {
  import ReverseRouteContext.empty

  // @LINE:6
  class ReverseHome(_prefix: => String) {

    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:6
    def ufo_sightings: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.Home.ufo_sightings",
      """
        function() {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "home/ufo_sightings"})
        }
      """
    )
  
  }


}
