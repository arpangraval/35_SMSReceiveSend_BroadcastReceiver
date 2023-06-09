package com.example.sms_broadcast

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Telephony
import android.telephony.SmsManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    if(ActivityCompat.checkSelfPermission(this,Manifest.permission.RECEIVE_SMS)!=PackageManager.PERMISSION_GRANTED)
    {
        ActivityCompat.requestPermissions(this, arrayOf
            (Manifest.permission.RECEIVE_SMS,Manifest.permission.SEND_SMS),111)
    }
        else
            receiveMsg()

        button.setOnClickListener {
        var sms:SmsManager = SmsManager.getDefault()
            sms.sendTextMessage(editTextPhone.text.toString(),"ME",editTextTextMultiLine.text.toString(),null,null )
        editTextPhone.setText("")
            editTextTextMultiLine.setText("")
            editTextPhone.requestFocus()

        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==111 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            receiveMsg()
      }

    private fun receiveMsg() {
        var br = object:BroadcastReceiver(){
            override fun onReceive(p0: Context?, p1: Intent?) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
                    for(sms in Telephony.Sms.Intents.getMessagesFromIntent(p1)){
                    editTextPhone.setText(sms.originatingAddress)
                        editTextTextMultiLine.setText(sms.displayMessageBody)
                    //Toast.makeText(applicationContext,sms.displayMessageBody,Toast.LENGTH_SHORT).show()

                    }
                }
            }

        }

    registerReceiver(br, IntentFilter("android.provider.Telephony.SMS_RECEIVED"))
    }

}