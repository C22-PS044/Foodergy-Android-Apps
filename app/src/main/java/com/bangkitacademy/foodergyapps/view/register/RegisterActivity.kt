package com.bangkitacademy.foodergyapps.view.register

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import com.bangkitacademy.foodergyapps.databinding.ActivityRegisterBinding
import com.bangkitacademy.foodergyapps.helper.DateHelper
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.bangkitacademy.foodergyapps.MainActivity
import com.bangkitacademy.foodergyapps.data.api.ApiConfig
import com.bangkitacademy.foodergyapps.response.RegisterResponse

import dagger.hilt.android.AndroidEntryPoint
import retrofit2.*

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val viewModel by viewModels<RegisterViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //setupAction()
        setupView()
        setupViewModel()
        register()
    }
    private fun setupViewModel() {
        viewModel.isLoading.observe(this) { showLoading(it) }
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

    private fun register(){
        binding.registerButton.setOnClickListener {
            val name = binding.nameEditText.text.toString().trim()
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()
            val alergi  = binding.alergiEditText.text.toString().trim()

            viewModel.registerUser(name, email, password, alergi)
            val dialogBuilder = AlertDialog.Builder(this)
            val dashboard = Intent(this, MainActivity::class.java)
            dialogBuilder.setMessage("Akunnya sudah jadi.")
                .setTitle("Yeah!")
                .setPositiveButton("Lanjut") { _, _ ->
                    Toast.makeText(this@RegisterActivity, "Sukses membuat Akun", Toast.LENGTH_SHORT).show()
                    startActivity(dashboard)
                }
                .create()
                .show()
        }
    }
    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}