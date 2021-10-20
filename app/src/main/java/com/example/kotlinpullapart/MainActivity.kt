package com.example.kotlinpullapart

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kotlinpullapart.api.RetrofitInstance
import com.example.kotlinpullapart.databinding.ActivityMainBinding

private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)



        //start count button
        var count = 0;
        binding.btnCount.setOnClickListener {
            count++
            binding.tvCount.text = "Let's count together: $count"
        }

        //end count button

        val listParent = mutableListOf<String>(
            "Honda",
            "Nissan",
            "Toyota",
        )
//        val listParent = mutableListOf<String>(
//            "Animals",
//            "Birds",
//            "Flowers"
//        )
        val arrayAdapterParent = ArrayAdapter(
            this, R.layout.textview_blue, listParent
        )
        binding.spParent.adapter = arrayAdapterParent

        val listChildAnimals = arrayListOf<String>(
            "Tiger",
            "Lion",
            "Elephant",
            "Monkey",
            "Cow",
            "Go!"
        )
        val listChildFlowers = arrayListOf<String>(
            "Rose",
            "Lotus"
        )
        val listChildBirds = arrayListOf<String>(
            "Sparrow", "Eagle", "Peacock"
        )

        val childLists = listOf<List<String>>(listChildAnimals, listChildBirds, listChildFlowers)
        val arrayAdapterChild = ArrayAdapter(this, R.layout.textview_red, listChildAnimals)
        binding.spChild.adapter = arrayAdapterChild

        binding.spParent.onItemSelectedListener = object :AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                Toast.makeText(applicationContext, listParent[position], Toast.LENGTH_LONG).show()

                val adapterChild = ArrayAdapter(applicationContext, R.layout.textview_red, childLists[position])
                binding.spChild.adapter = adapterChild
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        binding.spChild.onItemSelectedListener = object :AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (listChildAnimals[position] == "Go!") {
                    val activityIntent = Intent(this@MainActivity, ListTable::class.java)
                    activityIntent.putExtra("TestConstant", 42)
                    startActivity(activityIntent)
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

        binding.button.setOnClickListener {
            Log.i(TAG, "Button clicked!")
            viewModel.getMakes()
        }



//        val spinnerYear = findViewById<Spinner>(R.id.spinnerYear)
//        val spinnerMake = findViewById<Spinner>(R.id.spinnerMake)
//        val spinnerModel = findViewById<Spinner>(R.id.spinnerModel)



//        val spinnerCategories = listOf<Int>(
//            R.array.years,
//            R.array.makes,
//            R.array.models
//        )

//        ArrayAdapter.createFromResource(
//            this,
//            R.array.years,
//            android.R.layout.simple_spinner_item
//        ).also { adapter ->
//            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//            spinnerYear.adapter = adapter
//        }






    }
}