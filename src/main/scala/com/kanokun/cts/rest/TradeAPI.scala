package com.kanokun.cts.rest

import shapeless.get
import com.kanokun.cts.dao.StaticDataDao
import spray.routing.HttpService
import com.kanokun.cts.dao.TradeDao
import com.kanokun.cts.entity.TradeForm
import com.kanokun.cts.processor.TradeProcessor
import com.kanokun.cts.utils.CachedData._
import spray.httpx.SprayJsonSupport._
import com.kanokun.cts.entity.TradeFormJsonProtocol._

/**
 * @author Kunal
 */
trait TradeAPI extends HttpService {

  val tradeRoutes =
    (path("rest" / "transaction") & post) {
      entity(as[TradeForm]) { trade =>
        complete("{ \"count\" : " + TradeProcessor.processAndSaveTrade(trade).getOrElse(0.toLong) + " }")
      }
    } ~
      (path("rest" / "transaction" / "all") & get) {
        import spray.httpx.SprayJsonSupport._
        import com.kanokun.cts.entity.TradeEntryJsonProtocol._

        complete(TradeDao.getAllTrades().sortBy(_.id).map {
          var value = 0.0
          trd => {
            trd.cumulativeProfit = value + trd.profit
            trd.assetCode = assetsCache.getOrElse(trd.assetId, "")
            trd.pricelocal = assetPriceCache.get(trd.tradeDate.toDate().getTime).get.getOrElse(trd.assetId, 0)
            trd.currency = currencyCache.getOrElse(assetCurrencyCache.getOrElse(trd.assetId, 0), "")
            value = trd.cumulativeProfit
            trd
          }
        })
      } ~ path("rest" / "transaction" / "deleteAll") {
        complete("{ \"count\" : " + TradeDao.deleteAllTrades() + " }")
      }

}