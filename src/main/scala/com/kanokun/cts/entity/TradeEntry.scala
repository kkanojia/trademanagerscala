package com.kanokun.cts.entity

import spray.json.DefaultJsonProtocol
import spray.json.RootJsonFormat
import spray.json._
import java.util.Formatter._
import org.joda.time.format.ISODateTimeFormat
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormatter
import anorm._
import anorm.SqlParser._
import com.kanokun.cts.utils.JodaDateTimeMarshaller
import spray.httpx.SprayJsonSupport

case class TradeEntry(id: Long, tradeDate: DateTime,buySell:String, assetId: Int, quantity: Int, settled: Boolean, openQuantity: Int,
                      profit: Double, priceUSD: Double, var cumulativeProfit: Double, var assetCode: String, 
                      var currency: String, var pricelocal: Double) 
                      
object TradeEntryJsonProtocol extends DefaultJsonProtocol with JodaDateTimeMarshaller{
  implicit val tradeEntryFormat = jsonFormat13(TradeEntry.apply)
}

case class TradeForm(tradeDate: DateTime,buySell:String, assetId: Int,quantity: Int)

object TradeFormJsonProtocol extends DefaultJsonProtocol with JodaDateTimeMarshaller {
  implicit val tradeformFormat = jsonFormat4(TradeForm.apply)
}