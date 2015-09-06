package com.kanokun.cts.utils

import com.kanokun.cts.dao.DatabaseManager
import com.kanokun.cts.dao.StaticDataDao

object CachedData {

  val currencyCache = Map(1 -> "EUR", 2 -> "GBP", 3 -> "USD")

  val assetsCache = Map(1 -> "ABC", 2 -> "DEF", 3 -> "XYZ")

  val assetCurrencyCache = Map(1 -> 3, 2 -> 2, 3 -> 1)

  val fxratecache = StaticDataDao.getFxRates()

  val assetPriceCache = StaticDataDao.getAssetPrices()

}