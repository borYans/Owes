package com.example.owes.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.owes.R
import com.example.owes.data.db.DebtorDao
import com.example.owes.utils.DateConverter.convertDateToSimpleFormatString
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.*

@HiltWorker
class NotificationWorker @AssistedInject constructor (
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val debtorDao: DebtorDao
): CoroutineWorker(context, workerParameters) {


    override suspend fun doWork(): Result {

        val todayDate = getTodaysDateAsString()
        val debtors = debtorDao.getAllDebtors()
            for (date in debtors) {
                if (todayDate == date.dueDate) {
                    Log.d("WORK MANAGER", "Dates ---------->: ${date.dueDate} ")
                    createNotificationChannel()
                    showNotifiaction()
                }
            }

        return  Result.success()
    }

    private fun getTodaysDateAsString() = convertDateToSimpleFormatString(Calendar.getInstance().time)

    private fun showNotifiaction() { //here pass name of the debtor and amount of money to pay
        val notification = NotificationCompat.Builder(applicationContext, "channelID")
        notification.apply {
            setSmallIcon(R.drawable.credit_card_24)
                setContentTitle("Deadline for payment")
                setContentText("This person obligated to pay until today's date.")
                priority = NotificationCompat.PRIORITY_DEFAULT
        }
        NotificationManagerCompat.from(applicationContext).also { it.notify(1, notification.build()) } //this id should be unique
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = "channel_1"
            val description = "This is test channel"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("channelID", name, importance)
            channel.description = description
            val notificationManager: NotificationManager? = getSystemService(applicationContext, NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
        }
    }

}