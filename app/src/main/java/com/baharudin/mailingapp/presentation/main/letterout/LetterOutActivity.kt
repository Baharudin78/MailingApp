package com.baharudin.mailingapp.presentation.main.letterout

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.baharudin.mailingapp.R
import com.baharudin.mailingapp.databinding.ActivityLetterOutBinding
import com.baharudin.mailingapp.domain.letter.entity.LetterEntity
import com.baharudin.mailingapp.presentation.common.extention.gone
import com.baharudin.mailingapp.presentation.common.extention.showToast
import com.baharudin.mailingapp.presentation.common.extention.visible
import com.baharudin.mailingapp.presentation.main.create.AddLetterActivity
import com.baharudin.mailingapp.presentation.main.detail.DetailActivity
import com.baharudin.mailingapp.presentation.main.letterin.LetterInAdapter
import com.baharudin.mailingapp.presentation.main.letterin.LetterInViewState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class LetterOutActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLetterOutBinding
    private val viewModel : LetterOutViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLetterOutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecycleview()
        fetchLetterIn()
        initObserver()

        binding.apply {
            swipeLayout.setOnRefreshListener {
                swipeLayout.isRefreshing = true
                viewModel.fetchLetterOut()
                swipeLayout.isRefreshing = false
            }
        }

    }

    private fun setupRecycleview(){
        val letterAdapter = LetterInAdapter(mutableListOf())
        letterAdapter.setItemTapListener(object : LetterInAdapter.OnItemTap{
            override fun onTap(product: LetterEntity) {
                val intent = Intent(this@LetterOutActivity, DetailActivity::class.java)
                    .putExtra(DetailActivity.LETTER, product)
                startActivity(intent)
            }
        })
        binding.productsRecyclerView.apply {
            adapter = letterAdapter
            layoutManager = LinearLayoutManager(this@LetterOutActivity)
        }
    }

    private fun fetchLetterIn(){
        viewModel.fetchLetterOut()
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
        viewModel.letterOut
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

    private fun handleState(state : LetterOutViewState){
        when(state){
            is LetterOutViewState.IsLoading -> handleLoading(state.isLoading)
            is LetterOutViewState.ShowToast -> this.showToast(state.message)
            is LetterOutViewState.Init -> Unit
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