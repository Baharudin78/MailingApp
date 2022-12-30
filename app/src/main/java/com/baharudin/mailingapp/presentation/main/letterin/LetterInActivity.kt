package com.baharudin.mailingapp.presentation.main.letterin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.baharudin.mailingapp.R
import com.baharudin.mailingapp.databinding.ActivityLetterInBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LetterInActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLetterInBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLetterInBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}