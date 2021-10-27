package com.example.kotlinpullapart

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.kotlinpullapart.databinding.ActivityMainBinding
import com.example.kotlinpullapart.databinding.ActivitySecondBinding

class SecondActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySecondBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.textView.text = intent.getStringExtra("EXTRA_RESULT")

        binding.btnBack.setOnClickListener {
            finish()
        }

    }



}