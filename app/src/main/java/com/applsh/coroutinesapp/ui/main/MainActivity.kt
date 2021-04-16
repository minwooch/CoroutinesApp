package com.applsh.coroutinesapp.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.applsh.coroutinesapp.R
import com.applsh.coroutinesapp.databinding.MainActivityBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var binding: MainActivityBinding

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.main_activity)
        binding.lifecycleOwner = this

        navController =
            (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController
    }
}