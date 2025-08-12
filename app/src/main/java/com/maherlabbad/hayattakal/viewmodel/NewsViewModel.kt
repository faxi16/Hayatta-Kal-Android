package com.maherlabbad.hayattakal.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.maherlabbad.hayattakal.R
import com.maherlabbad.hayattakal.model.NewsItem
import com.maherlabbad.hayattakal.service.NewsCnnTurkApi
import com.maherlabbad.hayattakal.service.NewsHurriyetApi
import com.maherlabbad.hayattakal.service.NewsMilliyettApi
import com.maherlabbad.hayattakal.service.NewsSozcuApi
import com.maherlabbad.hayattakal.service.NewsTurkiyeGazApi
import com.maherlabbad.hayattakal.service.NewshaberTurkApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import okhttp3.TlsVersion
import retrofit2.Retrofit
import org.jsoup.Jsoup
import org.jsoup.parser.Parser
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.security.KeyStore
import java.security.SecureRandom
import java.security.cert.CertificateFactory
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

class NewsViewModel(application: Application) : AndroidViewModel(application) {

    private val BASE_URL_SOZCU = "https://www.sozcu.com.tr/"
    private val BASE_URL_HABERTURK = "https://www.haberturk.com/"
    private val BASE_URL_TURKIYEGAZ = "https://www.turkiyegazetesi.com.tr/"
    private val BASE_URL_CNNTURK = "https://www.cnnturk.com/"
    private val BASE_URL_HURRIYET = "https://www.hurriyet.com.tr/"
    private val BASER_URL_MILLIYET = "https://www.milliyet.com.tr/"
    val modernTlsSpec = ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
        .tlsVersions(TlsVersion.TLS_1_3, TlsVersion.TLS_1_2)
        .build()

    fun getSecureOkHttpClientMultipleCerts(context: Context): OkHttpClient {
        val cf = CertificateFactory.getInstance("X.509")

        val keyStore = KeyStore.getInstance(KeyStore.getDefaultType())
        keyStore.load(null, null)


        val caInput1 = context.resources.openRawResource(R.raw.cnnturk_cert)
        val ca1 = cf.generateCertificate(caInput1)
        caInput1.close()
        keyStore.setCertificateEntry("cnnturk", ca1)


        val caInput2 = context.resources.openRawResource(R.raw.sozcu_cert)
        val ca2 = cf.generateCertificate(caInput2)
        caInput2.close()
        keyStore.setCertificateEntry("sozcu", ca2)


        val caInput3 = context.resources.openRawResource(R.raw.haberturk_cert)
        val ca3 = cf.generateCertificate(caInput3)
        caInput3.close()
        keyStore.setCertificateEntry("haberturk", ca3)

        val caInput4 = context.resources.openRawResource(R.raw.hurriyet_)
        val ca4 = cf.generateCertificate(caInput4)
        caInput4.close()
        keyStore.setCertificateEntry("hurriyet", ca4)

        val caInput5 = context.resources.openRawResource(R.raw.milliyet_cert)
        val ca5 = cf.generateCertificate(caInput5)
        caInput5.close()
        keyStore.setCertificateEntry("milliyet", ca5)

        val caInput6 = context.resources.openRawResource(R.raw.turkiyegazetesi_cert)
        val ca6 = cf.generateCertificate(caInput6)
        caInput6.close()
        keyStore.setCertificateEntry("turkiyegazetesi", ca6)

        val tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
        tmf.init(keyStore)

        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(null, tmf.trustManagers, SecureRandom())

        return OkHttpClient.Builder()
            .sslSocketFactory(sslContext.socketFactory, tmf.trustManagers[0] as X509TrustManager)
            .build()
    }



    val okHttpClient = getSecureOkHttpClientMultipleCerts(application)

    private val retrofitCnnTurk = Retrofit.Builder()
        .baseUrl(BASE_URL_CNNTURK)
        .client(okHttpClient)
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()
        .create(NewsCnnTurkApi::class.java)
    private val retrofitHaberturk = Retrofit.Builder()
        .baseUrl(BASE_URL_HABERTURK)
        .client(okHttpClient)
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()
        .create(NewshaberTurkApi::class.java)

    private val retrofitSozcu = Retrofit.Builder()
        .baseUrl(BASE_URL_SOZCU)
        .client(okHttpClient)
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()
        .create(NewsSozcuApi::class.java)

    private val retrofitTurkiyeGaz = Retrofit.Builder()
        .baseUrl(BASE_URL_TURKIYEGAZ)
        .client(okHttpClient)
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()
        .create(NewsTurkiyeGazApi::class.java)

    private val retrofitMilliyet = Retrofit.Builder()
        .baseUrl(BASER_URL_MILLIYET)
        .client(okHttpClient)
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()
        .create(NewsMilliyettApi::class.java)

    private val retrofitHurriyet = Retrofit.Builder()
        .baseUrl(BASE_URL_HURRIYET)
        .client(okHttpClient)
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()
        .create(NewsHurriyetApi::class.java)
    val newsItems = mutableStateOf<List<NewsItem>>(listOf())

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getLatestNews() {
        viewModelScope.launch(Dispatchers.IO) {
            try {

                val response = async { retrofitCnnTurk.getLatestNewsCnnTurk() }
                val responseHurriyet = async { retrofitHurriyet.getLatestNewsHurriyet()}
                val responseMilliyet = async{ retrofitMilliyet.getLatestNewsMilliyet()}
                val responseTurkiyeGaz = async{retrofitTurkiyeGaz.getLatestNewsTurkiyeGaz()}
                val responseSozcu = async{ retrofitSozcu.getLatestNewsSozcu()}
                val responseHaberturk =async{ retrofitHaberturk.getLatestNewsHaberturk()}

                val rssXmls = awaitAll(response, responseHurriyet, responseMilliyet, responseTurkiyeGaz, responseSozcu, responseHaberturk)

                val news = parseRss(rssXmls)

                withContext(Dispatchers.Main){
                    newsItems.value = news
                }

            } catch (e: Exception) {
                Log.w("NewsViewModel", "Error fetching news: ${e.message}")
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