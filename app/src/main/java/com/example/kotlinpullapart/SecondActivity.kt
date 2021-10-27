package com.example.kotlinpullapart

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.kotlinpullapart.databinding.ActivityMainBinding
import com.example.kotlinpullapart.databinding.ActivitySecondBinding
import com.example.kotlinpullapart.models.LotItem
import com.example.kotlinpullapart.models.LotLocation

private const val TAG = "SecondActivity"
class SecondActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySecondBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val result = intent.getSerializableExtra("EXTRA_RESULT") as LotLocation
        Log.i(TAG, "onCreate: $result")

        val lotList = result.lots
        var lotsString = ""
        for (lot in lotList) {
            lotsString += "Row: ${lot.row}\n\n"
        }
        binding.textView.text = lotsString

        binding.btnBack.setOnClickListener {
            finish()
        }

    }



}