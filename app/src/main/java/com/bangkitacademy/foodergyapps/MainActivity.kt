package com.bangkitacademy.foodergyapps

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.bangkitacademy.foodergyapps.databinding.ActivityMainBinding
import com.bangkitacademy.foodergyapps.utils.DataStoreViewModel
import com.bangkitacademy.foodergyapps.view.login.LoginActivity
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val dataStoreViewModel by viewModels<DataStoreViewModel>()
    private lateinit var binding: ActivityMainBinding

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    getString(R.string.permission_not_granted),
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //setupView()
        //setupViewModel()
        dataStoreViewModel.getSession().observe(this){ userSession ->
            if(userSession.email == ""){
                Toast.makeText(this,"Login dulu yuk!", Toast.LENGTH_SHORT).show()
                Log.d("tag", userSession.email)
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent,
                    ActivityOptionsCompat.makeSceneTransitionAnimation(this@MainActivity as Activity).toBundle()
                )
                finish()
            }
//            else{
//                val dashboard = Intent(this, MainActivity::class.java)
//                startActivity(dashboard)
//            }

        }
        val bottomNavigationView: BottomNavigationView = binding.bottomNavbar
        val appbar : BottomAppBar = binding.bottomAppBar
        val fab : FloatingActionButton = binding.scanFab
        supportActionBar?.hide()
        bottomNavigationView.background = null


        val navController = findNavController(R.id.nav_host_fragment)
        bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener{ _,destinantion,_->
            if (destinantion.id == R.id.cameraFragment) {
                bottomNavigationView.visibility = View.GONE
                appbar.visibility = View.GONE
                fab.visibility = View.GONE

            }
            else{
                bottomNavigationView.visibility = View.VISIBLE
                appbar.visibility = View.VISIBLE
                fab.visibility = View.VISIBLE
            }
        }

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        binding.scanFab.setOnClickListener{
            if(allPermissionsGranted()){
                startCameraX()

            }
            else{
                Toast.makeText(this,getString(R.string.permission_not_granted),Toast.LENGTH_SHORT).show()

            }
        }

    }
    private fun startCameraX() {
        findNavController(R.id.nav_host_fragment).navigate(R.id.cameraFragment)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menutop, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.logout->{
                dataStoreViewModel.logout()
                return true
            }


            else->true
        }
    }


    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
//    private fun replaceFragment(fragment: Fragment){
//        if(fragment != null){
//            val transaction = supportFragmentManager.beginTransaction()
//            transaction.replace(R.id.fragment_container,fragment)
//            transaction.commit()
//        }
//    }
}