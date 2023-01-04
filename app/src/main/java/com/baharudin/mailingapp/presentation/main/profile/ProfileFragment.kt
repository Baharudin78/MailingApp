package com.baharudin.mailingapp.presentation.main.profile

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.baharudin.mailingapp.R
import com.baharudin.mailingapp.databinding.FragmentProfileBinding
import com.baharudin.mailingapp.domain.login.entity.LoginEntity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private var _binding : FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private var loginEntity : LoginEntity? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (arguments != null) {
            loginEntity = requireArguments().getParcelable(LOGIN_ID)
            Toast.makeText(requireContext(), loginEntity?.userId.orEmpty(), Toast.LENGTH_SHORT).show()
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentProfileBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)
        Log.d("TAGG", loginEntity?.userId.orEmpty())
        Toast.makeText(requireContext(), loginEntity?.userId.orEmpty(), Toast.LENGTH_SHORT).show()
    }

    companion object{
        const val LOGIN_ID = "LOGIN_ID"
        @JvmStatic
        fun newInstance(userId : LoginEntity) : ProfileFragment{
            return ProfileFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(LOGIN_ID, userId)
                }
            }
        }
    }

}