package com.gmail.devleedaeun.todolistapp.ui.main.category

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gmail.devleedaeun.todolistapp.R
import com.gmail.devleedaeun.todolistapp.data.entities.Category
import com.gmail.devleedaeun.todolistapp.databinding.ItemCategoryBinding

class CategoryRVAdapter(
    private val context: Context,
    private var categoryList: List<Category>,
): RecyclerView.Adapter<CategoryRVAdapter.ViewHolder>() {

    // Set onClickListener
    lateinit var mOnClickListener: MyOnClickListener
    interface MyOnClickListener{
        fun editCategory(position: Int)
    }
    fun setMyOnclickListener(myOnClickListener: MyOnClickListener){
        mOnClickListener = myOnClickListener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding: ItemCategoryBinding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        holder.bind(categoryList[position])
        holder.binding.root.setOnClickListener {
            editCategory(position)
        }
    }

    override fun getItemCount(): Int = categoryList.size

    fun loadCategory(newList: List<Category>){
        categoryList = newList
        notifyDataSetChanged()
    }

    fun editCategory(position: Int){
        val dialog = AddCategoryDialog(context, categoryList[position])
        dialog.show()
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemCategoryBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(category: Category){
            binding.itemCategoryIv.setImageResource(
                when(category.iconColor){
                    R.color.categoryIconRed -> R.drawable.dialog_icon_red_17
                    R.color.categoryIconPink -> R.drawable.dialog_icon_pink_17
                    R.color.categoryIconYellow -> R.drawable.dialog_icon_yellow_17
                    R.color.categoryIconGreen -> R.drawable.dialog_icon_green_17
                    R.color.categoryIconBlue -> R.drawable.dialog_icon_blue_17
                    R.color.black -> R.drawable.dialog_icon_black_17
                    else -> R.drawable.dialog_icon_default_17
                }
            )
            binding.itemCategoryTv.text = category.name
        }
    }
}