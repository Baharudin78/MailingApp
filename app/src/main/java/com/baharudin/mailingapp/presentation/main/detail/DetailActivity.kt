package com.baharudin.mailingapp.presentation.main.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.baharudin.mailingapp.R
import com.baharudin.mailingapp.databinding.ActivityDetailBinding
import com.baharudin.mailingapp.domain.letter.entity.LetterEntity
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val letterResult = intent.getParcelableExtra<LetterEntity>(LETTER)

        binding.apply {
            tvJenisSurat.text = letterResult?.letterKinds
            tvDeskripsi.text = letterResult?.letterDiscription
            tvNumber.text = letterResult?.letterNumber.toString()
            tvSender.text = letterResult?.senderIdentity
            tvTanggal.text = letterResult?.letterDate
            tvTempat.text = letterResult?.letterDestination
            Glide.with(this@DetailActivity)
                .load(letterResult?.imagesLetterUrl)
                .into(ivFoto)
        }


    }
    companion object{
        const val LETTER = "LETTER"
    }
}