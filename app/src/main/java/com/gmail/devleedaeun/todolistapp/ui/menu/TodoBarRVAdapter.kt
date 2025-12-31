package com.gmail.devleedaeun.todolistapp.ui.menu

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gmail.devleedaeun.todolistapp.databinding.ItemTodoBarBinding
import com.gmail.devleedaeun.todolistapp.ui.main.MainActivity

class TodoBarRVAdapter(
    private val context: Context,
    private var nameList: List<String>
) : RecyclerView.Adapter<TodoBarRVAdapter.ViewHolder>() {

    // Set onClickListener
    lateinit var mItemClickListener: MyItemClickListener
    interface MyItemClickListener{
        fun saveSelectedName(position: Int)
    }
    fun setMyItemClickListener(myItemClickListener: MyItemClickListener){
        mItemClickListener = myItemClickListener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemTodoBarBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        holder.bind(nameList[position])
        holder.binding.root.setOnClickListener {
            saveSelectedName(position)
            context.startActivity(Intent(context, MainActivity::class.java))
        }
    }

    override fun getItemCount(): Int = nameList.size

    fun saveSelectedName(position: Int) {
        val spf = context.getSharedPreferences("todo_bar", Context.MODE_PRIVATE)
        spf.edit().putString("selected", nameList[position]).apply()
    }

    fun loadCategory(newList: List<String>){
        nameList = newList
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemTodoBarBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(name: String){
            binding.todoBarTv.text = name
        }
    }
}