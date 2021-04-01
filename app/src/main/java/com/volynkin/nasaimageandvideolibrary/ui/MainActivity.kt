package com.volynkin.nasaimageandvideolibrary.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.volynkin.nasaimageandvideolibrary.databinding.ActivityMainBinding
import com.volynkin.nasaimageandvideolibrary.extensions.AppPreferences


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppPreferences.setup(applicationContext)
    }
}