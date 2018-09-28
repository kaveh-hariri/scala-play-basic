
// @GENERATOR:play-routes-compiler
// @SOURCE:/opt/git/scala-play-basic/conf/routes
// @DATE:Fri Sep 28 03:59:52 GMT 2018


package router {
  object RoutesPrefix {
    private var _prefix: String = "/"
    def setPrefix(p: String): Unit = {
      _prefix = p
    }
    def prefix: String = _prefix
    val byNamePrefix: Function0[String] = { () => prefix }
  }
}
