package com.baharudin.mailingapp.presentation.main.letterin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.baharudin.mailingapp.R
import com.baharudin.mailingapp.databinding.ActivityLetterInBinding
import com.baharudin.mailingapp.domain.letter.entity.LetterEntity
import com.baharudin.mailingapp.presentation.common.extention.gone
import com.baharudin.mailingapp.presentation.common.extention.showToast
import com.baharudin.mailingapp.presentation.common.extention.visible
import com.baharudin.mailingapp.presentation.main.create.AddLetterActivity
import com.baharudin.mailingapp.presentation.main.detail.DetailActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class LetterInActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLetterInBinding
    private val viewModel : LetterInViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLetterInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        goToCreatePage()
        setupRecycleview()
        fetchLetterIn()
        initObserver()
    }
    private fun goToCreatePage(){
        binding.createFab.setOnClickListener {
            val intent = Intent(this, AddLetterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupRecycleview(){
        val letterAdapter = LetterInAdapter(mutableListOf())
        letterAdapter.setItemTapListener(object : LetterInAdapter.OnItemTap{
            override fun onTap(product: LetterEntity) {
                val intent = Intent(this@LetterInActivity, DetailActivity::class.java)
                    .putExtra(DetailActivity.LETTER, product)
                startActivity(intent)
            }
        })
        binding.productsRecyclerView.apply {
            adapter = letterAdapter
            layoutManager = LinearLayoutManager(this@LetterInActivity)
        }
    }

    private fun fetchLetterIn(){
        viewModel.fetchLetterIn()
    }

    private fun initObserver(){
        observeState()
        observeLetterIn()
    }

    private fun observeState(){
        viewModel.mState
            .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach { state ->
                handleState(state)
            }
            .launchIn(lifecycleScope)
    }
    private fun observeLetterIn(){
        viewModel.mLetter
            .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach { letter ->
                handelLetter(letter)
            }
            .launchIn(lifecycleScope)
    }
    private fun handelLetter(letter: List<LetterEntity>){
        binding.productsRecyclerView.adapter?.let {
            if (it is LetterInAdapter){
                it.updateList(letter)
            }
        }
    }

    private fun handleState(state : LetterInViewState){
        when(state){
            is LetterInViewState.IsLoading -> handleLoading(state.isLoading)
            is LetterInViewState.ShowToast -> this.showToast(state.message)
            is LetterInViewState.Init -> Unit
        }
    }

    private fun handleLoading(isLoading: Boolean){
        if(isLoading){
            binding.loadingProgressBar.visible()
        }else{
            binding.loadingProgressBar.gone()
        }
    }
}