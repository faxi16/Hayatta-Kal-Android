package com.maherlabbad.hayattakal.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maherlabbad.hayattakal.model.NewsItem
import com.maherlabbad.hayattakal.service.NewsCnnTurkApi
import com.maherlabbad.hayattakal.service.NewsHurriyetApi
import com.maherlabbad.hayattakal.service.NewsMilliyettApi
import com.maherlabbad.hayattakal.service.NewsSozcuApi
import com.maherlabbad.hayattakal.service.NewsTurkiyeGazApi
import com.maherlabbad.hayattakal.service.NewshaberTurkApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import org.jsoup.Jsoup
import org.jsoup.parser.Parser
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class NewsViewModel : ViewModel() {

    private val BASE_URL_SOZCU = "https://www.sozcu.com.tr/"
    private val BASE_URL_HABERTURK = "https://www.haberturk.com/"
    private val BASE_URL_TURKIYEGAZ = "https://www.turkiyegazetesi.com.tr/"
    private val BASE_URL_CNNTURK = "https://www.cnnturk.com/"
    private val BASE_URL_HURRIYET = "https://www.hurriyet.com.tr/"
    private val BASER_URL_MILLIYET = "https://www.milliyet.com.tr/"
    private val retrofitCnnTurk = Retrofit.Builder()
        .baseUrl(BASE_URL_CNNTURK)
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()
        .create(NewsCnnTurkApi::class.java)
    private val retrofitHaberturk = Retrofit.Builder()
        .baseUrl(BASE_URL_HABERTURK)
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()
        .create(NewshaberTurkApi::class.java)

    private val retrofitSozcu = Retrofit.Builder()
        .baseUrl(BASE_URL_SOZCU)
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()
        .create(NewsSozcuApi::class.java)

    private val retrofitTurkiyeGaz = Retrofit.Builder()
        .baseUrl(BASE_URL_TURKIYEGAZ)
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()
        .create(NewsTurkiyeGazApi::class.java)

    private val retrofitMilliyet = Retrofit.Builder()
        .baseUrl(BASER_URL_MILLIYET)
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()
        .create(NewsMilliyettApi::class.java)

    private val retrofitHurriyet = Retrofit.Builder()
        .baseUrl(BASE_URL_HURRIYET)
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()
        .create(NewsHurriyetApi::class.java)
    val newsItems = mutableStateOf<List<NewsItem>>(listOf())


    fun getLatestNews() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = retrofitCnnTurk.getLatestNewsCnnTurk()
                val responseHurriyet = retrofitHurriyet.getLatestNewsHurriyet()
                val responseMilliyet = retrofitMilliyet.getLatestNewsMilliyet()
                val responseTurkiyeGaz = retrofitTurkiyeGaz.getLatestNewsTurkiyeGaz()
                val responseSozcu = retrofitSozcu.getLatestNewsSozcu()
                val responseHaberturk = retrofitHaberturk.getLatestNewsHaberturk()
                val list = listOf(response, responseHurriyet, responseMilliyet, responseTurkiyeGaz, responseSozcu, responseHaberturk)
                newsItems.value = parseRss(list)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    private fun parseRss(rssXmls: List<String>) : List<NewsItem> {
        val newsItems = ArrayList<NewsItem>()
        rssXmls.forEach { rssXml ->
            val doc = Jsoup.parse(rssXml, "", Parser.xmlParser())
            val items = doc.select("item")
            for (item in items) {

                val title = item.selectFirst("title")?.text()
                val Pubdate = item.selectFirst("pubDate")?.text()
                val idx = Pubdate?.indexOfFirst { it == '+' || it == 'Z' || it == 'G' }
                val date = Pubdate?.substring(0,idx!!)
                val image = item.selectFirst("image")?.text() ?:item.selectFirst("media|content")?.attr("url") ?: Jsoup.parse(item.selectFirst("description")!!.text(),"",Parser.xmlParser()).selectFirst("img")?.attr("src")
                val link = item.selectFirst("link")?.text() ?:item.selectFirst("atom|link")?.attr("href")
                if(title!!.contains("Deprem",true)){
                    val newsItem = NewsItem(title,link!!,date!!,image ?: "")
                    newsItems.add(newsItem)
                }
            }
        }
        return newsItems
    }

}