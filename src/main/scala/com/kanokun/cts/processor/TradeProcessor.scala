package com.kanokun.cts.processor

import com.kanokun.cts.entity.TradeEntry
import com.kanokun.cts.entity.TradeForm
import org.joda.time.DateTime
import com.kanokun.cts.utils.CachedData
import com.kanokun.cts.entity.TradeEntry
import com.kanokun.cts.dao.TradeDao
import scala.annotation.tailrec

/**
 * This should be an independent actor. but sadly no time for this
 * @author Kunal
 *
 */
object TradeProcessor {

  def processAndSaveTrade(trade: TradeForm): Option[Long] = {
    trade.buySell.toLowerCase() match {
      case "buy"  => processBuyTrade(trade)
      case "sell" => processSellTrade(trade)
      case _      => None
    }
  }

  def processBuyTrade(trade: TradeForm): Option[Long] = {
    TradeDao.saveTrade(new TradeEntry(0, trade.tradeDate, trade.buySell, trade.assetId, trade.quantity, false, trade.quantity,
      0, getUsdPrice(trade.assetId, trade.tradeDate), 0.0, "", "", 0.0))
  }

  def processSellTrade(trade: TradeForm): Option[Long] = {
    val openTrades = TradeDao.getAllNonSettledEntry(trade.assetId, trade.tradeDate);
    val buyOpenQuantity = openTrades.foldLeft(0)(_ + _.openQuantity);
    val usdPrice = getUsdPrice(trade.assetId, trade.tradeDate)
    
    buyOpenQuantity >= trade.quantity match {
      case false => None
      case true => {
        val profit = settleTrades(trade.quantity, openTrades.sortBy(_.id),usdPrice)
        val sellEntry = new TradeEntry(0, trade.tradeDate, trade.buySell, trade.assetId, trade.quantity, false, 0,
          profit, usdPrice, 0.0, "", "", 0.0)
        TradeDao.saveTrade(sellEntry)
      }
    }
  }

  @tailrec
  def settleTrades(openquantity: Int, openTrades: List[TradeEntry], sellPriceUSD:Double, profit: Double = 0.0): Double = {
    openquantity > 0 match {
      case false => profit
      case true => {
        openTrades.head.openQuantity >= openquantity match {
          case true => {
            TradeDao.updateTrade(openTrades.head.id, false, openTrades.head.openQuantity - openquantity)
            profit + openquantity * ( sellPriceUSD - openTrades.head.priceUSD);
          }
          case false => {
            TradeDao.updateTrade(openTrades.head.id, true, 0)
            val profitnew =  profit +  openTrades.head.openQuantity * ( sellPriceUSD - openTrades.head.priceUSD);
            settleTrades(openquantity - openTrades.head.openQuantity , openTrades.tail, sellPriceUSD, profitnew)
          }
        }
      }
    }
  }

  def getUsdPrice(assetId: Int, date: DateTime): Double = {
    val currencyId = CachedData.assetCurrencyCache.getOrElse(assetId, 0)
    CachedData.fxratecache.get(date.toDate().getTime).get.getOrElse(currencyId, 0.0) *
      CachedData.assetPriceCache.get(date.toDate().getTime).get.getOrElse(assetId, 0.0)
  }

}