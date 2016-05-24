package com.kanokun.cts.dao

import java.sql.{ Connection, DriverManager }
import scalikejdbc.ConnectionPool
import anorm._

object DatabaseManager {

  //FIXME - Uncomment and replace with appropriate JDBC details for connection to database.
  //TODO Read these from properties file. 
  //ConnectionPool.singleton(JDBC_URL, USER, PASSWORD)

  implicit val conn: Connection = ConnectionPool.borrow()

 

}
