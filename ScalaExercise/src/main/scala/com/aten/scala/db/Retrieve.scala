package com.aten.scala.db

import com.aten.scala.datasource.Datasource._
import com.aten.scala.mngmt.CloseManager
object Retrieve extends App {
  
  CloseManager.apply( connPool.getConnection ){
     conn =>
       CloseManager.apply( conn.createStatement ){
         stmt =>
           val rs = stmt.executeQuery("SELECT CITY FROM ADDRESS ")
           while( rs.next ) {
             println( s"""retrieved: ${rs.getString("CITY")}""")
           }
       }
  }
}