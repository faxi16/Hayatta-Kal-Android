package com.maherlabbad.hayattakal.service

import retrofit2.http.GET

interface NewsCnnTurkApi {
    @GET("feed/rss/all/news")
    suspend fun getLatestNewsCnnTurk(): String
}

interface NewsHurriyetApi{
    @GET("rss/son-dakika")
    suspend fun getLatestNewsHurriyet(): String
}
interface NewsMilliyettApi{
    @GET("rss/rssnew/sondakikarss.xml")
    suspend fun getLatestNewsMilliyet(): String
}

interface NewsTurkiyeGazApi{
    @GET("rss/rss.xml")
    suspend fun getLatestNewsTurkiyeGaz(): String
}

interface NewsSozcuApi{
    @GET("feeds-son-dakika")
    suspend fun getLatestNewsSozcu(): String
}

interface NewshaberTurkApi{
    @GET("rss/yerel-haberler.xml")
    suspend fun getLatestNewsHaberturk(): String

}