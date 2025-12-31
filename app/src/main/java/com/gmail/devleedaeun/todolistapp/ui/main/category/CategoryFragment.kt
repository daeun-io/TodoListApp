package com.gmail.devleedaeun.todolistapp.ui.main.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.gmail.devleedaeun.todolistapp.CategoryViewModel
import com.gmail.devleedaeun.todolistapp.data.entities.Category
import com.gmail.devleedaeun.todolistapp.databinding.FragmentCategoryBinding
import kotlinx.coroutines.launch

class CategoryFragment(): Fragment() {
    lateinit var binding: FragmentCategoryBinding
    private var categoryList = emptyList<Category>()
    private val categoryViewModel = CategoryViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCategoryBinding.inflate(inflater, container, false)
        categoryList = categoryViewModel.categoryList.value ?: emptyList()

        val categoryAdapter = CategoryRVAdapter(requireContext(), categoryList)
        binding.categoryListRv.adapter = categoryAdapter
        binding.categoryListRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        categoryAdapter.setMyOnclickListener(object: CategoryRVAdapter.MyOnClickListener{
            override fun editCategory(position: Int) {
                categoryAdapter.editCategory(position)
            }
        })
        observeCategoryData(categoryAdapter)
        return binding.root
    }

    private fun observeCategoryData(adapter: CategoryRVAdapter){
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                categoryViewModel.getAllCategories()
                categoryViewModel.categoryList.collect{
                    adapter.loadCategory(it ?: emptyList())
                }
            }
        }
    }
}