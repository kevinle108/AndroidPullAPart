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
//    var makeNames = makes.map { it.makeName }
    var modelsNameIdMap = mutableMapOf<String, Int>()
    var currentModelsList = mutableListOf<String>()
    var selectedYear = 0
    var selectedMakeId = 0
    var selectedModelId = 0
    var beforeSelection = true

    fun loadPreviousSelections() {
        println(viewModel.getSavedSelections())
    }

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

        val addContactDialog = AlertDialog.Builder(this)
            .setTitle("Add Contact")
            .setMessage("Do you want to add Kevin to your contact list?")
            .setIcon(R.drawable.ic_add_contact_foreground)
            .setPositiveButton("Yay") { _, _ ->
                Toast.makeText(this, "You added Kevin to your contact list", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Nah") { _, _ ->
                Toast.makeText(this, "You did not add Kevin to your contact list", Toast.LENGTH_SHORT).show()
            }
            .create()

        val options = arrayOf("First Item", "Second Item", "Third Item")
        val singleChoiceDialog = AlertDialog.Builder(this)
            .setTitle("Choose one of these options:")
            .setSingleChoiceItems(options, 0) { dialogInterface, i ->
                Toast.makeText(this, "You selected ${options[i]}", Toast.LENGTH_SHORT).show()
            }
            .setPositiveButton("Accept") { _, _ ->
                Toast.makeText(this, "You accepted.", Toast.LENGTH_SHORT).show()

            }.setNegativeButton("Deny") { _, _ ->
                Toast.makeText(this, "You denied.", Toast.LENGTH_SHORT).show()

            }
            .create()

        val garage = arrayOf("2000 Toyota Avalon", "2005 Toyota Sienna", "2016 Nissan Rogue")
        val multiChoiceDialog = AlertDialog.Builder(this)
            .setTitle("Choose 1 or more vehicles:")
            .setMultiChoiceItems(garage, garage.map { true }.toBooleanArray()) { dialogInterface, i, isChecked ->
                if (isChecked) {
                    Toast.makeText(this, "You checked ${garage[i]}", Toast.LENGTH_SHORT).show()
                }
                else Toast.makeText(this, "You unchecked ${garage[i]}", Toast.LENGTH_SHORT).show()
            }.setPositiveButton("Yes") { _, _ ->
                Toast.makeText(this, "Searching lots for checked vehicles...", Toast.LENGTH_SHORT).show()
            }.setNegativeButton("No") { _, _ ->
                Toast.makeText(this, "Search cancelled", Toast.LENGTH_SHORT).show()
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
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                selectedYear = years[position]
                viewModel.setSavedYear(selectedYear)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.spMake.onItemSelectedListener = object :AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedMakeId = makes[position].makeID
                viewModel.setSavedMakeID(selectedMakeId)
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

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }


        binding.spModel.onItemSelectedListener = object :AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (beforeSelection) {
                    binding.spModel.setSelection(currentModelsList.indexOf("AVALON"))
                    beforeSelection = false
                }
                val selectedModelName = currentModelsList[position]
                viewModel.setSavedModelID(selectedModelId)
                selectedModelId = modelsNameIdMap[selectedModelName] ?: 0
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.btnSearch.setOnClickListener {
//            addContactDialog.show()
//            singleChoiceDialog.show()
//            multiChoiceDialog.show()

//            Toast.makeText(applicationContext, text, Toast.LENGTH_LONG).show()

            // check for valid search String, String, Int, String
            if (selectedYear != 0 && selectedMakeId != 0 && selectedModelId != 0) {
                if(viewModel.isDuplicateSearch(searchEntry())) {
                    goToResultsScreen()
                    return@setOnClickListener
                }
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

        binding.btnLot.setOnClickListener {
            var lots: List<LotItem> = listOf<LotItem>()
            goToResultsScreen()
        }
    }

    fun goToResultsScreen() {
        Intent(this, SecondActivity::class.java).also {
            it.putExtra("EXTRA_RESULT", LotLocation(viewModel.getLotResults()))
            startActivity(it)
        }
    }
}