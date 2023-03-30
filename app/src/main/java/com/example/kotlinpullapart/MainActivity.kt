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
import javax.security.auth.login.LoginException

private const val TAG = "MainActivity"

// pre-set selections on load
private const val INITIAL_YEAR = 2000
private const val INITIAL_MAKE = "TOYOTA"
private const val INITIAL_MODEL = "AVALON"

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    val years = StartUp.years
    val makes = StartUp.makes.sortedBy { it.makeName }
    var modelsNameIdMap = mutableMapOf<String, Int>()
    var selectedYear = 0
    var selectedMakeId = 0
    var selectedModelId = 0
    var beforeSelection = true


    fun searchEntry(): String {
        val year = selectedYear
        val make = makes.first { it.makeID == selectedMakeId }.makeName
        val model = modelsNameIdMap.filter { it.value == selectedModelId }.keys.first()
        val searchEntry = "$year $make $model"
        println(searchEntry)
        return searchEntry
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

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
        val currentModelList = viewModel.currentModelsList ?: listOf<String>("Hello", "There", "Hello", "There")
        binding.spModel.adapter = ArrayAdapter(this, R.layout.spinner_item, currentModelList)

        // set first two spinners to initial selections
        binding.spYear.setSelection(years.indexOf(INITIAL_YEAR))
        binding.spMake.setSelection(makes.indexOfFirst { it.makeName == INITIAL_MAKE })

        binding.spYear.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                selectedYear = years[position]
                viewModel.savedYear = selectedYear.toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

//        binding.spMake.onItemSelectedListener = object :AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//                selectedMakeId = makes[position].makeID
//                viewModel.savedMakeID = makes.first { it.makeID == selectedMakeId }.makeName
//                val models = viewModel.getModelsFromMakeId(selectedMakeId)
//                val modelsNames = models.map { it.modelName }
//                viewModel.currentModelsList = modelsNames.sorted().toMutableList()
//                for (model in models) {
//                    if (modelsNameIdMap[model.modelName] == null) {
//                        modelsNameIdMap[model.modelName] = model.modelID
//                    }
//                }
//                val adapterChild = ArrayAdapter(applicationContext, R.layout.spinner_item, viewModel.currentModelsList)
//                binding.spModel.adapter = adapterChild
//            }
//            override fun onNothingSelected(parent: AdapterView<*>?) {}
//        }


        binding.spModel.onItemSelectedListener = object :AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // set third spinner to initial selection
                if (beforeSelection) {
                    binding.spModel.setSelection(viewModel.currentModelsList.indexOf(INITIAL_MODEL))
                    beforeSelection = false
                }
                val selectedModelName = viewModel.currentModelsList[position]
                viewModel.savedModelID = selectedModelName
                selectedModelId = modelsNameIdMap[selectedModelName] ?: 0
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.btnSearch.setOnClickListener {
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
                    val entry = searchEntry()
                    val results = searchResult.count()
                    val entries = viewModel.getSearchEntries()
                    if (!entries.containsKey(entry)) {
                        viewModel.addSearchEntry(entry, results)
                        viewModel.updateLotResults(searchResult)
                    }
                    goToResultsScreen()
                }
            }
        }

        binding.btnLot.setOnClickListener {
            if (viewModel.getLotResults().isEmpty()) {
                Toast.makeText(this, "No results to display yet. Try searching first.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            goToResultsScreen()
        }
        loadPrevSelections()
    }

    fun loadPrevSelections() {
        val saved = viewModel.getSavedSelections()
        if (saved[0].isNotBlank() && saved[1].isNotBlank() && saved[2].isNotBlank()) {
            beforeSelection = false
        }
    }

    fun goToResultsScreen() {
        Intent(this, SecondActivity::class.java).also {
            it.putExtra("EXTRA_RESULT", LotLocation(viewModel.getLotResults()))
            startActivity(it)
        }
    }
}