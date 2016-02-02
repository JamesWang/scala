package com.aten.scala.datasource

import org.apache.commons.dbcp2.BasicDataSource

object Datasource {
  val dbUrl = "jdbc:postgresql://your_db_host:5432/postgres"
  
  val connPool = new BasicDataSource
  connPool.setUsername("postgres")
  connPool.setPassword("postgres")
  
  connPool.setDriverClassName("org.postgresql.Driver")
  connPool.setUrl( dbUrl )
  connPool.setInitialSize(2)
}