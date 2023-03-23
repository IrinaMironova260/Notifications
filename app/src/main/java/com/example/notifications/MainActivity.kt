package com.example.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.example.notifications.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private var binding: ActivityMainBinding? = null
    private val channelId = "com.example.notifications.channel1"
    private var notificationManager: NotificationManager? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager //Теперь, из функции onCreate, давайте вызовем функцию display, передав идентификатор, имя и описание.
        createNotificationChannel(channelId,"DemoChannel","this is a demo") //Мы создаем канал уведомлений, создавая экземпляр NotificationChannel, а затем передавая этот экземпляр к createNotificationChannel () класса NotificationManager. Давайте создадим новую функцию. частная забава createNotificationChannel Эта функция должна иметь 3 параметра. Идентификатор, имя и описание канала. Версия SDK должна быть android oreo или выше. Давайте напишем подтверждение для этого
        binding?.butOne?.setOnClickListener(this)
        binding?.textView?.setOnClickListener(this)

    }

    override fun onClick(view: View) {
            when(view.id){
                R.id.butOne -> {
                    displayNotification()
                }
                R.id.textView ->{
                   val intent = Intent(this, Info::class.java)
                   startActivity(intent)
                }
            }
    }


    private fun displayNotification(){
        val notificationId = 7

        val startInfoNotification = Intent(this, Info::class.java)

        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            this,
            0,
            startInfoNotification,
            PendingIntent.FLAG_UPDATE_CURRENT
        )


        val startDetails = Intent(this, Details::class.java)
        val pendingDetails: PendingIntent = PendingIntent.getActivity(
            this,
            0,
            startDetails,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val actionsDetails: NotificationCompat.Action =
            NotificationCompat.Action.Builder(0,"Детали", pendingDetails).build()


        val startPrice = Intent(this, Price::class.java)
        val pendingPrice: PendingIntent = PendingIntent.getActivity(
            this,
            0,
            startPrice,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val actionsPrice: NotificationCompat.Action =
            NotificationCompat.Action.Builder(0,"Прайс", pendingPrice).build()


        val notification = NotificationCompat.Builder(this@MainActivity,channelId) //мы будем использовать NotificationCompat для создания объекта notification
            .setContentTitle("Новый Заказ")
            .setContentText("Поступил новый заказ!")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setAutoCancel(true)//автоотмена
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)//самый высокий приоритет
            .setContentIntent(pendingIntent)
            .addAction(actionsDetails)
            .addAction(actionsPrice)
            .build()
        notificationManager?.notify(notificationId,notification)//создание

    }

    private fun createNotificationChannel(id : String, name:String, channelDescription:String) {  //Вы можете видеть, что существуют различные уровни важности. Этот параметр определяет способ прерывания пользователя для любого уведомления, принадлежащего этому каналу. Давайте установим это как можно выше.
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(id,name,importance).apply {//нужно создать канал уведомлений, прежде чем публиковать какие-либо уведомления
                description = channelDescription
            }
            notificationManager?.createNotificationChannel(channel)
        }
    }
}