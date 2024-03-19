package com.example.tasksmanager.presentation.viewModels

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasksmanager.MainActivity
import com.example.tasksmanager.R
import com.example.tasksmanager.data.database.TaskDatabaseDao
import com.example.tasksmanager.domain.entities.Task
import kotlinx.coroutines.launch
import java.util.Date

class NewTaskViewModel(
    private val dao: TaskDatabaseDao,
) : ViewModel(){

    private val _taskAddedEvent = MutableLiveData<Unit>()
    val taskAddedEvent: LiveData<Unit> = _taskAddedEvent

    private val _name = MutableLiveData<String>()
    val name: LiveData<String> = _name

    private val _description = MutableLiveData<String>()
    val description: LiveData<String> = _description

    private val _category = MutableLiveData<String>()
    val category: LiveData<String> = _category

    private val _date = MutableLiveData<Date>()
    val date: LiveData<Date> = _date

    fun onChange(__name:String, __description:String, __category:String, __date:Date ){
        _name.value = __name
        _description.value = __description
        _category.value = __category
        _date.value = __date
    }

    fun saveTask(context: Context,task: Task) = viewModelScope.launch {
        dao.addTask(task)
        _taskAddedEvent.value = Unit
        sendNotification(context)
    }
    fun sendNotification(context: Context){


        val notification = NotificationCompat.Builder(context, MainActivity.CHANNEL_ID)
            .setContentTitle("Se ha creado la tarea")
            .setContentText("Se ha creado")
            .setSmallIcon(R.drawable.tasknotification)
            .setAutoCancel(true)
            .build()

        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            notify(1, notification)
        }
    }
}