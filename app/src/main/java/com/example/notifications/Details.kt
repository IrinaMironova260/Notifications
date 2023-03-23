package com.example.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.app.NotificationCompat
import androidx.core.app.RemoteInput
import com.example.notifications.databinding.DetailsBinding

class Details : AppCompatActivity(), View.OnClickListener  {
    private var binding: DetailsBinding? = null

    private val channelId = "com.example.notifications.channel1"
    private var notificationManager: NotificationManager? = null
    private val KEY_REPLY = "key_reply"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DetailsBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel(channelId,"DemoChannel","this is a demo")

        binding?.order?.setOnClickListener(this)
        binding?.textViewDetails?.setOnClickListener(this)

    }

    override fun onClick(view: View) {
        when(view.id){
            R.id.order ->{
                val intent = Intent(this, Info::class.java)
                startActivity(intent)
            }
            R.id.textViewDetails -> {
                displayOrderNotification()
            }
        }
    }
    private fun displayOrderNotification(){
        val notificationId = 8
        val startInfoNotification = Intent(this, Info::class.java)

        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            this,
            0,
            startInfoNotification,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val remoteInput: RemoteInput = RemoteInput.Builder(KEY_REPLY).run{
            setLabel("Вы согласны сделать заказ?")
            build()
        }
        val replyAction : NotificationCompat.Action = NotificationCompat.Action.Builder(
            0,
            "Ответить",
            pendingIntent)
            .addRemoteInput(remoteInput)
            .build()

        val startDetails = Intent(this, Info::class.java)
        val pendingDetails: PendingIntent = PendingIntent.getActivity(
            this,
            0,
            startDetails,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val actionDetails : NotificationCompat.Action =
            NotificationCompat.Action.Builder(0,"Информация",pendingDetails).build()



        val startPrice = Intent(this, Price::class.java)
        val pendingPrice: PendingIntent = PendingIntent.getActivity(
            this,
            0,
            startPrice,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val actionPrice : NotificationCompat.Action =
            NotificationCompat.Action.Builder(0,"Стоимость",pendingPrice).build()


        val notification = NotificationCompat.Builder(this@Details,channelId)
            .setContentTitle("Получить информацию")
            .setContentText("Узнать стоимость")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setAutoCancel(true)//автоотмена
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .addAction(actionDetails)
            .addAction(actionPrice)
            .build()
        notificationManager?.notify(notificationId,notification)//создание
    }

    private fun createNotificationChannel(id : String, name:String, channelDescription:String){
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(id,name,importance).apply {
                description = channelDescription
            }
            notificationManager?.createNotificationChannel(channel)
        }
    }
}