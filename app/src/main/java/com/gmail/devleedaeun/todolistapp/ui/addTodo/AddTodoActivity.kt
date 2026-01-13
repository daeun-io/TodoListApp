package com.gmail.devleedaeun.todolistapp.ui.addTodo

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.gmail.devleedaeun.todolistapp.AlarmReceiver
import com.gmail.devleedaeun.todolistapp.CategoryViewModel
import com.gmail.devleedaeun.todolistapp.R
import com.gmail.devleedaeun.todolistapp.TodoViewModel
import com.gmail.devleedaeun.todolistapp.data.entities.Todo
import com.gmail.devleedaeun.todolistapp.databinding.ActivityAddTodoBinding
import kotlinx.coroutines.launch
import java.sql.Time
import java.util.Calendar

class AddTodoActivity() : AppCompatActivity() {

    private lateinit var binding: ActivityAddTodoBinding
    private val todoViewModel = TodoViewModel()
    private val categoryViewModel = CategoryViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddTodoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val spf: SharedPreferences? = getSharedPreferences("login", MODE_PRIVATE)
        val user = spf?.getLong("user", 0) ?: 0

        binding.addTodoCancelBtn.setOnClickListener { onBackPressed() }

        // Set whether Todo is important
        var isImportant = false
        binding.importantIb.setOnClickListener {
            if(!isImportant){
                binding.importantIb.setImageResource(R.drawable.baseline_star_24)
                isImportant = true
            }
            else{
                binding.importantIb.setImageResource(R.drawable.outline_star_24)
                isImportant = false
            }
        }

        // Set Time for alarm notification
        var isTimeSelected = false
        binding.addTodoTimeTv.setOnClickListener {
            if(!isTimeSelected){
                binding.addTodoTimeLl.visibility = View.VISIBLE
                binding.addTodoTp.visibility = View.VISIBLE
                binding.addTodoDateTv.setOnClickListener {
                    binding.addTodoDateTv.setTypeface(null, android.graphics.Typeface.BOLD)
                    binding.addTodoSelectTimeTv.setTypeface(null, android.graphics.Typeface.NORMAL)
                    binding.addTodoSelectTimeTv.text = "${binding.addTodoTp.hour}:${binding.addTodoTp.minute}"
                    binding.addTodoDp.visibility = View.VISIBLE
                    binding.addTodoTp.visibility = View.GONE
                }
                binding.addTodoSelectTimeTv.setOnClickListener {
                    binding.addTodoDateTv.setTypeface(null, android.graphics.Typeface.NORMAL)
                    binding.addTodoSelectTimeTv.setTypeface(null, android.graphics.Typeface.BOLD)
                    binding.addTodoDateTv.text = "${binding.addTodoDp.year}.${binding.addTodoDp.month + 1}.${binding.addTodoDp.dayOfMonth}"
                    binding.addTodoDp.visibility = View.GONE
                    binding.addTodoTp.visibility = View.VISIBLE
                }
                isTimeSelected = true
            }
            else{
                binding.addTodoSelectTimeTv.text = "${binding.addTodoTp.hour}:${binding.addTodoTp.minute}"
                binding.addTodoDateTv.text = "${binding.addTodoDp.year}.${binding.addTodoDp.month + 1}.${binding.addTodoDp.dayOfMonth}"
                binding.addTodoTimeTv.text = binding.addTodoDateTv.text.toString() + " " + binding.addTodoSelectTimeTv.text.toString()
                binding.addTodoTimeLl.visibility = View.GONE
                binding.addTodoTp.visibility = View.GONE
                binding.addTodoDp.visibility = View.GONE
                isTimeSelected = false
            }
        }

        // TODO: think proper comment
        val storageArray = when(user){
            0L -> {
                arrayOf("My Android")
            }
            else -> {
                resources.getStringArray(R.array.storage)
            }
        }
        val storageArrayAdapter = ArrayAdapter(this, R.layout.item_dropdown, storageArray)
        binding.addTodoStorageSpinner.setAdapter(storageArrayAdapter)
        getCategoryNames()

