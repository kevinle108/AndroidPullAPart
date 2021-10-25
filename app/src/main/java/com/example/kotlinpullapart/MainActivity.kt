package com.example.kotlinpullapart

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.kotlinpullapart.data.StartUp
import com.example.kotlinpullapart.databinding.ActivityMainBinding

private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    val makes = StartUp.makes
    var makeNames = makes.map { it.makeName }
    var modelsNameIdMap = mutableMapOf<String, Int>()
    var currentModelsList = mutableListOf<String>()
    var selectedMakeId = 0
    var selectedModelId = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        Log.i(TAG, "onCreate: makes size = ${makes.size}")
        Log.i(TAG, "MAKE56 ${makes.first{it.makeID == 56}.makeName}")

//        val listParent = mutableListOf<String>(
//            "Honda",
//            "Nissan",
//            "Toyota",
//        )

        val listParent = makes.map { it.makeName }

//        val listParent = mutableListOf<String>(
//            "Animals",
//            "Birds",
//            "Flowers"
//        )
        val arrayAdapterParent = ArrayAdapter(
            this, R.layout.textview_blue, makeNames
        )
        binding.spParent.adapter = arrayAdapterParent

//        val listChildAnimals = arrayListOf<String>(
//            "Tiger",
//            "Lion",
//            "Elephant",
//            "Monkey",
//            "Cow",
//            "Go!"
//        )
//        val listChildFlowers = arrayListOf<String>(
//            "Rose",
//            "Lotus"
//        )
//        val listChildBirds = arrayListOf<String>(
//            "Sparrow", "Eagle", "Peacock"
//        )
//
//        val childLists = listOf<List<String>>(listChildAnimals, listChildBirds, listChildFlowers)
        val arrayAdapterChild = ArrayAdapter(this, R.layout.textview_red, currentModelsList)
        binding.spChild.adapter = arrayAdapterChild

        binding.spParent.onItemSelectedListener = object :AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                Log.i(TAG, "Parent Spinner onItemSelected: fired")
                Toast.makeText(applicationContext, "MakeID: ${makes[position].makeID}", Toast.LENGTH_SHORT).show()
                selectedMakeId = makes[position].makeID

                if (selectedMakeId == 0) {
                    currentModelsList = mutableListOf<String>("MODEL")
                    val adapterChild = ArrayAdapter(applicationContext, R.layout.textview_red, currentModelsList)
                    binding.spChild.adapter = adapterChild
                    return
                }

                val models = viewModel.getModelsFromMakeId(selectedMakeId)
                val modelsNames = models.map { it.modelName }
                currentModelsList = modelsNames.sorted().toMutableList()

                for (model in models) {
                    if (modelsNameIdMap[model.modelName] == null) {
                        modelsNameIdMap.put(model.modelName, model.modelID)
                    }
                }
//                Toast.makeText(applicationContext, id.toString(), Toast.LENGTH_LONG).show()


                val adapterChild = ArrayAdapter(applicationContext, R.layout.textview_red, currentModelsList)
                binding.spChild.adapter = adapterChild
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
//                Log.i(TAG, "Parent Spinner onNothingSelected: fired")
            }
        }

        binding.spChild.onItemSelectedListener = object :AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                Log.i(TAG, "Child Spinner onItemSelected: fired")
                val selectedModelName = currentModelsList[position]
                selectedModelId = modelsNameIdMap[selectedModelName] ?: 0
                Toast.makeText(applicationContext, "ModelId: $selectedModelId", Toast.LENGTH_LONG).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
//                Log.i(TAG, "Child Spinner onNothingSelected: fired")
            }


        }
//        binding.spParent.onItemSelectedListener = object :AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(
//                parent: AdapterView<*>?,
//                view: View?,
//                position: Int,
//                id: Long
//            ) {
//                Toast.makeText(applicationContext, listParent[position], Toast.LENGTH_LONG).show()
//
//                val adapterChild = ArrayAdapter(applicationContext, R.layout.textview_red, childLists[position])
//                binding.spChild.adapter = adapterChild
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>?) {
//                TODO("Not yet implemented")
//            }
//        }

//        binding.spChild.onItemSelectedListener = object :AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(
//                parent: AdapterView<*>?,
//                view: View?,
//                position: Int,
//                id: Long
//            ) {
//                if (listChildAnimals[position] == "Go!") {
//                    val activityIntent = Intent(this@MainActivity, ListTable::class.java)
//                    activityIntent.putExtra("TestConstant", 42)
//                    startActivity(activityIntent)
//                }
//
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>?) {
//                TODO("Not yet implemented")
//            }
//
//
//        }

        binding.button.setOnClickListener {
            Log.i(TAG, "API Button clicked!")
            Log.i(TAG, "makeId: $selectedMakeId, modelId: $selectedModelId")
//            println("")
//            viewModel.getMakes()
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