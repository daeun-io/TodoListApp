package com.gmail.devleedaeun.todolistapp.ui.main.todo

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.SystemClock.sleep
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.gmail.devleedaeun.todolistapp.R
import com.gmail.devleedaeun.todolistapp.TodoViewModel
import com.gmail.devleedaeun.todolistapp.data.entities.Todo
import com.gmail.devleedaeun.todolistapp.databinding.ItemTodoBinding
import com.gmail.devleedaeun.todolistapp.ui.addTodo.AddTodoActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TodoRVAdapter(
    private val context: Context?, private var todoList: List<Todo>
): RecyclerView.Adapter<TodoRVAdapter.ViewHolder>() {

    // Set onClickListener
    lateinit var mItemClickListener: MyItemClickListener
    private val todoViewModel = TodoViewModel()
    interface MyItemClickListener{
        fun moveToAddActivity(position: Int)
        fun checkCompleted(holder: ViewHolder, position: Int)
    }
    fun setMyItemClickListener(myItemClickListener: MyItemClickListener){
        mItemClickListener = myItemClickListener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding: ItemTodoBinding = ItemTodoBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        holder.bind(todoList[position])
        holder.binding.itemTodoCheckBoxIv.setOnClickListener {
            checkTodoCompleted(holder, position)
        }
        holder.binding.root.setOnClickListener {
            moveToAddTodoActivity(position)
        }
    }

    override fun getItemCount(): Int {
        return todoList.size
    }

    fun loadTodo(newList: List<Todo>){
        todoList = newList
        notifyDataSetChanged()
    }
    fun moveToAddTodoActivity(position: Int){
        val intent = Intent(context, AddTodoActivity::class.java).apply{
            putExtra("title", todoList[position].title)
            putExtra("time",todoList[position].time)
            putExtra("category", todoList[position].category)
            putExtra("storage", todoList[position].storage)
            putExtra("isImportant", todoList[position].isImportant)
            putExtra("isCompleted", todoList[position].isCompleted)
            putExtra("userId", todoList[position].userId)
            putExtra("id", todoList[position].id)
        }
        context?.startActivity(intent)
    }

    // Update Todo as Completed
    fun checkTodoCompleted(holder: ViewHolder, position: Int){
        val spf = context?.getSharedPreferences("login", MODE_PRIVATE)
        val user = spf?.getLong("user", 0)

        when(todoList[position].storage){
            "My Android" -> {
                todoViewModel.updateTodo(
                    Todo(
                        todoList[position].title,
                        todoList[position].time,
                        todoList[position].category,
                        todoList[position].storage,
                        todoList[position].isImportant, true,
                        todoList[position].userId,
                        todoList[position].id)
                )
            }
            "Firebase" -> {
                todoViewModel.updateTodoAtFirebase(
                    Todo(
                        todoList[position].title,
                        todoList[position].time,
                        todoList[position].category,
                        todoList[position].storage,
                        todoList[position].isImportant,
                        true,
                        todoList[position].userId,
                        todoList[position].id
                    ),
                    user?:0,
                    todoList[position].id
                )
            }
        }
    }
    inner class ViewHolder(val binding: ItemTodoBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(todo: Todo) {
            binding.itemTodoTitleTv.text = todo.title
            if(todo.isCompleted)
                binding.itemTodoCheckBoxIv.setImageResource(R.drawable.baseline_check_box_17)
            else
                binding.itemTodoCheckBoxIv.setImageResource(R.drawable.outline_check_box_outline_blank_17)
        }
    }
}