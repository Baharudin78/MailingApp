package com.baharudin.mailingapp.presentation.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.baharudin.mailingapp.R
import com.baharudin.mailingapp.core.SharedPrefs
import com.baharudin.mailingapp.databinding.ActivityMainBinding
import com.baharudin.mailingapp.domain.login.entity.LoginEntity
import com.baharudin.mailingapp.presentation.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    lateinit var navigationController : NavController
    @Inject
    lateinit var sharedPrefs: SharedPrefs
    var loginEntity: LoginEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navigation = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navigationController = navigation.findNavController()
        getIntentData()
        binding.apply {
            bottomNavigationView.setupWithNavController(navigationController)
            navigation.findNavController().addOnDestinationChangedListener{ _, destination, _ ->
                when (destination.id) {
                    R.id.homeFragment2, R.id.profileFragment ->
                        bottomNavigationView.visibility = View.VISIBLE
                    else -> bottomNavigationView.visibility = View.GONE
                }
            }
        }
    }

    private fun getIntentData() {
        intent.getParcelableExtra<LoginEntity>(LOGIN_ID)?.let {
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

    companion object{
        const val LOGIN_ID = "LOGIN_ID"
    }
}