package com.baharudin.mailingapp.presentation.main.letterout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.baharudin.mailingapp.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LetterOutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_letter_out)
    }
}