package com.aten.scala.ps.http

object Pure {

  trait Resource {
    def exists : Boolean
    def contents: List[String]
    def contentLength:Int
  }
  
  type ResourceLocator = String => Resource
  type Request = Iterator[Char]
  type Response = List[String]
  
  def get( req:Request)( implicit locator : ResourceLocator):Response = {
    val requestedResource = 
      req.takeWhile { x => x != '\n' }
         .mkString
         .split(" ")(1)
         .drop(1)
    (_200 orElse _404 )( locator(requestedResource ) )
  }
  /**
   * PartialFunction will be compiled to generate a method
   * called isDefinedAt( a: A ), in _200 case, and it 
   * will be called to check if the passed parameter 
   * item is defined for the function, if not, then it will
   * call next chained ( composed ) PartialFunction, in above
   * case, the PartialFunction is _404 which captures all ohter
   * cases
   */
  private def _200:PartialFunction[Resource,Response] = {
    case resource if( resource.exists) =>
      "HTTP/1.1 200 OK" ::
      ( "Date " + new java.util.Date() )::
      "Content-Type:text/html" ::
      ("Content-Length:" + resource.contentLength)::
      System.getProperty("line.separator")::
      resource.contents      
  }
  private def _404:PartialFunction[Resource,Response] ={
    case _ => List("HTTP/1.1 404 Not Found")
  }
}