
// @GENERATOR:play-routes-compiler
// @SOURCE:/opt/git/scala-play-basic/conf/routes
// @DATE:Wed Sep 26 23:14:53 GMT 2018


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
