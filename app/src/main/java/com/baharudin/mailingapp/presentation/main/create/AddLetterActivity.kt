package com.baharudin.mailingapp.presentation.main.create

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.baharudin.mailingapp.R
import com.baharudin.mailingapp.databinding.ActivityAddLetterBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AddLetterActivity : AppCompatActivity() {
    private lateinit var binding : ActivityAddLetterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddLetterBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    companion object{
        const val LETTER = "LETTER"
    }
}