        // Update Delete
        if(intent.hasExtra("id")){
            binding.addTodoDeleteBtn.visibility = View.VISIBLE

            // Load Todo data
            val title = intent.getStringExtra("title")
            val time = intent.getStringExtra("time")
            binding.addTodoTitleEt.setText(title)
            binding.addTodoTimeTv.text = time
            if(intent.getBooleanExtra("isImportant", false)){
                binding.importantIb.setImageResource(R.drawable.baseline_star_24)
            }
            else{
                binding.importantIb.setImageResource(R.drawable.outline_star_24)
            }
            val storageArray = resources.getStringArray(R.array.storage)
            val storagePos = storageArray.indexOf(intent.getStringExtra("storage"))
            binding.addTodoStorageSpinner.setSelection(storagePos)

            binding.addTodoDeleteBtn.setOnClickListener {
                when(intent.getStringExtra("storage")){
                    "My Android" -> todoViewModel.deleteTodo(Todo(id = intent.getLongExtra("id", 0),))
                    "Firebase" -> todoViewModel.deleteTodoAtFirebase(user, intent.getLongExtra("id", 0))
                }
                navigateUpTo(intent)
            }

            if(intent.getBooleanExtra("isCompleted", false)){
                binding.addTodoSaveBtn.text = "Restore"
            }
            binding.addTodoSaveBtn.setOnClickListener {
                // Save the time for alarm
                val calendar = Calendar.getInstance().apply{
                    set(Calendar.YEAR, binding.addTodoDp.year)
                    set(Calendar.MONTH, binding.addTodoDp.month)
                    set(Calendar.DAY_OF_MONTH, binding.addTodoDp.dayOfMonth)
                    set(Calendar.HOUR_OF_DAY, binding.addTodoTp.hour)
                    set(Calendar.MINUTE, binding.addTodoTp.minute)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }

                // Update Todo depending on a storage change
                val pastStorage = intent.getStringExtra("storage")
                val currentStorage = binding.addTodoStorageSpinner.selectedItem.toString()
                val id = intent.getLongExtra("id", 0)
                if(pastStorage == currentStorage){
                    when(pastStorage){
                        "My Android" -> {
                            todoViewModel.updateTodo(
                                Todo(
                                    binding.addTodoTitleEt.text.toString(),
                                    binding.addTodoDateTv.text.toString() + " " + binding.addTodoSelectTimeTv.text.toString(),
                                    binding.addTodoCategorySpinner.selectedItem.toString(),
                                    binding.addTodoStorageSpinner.selectedItem.toString(),
                                    isImportant,
                                    false,
                                    id = id
                                )
                            )
                        }
                        "Firebase" -> {
                            todoViewModel.updateTodoAtFirebase(
                                Todo(
                                    binding.addTodoTitleEt.text.toString(),
                                    binding.addTodoDateTv.text.toString() + " " + binding.addTodoSelectTimeTv.text.toString(),
                                    binding.addTodoCategorySpinner.selectedItem.toString(),
                                    binding.addTodoStorageSpinner.selectedItem.toString(),
                                    isImportant,
                                    false,
                                    id = id
                                ),
                                user,
                                id
                            )
                        }
                    }
                }
                else{
                    when(pastStorage){
                        "My Android" -> {
                            todoViewModel.deleteTodo(Todo(id = id))
                            todoViewModel.addTodoAtFirebase(
                                Todo(
                                    binding.addTodoTitleEt.text.toString(),
                                    binding.addTodoDateTv.text.toString() + " " + binding.addTodoSelectTimeTv.text.toString(),
                                    binding.addTodoCategorySpinner.selectedItem.toString(),
                                    binding.addTodoStorageSpinner.selectedItem.toString(),
                                    isImportant,
                                    isCompleted = false,
                                    id = id),
                                user,
                                id
                            )
                        }
                        "Firebase" -> {
                            todoViewModel.deleteTodoAtFirebase(user, id)
                            todoViewModel.addTodo(Todo(
                                binding.addTodoTitleEt.text.toString(),
                                binding.addTodoDateTv.text.toString() + " " + binding.addTodoSelectTimeTv.text.toString(),
                                binding.addTodoCategorySpinner.selectedItem.toString(),
                                binding.addTodoStorageSpinner.selectedItem.toString(),
                                isImportant,
                                isCompleted = false,
                                id)
                            )
                        }
                    }
                }
                // When the time is not before than now set an alarm
                if(!calendar.before(Calendar.getInstance())){
                    scheduleAlarm(calendar.timeInMillis, binding.addTodoTitleEt.text.toString(), id)
                }
                navigateUpTo(intent)
            }
        }
        // Add
        else{
            binding.addTodoDeleteBtn.visibility = View.GONE
            binding.addTodoSaveBtn.setOnClickListener {

                // Save the time for alarm
                val calendar = Calendar.getInstance().apply{
                    set(Calendar.YEAR, binding.addTodoDp.year)
                    set(Calendar.MONTH, binding.addTodoDp.month)
                    set(Calendar.DAY_OF_MONTH, binding.addTodoDp.dayOfMonth)
                    set(Calendar.HOUR_OF_DAY, binding.addTodoTp.hour)
                    set(Calendar.MINUTE, binding.addTodoTp.minute)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }
                val id = System.currentTimeMillis()

                // Add Todo and set alarm
                when(val storage = binding.addTodoStorageSpinner.selectedItem.toString()){
                    "My Android" -> {
                        todoViewModel.addTodo(
                            Todo(
                                binding.addTodoTitleEt.text.toString(),
                                binding.addTodoDateTv.text.toString() + " " + binding.addTodoSelectTimeTv.text.toString(),
                                binding.addTodoCategorySpinner.selectedItem.toString(),
                                storage,
                                isImportant,
                                false,
                                intent.getLongExtra("userId", 0),
                                id
                            )
                        )
                    }
                    "Firebase" -> {
                        val id = System.currentTimeMillis()
                        todoViewModel.addTodoAtFirebase(
                            Todo(
                                binding.addTodoTitleEt.text.toString(),
                                binding.addTodoDateTv.text.toString() + " " + binding.addTodoSelectTimeTv.text.toString(),
                                binding.addTodoCategorySpinner.selectedItem.toString(),
                                storage,
                                isImportant,
                                false,
                                user,
                                id = id
                            ),
                            user,
                            id
                        )
                    }
                }
                if(!calendar.before(Calendar.getInstance())){
                    scheduleAlarm(calendar.timeInMillis, binding.addTodoTitleEt.text.toString(), id)
                }
                navigateUpTo(intent)
            }
        }
    }

    private fun getCategoryNames(){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                categoryViewModel.getAllCategories()
                categoryViewModel.nameList.collect {
                    if(it?.isNotEmpty() == true){
                        // Get Category list and attach its adapter in Category spinner
                        val categoryNames = it as ArrayList<String>
                        val categoryArrayAdapter = ArrayAdapter(this@AddTodoActivity, R.layout.item_dropdown, categoryNames)
                        binding.addTodoCategorySpinner.setAdapter(categoryArrayAdapter)

                        // Set Category spinner as saved
                        if(intent.hasExtra("id")){
                            val categoryPos = categoryNames.indexOf(intent.getStringExtra("category"))
                            binding.addTodoCategorySpinner.setSelection(categoryPos)
                        }
                    }
                }
            }
        }
    }

    // Schedule alarm notification
    private fun scheduleAlarm(time: Long, title: String, id:Long){
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        if(alarmManager.canScheduleExactAlarms()){
            // Set intent and save data
            val intent = Intent(this, AlarmReceiver::class.java)
            intent.putExtra("title", title)
            intent.putExtra("id", id)
            // Set pendingIntent for alarm notification
            val pendingIntent = PendingIntent.getBroadcast(this, id.toInt(), intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,time, pendingIntent)
        }
    }
}
