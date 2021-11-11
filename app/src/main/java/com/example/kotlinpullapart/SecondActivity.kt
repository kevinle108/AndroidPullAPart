package com.example.kotlinpullapart

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlinpullapart.databinding.ActivityMainBinding
import com.example.kotlinpullapart.databinding.ActivitySecondBinding
import com.example.kotlinpullapart.models.LotItem
import com.example.kotlinpullapart.models.LotLocation

private const val TAG = "SecondActivity"
class SecondActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySecondBinding
    private lateinit var lotList: List<LotItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val result = intent.getSerializableExtra("EXTRA_RESULT") as LotLocation
        lotList = result.lots
        var adapter = ListAdapter(lotList)
        binding.rvLocations.adapter = adapter
        binding.rvLocations.layoutManager = LinearLayoutManager(this)
    }



}