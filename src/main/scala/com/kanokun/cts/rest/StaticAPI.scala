package com.kanokun.cts.rest

import shapeless.get
import com.kanokun.cts.dao.StaticDataDao
import spray.routing.HttpService

/**
 * @author Kunal
 */
trait StaticAPI extends HttpService{
  
  val staticDataRoute = (path("rest" / "staticdata" / "assetprices") & get) {
        import spray.httpx.SprayJsonSupport._
        import com.kanokun.cts.entity.AssetPricesJsonProtocol._

        val assets = StaticDataDao.getAssetPricesForUI()
        complete(assets)
      } ~
      (path("rest" / "staticdata" / "fxrates") & get) {
        import spray.httpx.SprayJsonSupport._
        import com.kanokun.cts.entity.FxRatesJsonProtocol._

        val fxRates = StaticDataDao.getFxRatesForUI()
        complete(fxRates)
      }
}