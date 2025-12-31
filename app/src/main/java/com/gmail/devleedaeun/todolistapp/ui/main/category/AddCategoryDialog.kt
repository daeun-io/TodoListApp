package com.gmail.devleedaeun.todolistapp.ui.main.category

import android.app.Dialog
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import com.gmail.devleedaeun.todolistapp.CategoryViewModel
import com.gmail.devleedaeun.todolistapp.R
import com.gmail.devleedaeun.todolistapp.data.entities.Category
import com.gmail.devleedaeun.todolistapp.databinding.DialogAddCategoryBinding
import com.gmail.devleedaeun.todolistapp.TodoViewModel

class AddCategoryDialog(context: Context, private val category: Category? = null): Dialog(context) {
    private lateinit var binding: DialogAddCategoryBinding
    private val categoryViewModel = CategoryViewModel()
    private val todoViewModel = TodoViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogAddCategoryBinding.inflate(LayoutInflater.from(context))
        setContentView(binding.root)

        // Set Category color
        var clickedButton = binding.dialogDefaultIb
        var isClicked = false
        binding.dialogRedIb.setOnClickListener {
            clickedButton = binding.dialogRedIb
            isClicked = true
        }
        binding.dialogPinkIb.setOnClickListener {
            clickedButton = binding.dialogPinkIb
            isClicked = true
        }
        binding.dialogYellowIb.setOnClickListener {
            clickedButton = binding.dialogYellowIb
            isClicked = true
        }
        binding.dialogGreenIb.setOnClickListener {
            clickedButton = binding.dialogGreenIb
            isClicked = true
        }
        binding.dialogBlueIb.setOnClickListener {
            clickedButton = binding.dialogBlueIb
            isClicked = true
        }
        binding.dialogBlackIb.setOnClickListener {
            clickedButton = binding.dialogBlackIb
            isClicked = true
        }

        binding.dialogCancelBtn.setOnClickListener { dismiss() }

        // Add, Update, Delete
        if(category != null){
            binding.dialogDeleteBtn.visibility = View.VISIBLE
            binding.dialogCategoryEt.setText(category.name)
            binding.dialogSaveBtn.setOnClickListener {
                category.name = binding.dialogCategoryEt.text.toString()
                categoryViewModel.updateCategory(
                    Category(
                        category.name,
                        returnCategoryColor(clickedButton, isClicked),
                        id = category.id
                    )
                )
                dismiss()
            }
            binding.dialogDeleteBtn.setOnClickListener {
                categoryViewModel.deleteCategory(Category(id= category.id))
                todoViewModel.deleteTodosInName(category.name)
                val spf = context.getSharedPreferences("login", MODE_PRIVATE)
                val user = spf.getLong("user", 0)
                if(user != 0L){
                    todoViewModel.deleteTodosFromFirebaseInName(user, category.name)
                }
                dismiss()
            }
        }else{
            binding.dialogDeleteBtn.visibility = View.GONE
            binding.dialogSaveBtn.setOnClickListener {
                categoryViewModel.addCategory(
                    Category(
                        binding.dialogCategoryEt.text.toString(),
                        returnCategoryColor(clickedButton, true)
                    )
                )
                dismiss()
            }
        }
    }

    private fun returnCategoryColor(clickedButton: ImageButton, isClicked: Boolean): Int{

        val iconColor = if(isClicked) {
            when (clickedButton) {
                binding.dialogRedIb -> R.color.categoryIconRed
                binding.dialogPinkIb -> R.color.categoryIconPink
                binding.dialogYellowIb -> R.color.categoryIconYellow
                binding.dialogGreenIb -> R.color.categoryIconGreen
                binding.dialogBlueIb -> R.color.categoryIconBlue
                binding.dialogBlackIb -> R.color.black
                else -> R.color.categoryIconDefault
            }
        }else
            category!!.iconColor
        return iconColor
    }

}