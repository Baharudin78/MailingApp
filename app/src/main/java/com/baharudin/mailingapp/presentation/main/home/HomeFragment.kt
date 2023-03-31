package com.baharudin.mailingapp.presentation.main.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.baharudin.mailingapp.R
import com.baharudin.mailingapp.databinding.FragmentHomeBinding
import com.baharudin.mailingapp.presentation.main.create.AddLetterActivity
import com.baharudin.mailingapp.presentation.main.letterin.LetterInActivity
import com.baharudin.mailingapp.presentation.main.letterout.LetterOutActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentHomeBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            cardMailIn.setOnClickListener {
                val intent = Intent(requireContext(), LetterInActivity::class.java)
                startActivity(intent)
            }
            cardMailOut.setOnClickListener {
                val intent = Intent(requireContext(), LetterOutActivity::class.java)
                startActivity(intent)
            }
            createFab.setOnClickListener {
                val intent = Intent(requireContext(), AddLetterActivity::class.java)
                startActivity(intent)
            }
        }
    }
}