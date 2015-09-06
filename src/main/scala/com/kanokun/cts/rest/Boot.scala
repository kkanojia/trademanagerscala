package com.kanokun.cts.rest

import akka.actor.{ActorSystem, Props}
import akka.io.IO
import spray.can.Http
import akka.routing.RoundRobinPool

object Boot extends App {

  implicit val system = ActorSystem("trade-manager-system")

  val service = system.actorOf(Props[TradeManagerRestActor].withRouter(RoundRobinPool(10)), "tradeManagerRestActor")

  IO(Http) ! Http.Bind(service, interface = "0.0.0.0", port = scala.util.Properties.envOrElse("PORT", "8080").toInt)
  
}
