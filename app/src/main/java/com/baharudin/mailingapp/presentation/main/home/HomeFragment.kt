package com.baharudin.mailingapp.presentation.main.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.baharudin.mailingapp.R
import com.baharudin.mailingapp.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentHomeBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)


    }
}