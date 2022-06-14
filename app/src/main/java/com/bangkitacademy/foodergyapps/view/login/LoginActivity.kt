package com.bangkitacademy.foodergyapps.view.login

import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityOptionsCompat
import com.bangkitacademy.foodergyapps.MainActivity
import com.bangkitacademy.foodergyapps.data.model.UserSession
import com.bangkitacademy.foodergyapps.data.response.LoginItem
import com.bangkitacademy.foodergyapps.databinding.ActivityLoginBinding
import com.bangkitacademy.foodergyapps.utils.DataStoreViewModel
import com.bangkitacademy.foodergyapps.view.register.RegisterActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModelLogin by viewModels<LoginViewModel>()
    private val dataStoreViewModel by viewModels<DataStoreViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupViewModel()
        moveRegister()
        actionLogin()
        //clear()
        binding.clearButton.setOnClickListener {
            dataStoreViewModel.logout()
        }
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }
    private fun setupViewModel() {
        viewModelLogin.isLoading.observe(this) { showLoading(it) }
        //viewModelLogin.toastMessage.observe(this) { toast(it) }
    }
    private fun moveRegister(){
        binding.registerButton.setOnClickListener {
            val moveWithParcelableIntent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(moveWithParcelableIntent)
        }
    }
    private fun actionLogin(){
        binding.loginButton.setOnClickListener {
            login()
        }
    }
    private fun login(){
        val email = binding.emailEditText.text.toString().trim()
        val password = binding.passwordEditText.text.toString().trim()

        when{
            email.isEmpty()->{
                binding.emailEditTextLayout.error = "Masukkan email"
            }
            password.isEmpty()->{
                binding.passwordEditTextLayout.error = "Masukkan password"
            }
            else->{
                viewModelLogin.loginUser(email, password)
                viewModelLogin.userLogin.observe(this){
                    AlertDialog.Builder(this).apply {
                        setTitle("berhasil login")
                        setMessage("Selamat Datang !")
                        setPositiveButton("Lanjut") { _, _ ->
                            val intent = Intent(context, MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent,
                                ActivityOptionsCompat.makeSceneTransitionAnimation(this@LoginActivity as Activity).toBundle()
                            )
                            finish()
                        }
                        create()
                        show()
                    }
                    saveSession(UserSession(it.email))
                }
            }
        }
    }

    private fun saveSession(user: UserSession){
        dataStoreViewModel.setSession(user)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}