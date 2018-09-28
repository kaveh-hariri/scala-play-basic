
// @GENERATOR:play-routes-compiler
// @SOURCE:/opt/git/scala-play-basic/conf/routes
// @DATE:Fri Sep 28 03:59:52 GMT 2018

package controllers;

import router.RoutesPrefix;

public class routes {
  
  public static final controllers.ReverseHome Home = new controllers.ReverseHome(RoutesPrefix.byNamePrefix());

  public static class javascript {
    
    public static final controllers.javascript.ReverseHome Home = new controllers.javascript.ReverseHome(RoutesPrefix.byNamePrefix());
  }

}
