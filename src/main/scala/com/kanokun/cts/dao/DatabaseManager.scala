package com.kanokun.cts.dao

import java.sql.{ Connection, DriverManager }
import scalikejdbc.ConnectionPool
import anorm._

object DatabaseManager {

  //Read these from properties file.
  ConnectionPool.singleton("jdbc:postgresql://ec2-54-197-245-93.compute-1.amazonaws.com:5432/d4i0pcqp1ed6mu?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory",
    "menxmcqdjpamji", "1nRk4kqckYUaeLioSpb-mjTdG1")

  implicit val conn: Connection = ConnectionPool.borrow()

 

}