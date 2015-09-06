package com.kanokun.cts.dao

import java.sql.{ Connection, DriverManager }
import scalikejdbc.ConnectionPool
import anorm._
import anorm.SqlParser._
import org.joda.time.DateTime
import com.kanokun.cts.entity.TradeEntry

/**
 * @author Kunal
 */
object TradeDao {

  import com.kanokun.cts.dao.DatabaseManager._

  def getAllTrades(): Seq[TradeEntry] = {
    println("Getting all trades")
    val tradesquery = "select id,tradedate,buySell,assetid,quantity,settled,openquantity,profit,priceusd from tradeinformation"
    SQL(tradesquery)().map(
      trades => new TradeEntry(trades[Long]("id"), trades[DateTime]("tradedate"), trades[String]("buySell"), trades[Int]("assetid"), trades[Int]("quantity"),
        trades[Boolean]("settled"), trades[Int]("openquantity"), trades[Double]("profit"),
        trades[Double]("priceusd"), 0.0, "", "", 0.0)).toSeq
  }

  def saveTrade(entry: TradeEntry): Option[Long] = {
        val newinsertQuery = """insert into tradeinformation(id,tradedate,buySell,assetId,quantity,settled,openQuantity,profit,priceUSD) values( 
             nextval('trade_seq'),{tradedate}, {buySell},{assetId}, {quantity},{settled}, {openQuantity},{profit}, {priceUSD})"""
        SQL(newinsertQuery)
          .on("tradedate" -> entry.tradeDate.toDate(),
            "buySell" -> entry.buySell,
            "assetId" -> entry.assetId,
            "quantity" -> entry.quantity,
            "settled" -> entry.settled,
            "openQuantity" -> entry.openQuantity,
            "profit" -> entry.profit,
            "priceUSD" -> entry.priceUSD).executeInsert()
  }
  
  def updateTrade(id:Long, settled:Boolean, openQuantity:Int) :Int = {
     val  update = """update  tradeinformation  set  settled = {settled},openQuantity = {openQuantity} 
           where id = {id}"""
        SQL(update)
          .on("id" -> id,
            "settled" -> settled,
            "openQuantity" -> openQuantity).executeUpdate()
  }

  def getAllNonSettledEntry(assetid: Int, date: DateTime): List[TradeEntry] = {
    val selectQuery = """select id,tradedate,buySell,assetid,quantity,settled,openquantity,profit,priceusd from tradeinformation WHERE buySell  = {buySell} 
       and settled = {settled} and assetId = {assetid} and tradeDate < {date}"""

    SQL(selectQuery).on("buySell" -> "Buy",
      "settled" -> false,
      "assetid" -> assetid,
      "date" -> date.toDate())().map(
        trades => new TradeEntry(trades[Long]("id"), trades[DateTime]("tradedate"), trades[String]("buySell"), trades[Int]("assetid"),
          trades[Int]("quantity"), trades[Boolean]("settled"), trades[Int]("openquantity"), trades[Double]("profit"),
          trades[Double]("priceusd"), 0.0, "", "", 0.0)).toList
  }
  
  def deleteAllTrades():Int ={
      println("deleting all trades")
     SQL("delete from tradeinformation").executeUpdate()
  }

}