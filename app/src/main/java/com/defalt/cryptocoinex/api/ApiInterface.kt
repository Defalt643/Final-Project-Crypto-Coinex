package com.defalt.cryptocoinex.api

import com.defalt.cryptocoinex.model.MarketModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers


interface ApiInterface {
//    @Headers("X-CMC_PRO_API_KEY : 3791046c-f499-4a0c-ac8a-01c819bb7a61")
    @GET("https://api.coinmarketcap.com/data-api/v3/cryptocurrency/listing?start=1&limit=500&convert=THB")
    suspend fun getMarketData() : Response<MarketModel>


}