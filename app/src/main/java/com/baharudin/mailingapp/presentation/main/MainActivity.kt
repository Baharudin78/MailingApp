package com.baharudin.mailingapp.presentation.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.baharudin.mailingapp.R
import com.baharudin.mailingapp.core.SharedPrefs
import com.baharudin.mailingapp.databinding.ActivityMainBinding
import com.baharudin.mailingapp.domain.login.entity.LoginEntity
import com.baharudin.mailingapp.presentation.login.LoginActivity
import com.baharudin.mailingapp.presentation.main.profile.ProfileFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    lateinit var navigationController : NavController
    @Inject
    lateinit var sharedPrefs: SharedPrefs
    var loginEntity: LoginEntity? = null
    private lateinit var fragments: ArrayList<Fragment>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navigation = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navigationController = navigation.findNavController()
        initPaging()
        getIntentData()
        binding.apply {
            bottomNavigationView.setupWithNavController(navigationController)
            navigation.findNavController().addOnDestinationChangedListener{ _, destination, _ ->
                when (destination.id) {
                    R.id.homeFragment2->
                        bottomNavigationView.visibility = View.VISIBLE
                    else -> bottomNavigationView.visibility = View.GONE
                }
            }
        }
    }

    private fun getIntentData() {
        intent.getParcelableExtra<LoginEntity>(EXTRA_DATA)?.let {
            loginEntity = it
        }
    }

    override fun onStart() {
        super.onStart()
        checkIsLoggedIn()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    private fun checkIsLoggedIn(){
        if (sharedPrefs.getToken().isEmpty()){
            goToLoginActivity()
        }
    }

    private fun goToLoginActivity(){
        startActivity(Intent(this@MainActivity, LoginActivity::class.java))
        finish()
    }

    private fun signOut(){
        sharedPrefs.clear()
        goToLoginActivity()
    }
    override fun onSupportNavigateUp(): Boolean {
        return navigationController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun initPaging() {
        loginEntity?.let {
            fragments = arrayListOf(
                ProfileFragment.newInstance(it),
            )
        }
    }

    companion object{
        const val EXTRA_DATA = "EXTRA_DATA"
    }
}