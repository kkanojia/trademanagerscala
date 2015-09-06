package com.kanokun.cts.rest

import collection.mutable.Stack
import org.scalatest._
import spray.testkit.ScalatestRouteTest
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import spray.http.HttpMethods._
import spray.http.StatusCodes._
import spray.httpx.SprayJsonSupport._
import com.kanokun.cts.entity.AssetPricesJsonProtocol._
import spray.httpx.SprayJsonSupport._
import com.kanokun.cts.entity.AssetPricesJsonProtocol._
import com.kanokun.cts.entity.FxRatesJsonProtocol._
/**
 * @author Kunal
 */
@RunWith(classOf[JUnitRunner])
class StaticAPITest extends FlatSpec with Matchers with StaticAPI with ScalatestRouteTest {
  
  def actorRefFactory = system

  "The service" should "return success mesages" in {

    Get("/rest/staticdata/assetprices") ~> staticDataRoute ~> check {
      status == OK
      //TODO check more things
    }
    
      Get("/rest/staticdata/fxrates") ~> staticDataRoute ~> check {
      status == OK
      //TODO check more things
    }

  }

}