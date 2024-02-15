package com.example.androidessncial.service

import android.annotation.SuppressLint
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.IBinder
import android.provider.Telephony
import android.telephony.SmsManager
import androidx.annotation.RequiresApi
import com.example.androidessncial.database.DatabaseHelper
import com.example.androidessncial.notifications.SmsServiceLocationBack

class SmsReceiverTeste : BroadcastReceiver() {
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context, intent: Intent) {
        fun deleteLocationDataBase(){
            val dbHelper = DatabaseHelper(context) // Substitua 'context' pelo contexto do seu aplicativo
            dbHelper.deleteAllItems()
        }

        fun startLocation() {
            Intent(context, SmsServiceLocationBack::class.java).also {
                it.action = SmsServiceLocationBack.Actions.START.toString()
                context.startForegroundService(it)
                //context.startService(it)
            }
        }

        fun stopLocation() {
            Intent(context, SmsServiceLocationBack::class.java).also {
                it.action = SmsServiceLocationBack.Actions.STOP.toString()
                context.startForegroundService(it)
                //context.startService(it)
            }
        }

        fun senderPostion(address: String, smsBody: String) {
            val getDateLocation = DatabaseHelper(context)
            val getData = getDateLocation.getAllItems()
            val get = getData.last()

            val date = get.datedmy
            val time = get.datetime
            val latitude = get.latitude
            val longitude = get.longitude

            val googleMapsUrl =
                "http://maps.google.com/maps?q=$latitude,$longitude"
            val locationInfo =
                "Data:$date\nHora:$time"

            //val testAddress = "244944790744"
            @Suppress("DEPRECATION") val smsManager = SmsManager.getDefault()

            smsManager.sendTextMessage(
                address,
                null,
                "$locationInfo\n$googleMapsUrl",
                null,
                null
            )
        }

        val messages = Telephony.Sms.Intents.getMessagesFromIntent(intent)

        for (sms in messages) {
            val address = sms.displayOriginatingAddress
            val smsBody = sms.displayMessageBody

            if (smsBody == "liga...") {
                startLocation()
            }

            if (smsBody == "tudo bem...") {
                stopLocation()
            }

            if (smsBody == "fatorylocation...") {
                deleteLocationDataBase()
            }

            if (smsBody == "liga só...") {
                senderPostion(address, smsBody)
            }
        }
    }
}



class SmsService : Service() {


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {


        val intentFilter = IntentFilter("android.provider.Telephony.SMS_RECEIVED")
        val receiver = SmsReceiverTeste()
        registerReceiver(receiver, intentFilter)
        return START_STICKY
    }
}