// @GENERATOR:play-routes-compiler
// @SOURCE:D:/eclipse-workspace/ITSD-DT2021-Template/conf/routes
// @DATE:Fri Jun 11 05:56:47 CST 2021


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
