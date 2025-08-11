package com.maherlabbad.hayattakal.service

import android.Manifest
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.media.AudioManager
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.telephony.SmsManager
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.maherlabbad.hayattakal.db.EarthquakeDatabase
import com.maherlabbad.hayattakal.db.Model_Database
import com.maherlabbad.hayattakal.model.EarthquakeModel
import com.maherlabbad.hayattakal.repository.EarthquakeRepository
import com.maherlabbad.hayattakal.repository.RelativeRepository
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class QuakeService : Service() {

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private lateinit var earthquakeRepository: EarthquakeRepository

    private lateinit var db : EarthquakeDatabase

    private lateinit var relativeRepository: RelativeRepository

    private lateinit var db_relative : Model_Database

    override fun onCreate() {

        super.onCreate()
        db = EarthquakeDatabase.getDatabase(applicationContext)
        db_relative = Model_Database.getDatabase(applicationContext)
        earthquakeRepository = EarthquakeRepository(db.earthquakeDao())
        relativeRepository = RelativeRepository(db_relative.userDao(), context = applicationContext)


        NotificationHelper.createChannels(this)
        startForeground(
            NotificationHelper.SERVICE_NOTIF_ID,
            NotificationHelper.createServiceNotification(this, "Veriler kontrol ediliyor...")
        )
        startChecking()
    }

    private fun startChecking() {
        scope.launch {
            while (isActive) {
                try {
                    checkEarthquakes()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                delay(60_000)
            }
        }
    }

    private suspend fun checkEarthquakes() {
        val earthquakes = earthquakeRepository.getAllEarthquakes()
        earthquakes.forEach { quake ->

            val inserted = earthquakeRepository.insertEarthquake(quake)

            if (inserted && quake.magnitude.toDouble() >= 4.5 && !quake.check) {
                quake.check = true
                sendNotification(quake)
                if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                    val location = relativeRepository.getLocation()
                    val results = FloatArray(1)
                    Location.distanceBetween(
                        location?.latitude!!, location.longitude,
                        quake.latitude.toDouble(), quake.longitude.toDouble(),
                        results
                    )
                    val distanceInKm = results[0] / 1000

                    if (distanceInKm <= 100) {
                        sendSmsToContacts(quake)
                    }
                }
            }
        }

        val cutoff = getCutoffDateString()
        earthquakeRepository.deleteOldData(cutoff)

    }

    fun getCutoffDateString(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val cutoffTime = System.currentTimeMillis() - 24 * 60 * 60 * 1000
        return sdf.format(Date(cutoffTime))
    }

    private fun sendNotification(quake: EarthquakeModel) {

        if(ContextCompat.checkSelfPermission(this,Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED){
            val notif = NotificationHelper.createAlertNotification(
                this,
                "Deprem Uyarısı",
                "${quake.location} - M${quake.magnitude}"
            )

            val nm = NotificationManagerCompat.from(this)
            nm.notify(quake.eventId.hashCode(), notif)
        }
    }
    fun playAlarmSound(context: Context) {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        audioManager.setStreamVolume(
            AudioManager.STREAM_ALARM,
            audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM),
            0
        )
        val alarmUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
            ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val ringtone: Ringtone = RingtoneManager.getRingtone(context, alarmUri)
        ringtone.play()
        Handler(Looper.getMainLooper()).postDelayed({
            ringtone.stop()
        }, 5000)
    }

    private fun sendSmsToContacts(quake: EarthquakeModel) {

        playAlarmSound(context = this)

        val permissionGranted = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.SEND_SMS
        ) == PackageManager.PERMISSION_GRANTED

        if (!permissionGranted) {
            Log.e("SMS_TEST", "SEND_SMS izni yok! SMS gönderilmedi.")
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val phones = relativeRepository.getAllRelativeModels()

                if (phones.isEmpty()) {
                    Log.w("SMS_TEST", "Telefon listesi boş. SMS gönderilmeyecek.")
                    return@launch
                }

                val latlng = relativeRepository.getLocation()
                if (latlng == null) {
                    Log.w("SMS_TEST", "Konum bilgisi yok. SMS gönderilmeyecek.")
                    return@launch
                }

                val message =
                    "Tehlikedeyim! Son deprem: ${quake.location} - M${quake.magnitude}. Konumum: https://maps.google.com/?q=${latlng.latitude},${latlng.longitude}"

                phones.forEach { phone ->
                    SmsManager.getDefault().sendTextMessage(phone, null, message, null, null)
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}