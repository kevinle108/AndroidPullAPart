package com.example.kotlinpullapart

import android.content.Intent
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
import com.example.kotlinpullapart.models.LotItem
import com.example.kotlinpullapart.models.LotLocation
import kotlinx.coroutines.runBlocking
import android.app.AlertDialog

private const val TAG = "MainActivity"
private const val INITIAL_YEAR = 2000
private const val INITIAL_MAKE = "TOYOTA"
class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    val years = StartUp.years
    val makes = StartUp.makes.sortedBy { it.makeName }
    var modelsNameIdMap = mutableMapOf<String, Int>()
    var currentModelsList = mutableListOf<String>()
    var selectedYear = 0
    var selectedMakeId = 0
    var selectedModelId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        // dialog for when a search returns zero matches
        val zeroMatchesDialog = AlertDialog.Builder(this)
            .setTitle("Sorry!")
            .setMessage("This location does not contain any matches for your vehicle.")
            .setPositiveButton("OK") { _, _ ->
            }
            .create()

        // set spinners to their list adapters
        val makeNames = makes.map { it.makeName }
        binding.spYear.adapter = ArrayAdapter(this, R.layout.spinner_item, years)
        binding.spMake.adapter = ArrayAdapter(this, R.layout.spinner_item, makeNames)
        binding.spModel.adapter = ArrayAdapter(this, R.layout.spinner_item, currentModelsList)

        // set spinners to 2000 Toyota Avalon
        binding.spYear.setSelection(years.indexOf(INITIAL_YEAR))
        binding.spMake.setSelection(makes.indexOfFirst { it.makeName == INITIAL_MAKE })

        binding.spYear.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedYear = years[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }


        binding.spMake.onItemSelectedListener = object :AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedMakeId = makes[position].makeID

                if (selectedMakeId == 0) {
                    currentModelsList = mutableListOf<String>("MODEL")
                    val adapterChild = ArrayAdapter(applicationContext, R.layout.spinner_item, currentModelsList)
                    binding.spModel.adapter = adapterChild
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

                val adapterChild = ArrayAdapter(applicationContext, R.layout.spinner_item, currentModelsList)
                binding.spModel.adapter = adapterChild
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        binding.spModel.onItemSelectedListener = object :AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (viewModel.getBeforeSelection()) {
                    binding.spModel.setSelection(currentModelsList.indexOf("AVALON"))
                    viewModel.setBeforeSelection(false)
                    return
                }
                else {
                    val selectedModelName = currentModelsList[position]
                    selectedModelId = modelsNameIdMap[selectedModelName] ?: 0
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }


        binding.btnSearch.setOnClickListener {
//            addContactDialog.show()
//            singleChoiceDialog.show()
//            multiChoiceDialog.show()



            val searchEntry = "$selectedYear ${makes.first { it.makeID == selectedMakeId }.makeName} ${ modelsNameIdMap.filterValues { it == selectedModelId }.keys.first()}"
            if (viewModel.isDuplicateSearch(searchEntry)) {

            }


            // check for valid search String, String, Int, String
            if (selectedYear != 0 && selectedMakeId != 0 && selectedModelId != 0) {
                var searchResult = listOf<LotItem>()
                runBlocking {
                    val year = selectedYear.toString()
                    val make = selectedMakeId
                    val model = selectedModelId.toString()
                    searchResult = viewModel.searchCar(year, make, model)
                }
                if (searchResult.isEmpty()) {
                    zeroMatchesDialog.show()
//                    Toast.makeText(this, "Sorry! No matches for this car.", Toast.LENGTH_SHORT).show()
                } else {
                    viewModel.updateLotResults(searchResult)
                    goToResultsScreen()
                }

            }
        }

        binding.btnViewResults.setOnClickListener {
            if (viewModel.getLotResults().isEmpty()) {
                Toast.makeText(this, "Results list is currently empty!", Toast.LENGTH_SHORT).show()
            }
            else {
                goToResultsScreen()
            }
        }
    }

    fun goToResultsScreen() {
        Intent(this, SecondActivity::class.java).also {
            it.putExtra("EXTRA_RESULT", LotLocation(viewModel.getLotResults()))
            startActivity(it)
        }
    }
}