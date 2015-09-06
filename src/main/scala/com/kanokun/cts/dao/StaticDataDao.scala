package com.kanokun.cts.dao

import java.sql.{ Connection, DriverManager }
import scalikejdbc.ConnectionPool
import anorm._
import anorm.SqlParser._
import java.util.Date
import com.kanokun.cts.entity.AssetPrices
import org.joda.time.DateTime
import com.kanokun.cts.entity.FxRates

/**
 * @author Kunal
 */
object StaticDataDao {

  import com.kanokun.cts.dao.DatabaseManager._

  //Ideally not required can be read from cache. But is only called once on startup
  def getAssetPricesForUI(): Seq[AssetPrices] = {
    val assetPrices: String = """select data_date,ABC,DEF,XYZ FROM   crosstab(
                 'select p.data_date, a.asset_code, p.closing_price from asset a, assetprices p 
                 where a.asset_id = p. asset_id order by 1,2') 
                AS ct (data_date date, ABC numeric, DEF numeric, XYZ numeric)"""

    SQL(assetPrices)().map(
      assets => new AssetPrices(assets[DateTime]("data_date"), assets[Double]("ABC"), assets[Double]("DEF"), assets[Double]("XYZ"))).toSeq
  }

   //Ideally not required can be read from cache. But is only called once on startups
  def getFxRatesForUI(): Seq[FxRates] = {
    val fxrates: String = """SELECT data_date,EUR,GBP FROM 
             crosstab('select f.data_date, c.name, f.rate from fxrates f, currency c 
       where f.currency_id = c.currency_id order by 1,2') 
       AS ct ( data_date  date, EUR numeric, GBP numeric)"""

    SQL(fxrates)().map(
      fx => new FxRates(fx[DateTime]("data_date"), fx[Double]("GBP"), fx[Double]("EUR"))).toSeq
  }

  def getFxRates(): Map[Long, Map[Int, Double]] = {
    val fxratessql: String = """SELECT data_date,EUR,GBP FROM 
             crosstab('select f.data_date, c.name, f.rate from fxrates f, currency c 
       where f.currency_id = c.currency_id order by 1,2') 
       AS ct ( data_date  date, EUR numeric, GBP numeric)"""

    SQL(fxratessql)().map(
      fx => (fx[DateTime]("data_date").toDate().getTime, Map(1 -> fx[Double]("EUR"), 2 -> fx[Double]("GBP"), 3 -> 1.0))).toMap
  }

  def getAssetPrices(): Map[Long, Map[Int, Double]] = {
    val assetPrices: String = """select data_date,ABC,DEF,XYZ FROM   crosstab(
                 'select p.data_date, a.asset_code, p.closing_price from asset a, assetprices p 
                 where a.asset_id = p. asset_id order by 1,2') 
                AS ct (data_date date, ABC numeric, DEF numeric, XYZ numeric)"""

    SQL(assetPrices)().map(
      ap => (ap[DateTime]("data_date").toDate().getTime, Map(1 -> ap[Double]("ABC"), 2 -> ap[Double]("DEF"), 3 -> ap[Double]("XYZ")))).toMap
  }
}