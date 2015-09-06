package com.kanokun.cts.entity

import org.joda.time.DateTime
import spray.json.DefaultJsonProtocol
import com.kanokun.cts.utils.JodaDateTimeMarshaller



/**
 * @author Kunal
 */
case class FxRates(dataDate:DateTime, gbpRate:Double, eurRate:Double)

object FxRatesJsonProtocol extends DefaultJsonProtocol  with JodaDateTimeMarshaller{
  
 implicit val fxFormat = jsonFormat3(FxRates.apply)
 
}
