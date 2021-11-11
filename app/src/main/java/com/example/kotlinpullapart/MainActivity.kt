package com.example.kotlinpullapart

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
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
        binding.spYear.adapter = ArrayAdapter(this, R.layout.spinner_item, viewModel.years)
        binding.spMake.adapter = ArrayAdapter(this, R.layout.spinner_item, viewModel.makes.map { it.makeName })
        binding.spModel.adapter = ArrayAdapter(this, R.layout.spinner_item, viewModel.getCurrentModelsList())

        // set spinners to 2000 Toyota Avalon
//        binding.spYear.setSelection(viewModel.years.indexOf(INITIAL_YEAR))
//        binding.spMake.setSelection(viewModel.makes.indexOfFirst { it.makeName == INITIAL_MAKE })

        binding.spYear.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.yearSelectionHandler(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }


        binding.spMake.onItemSelectedListener = object :AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.makeSelectionHandler(position)
                val modelsList = viewModel.getCurrentModelsList()
                println(modelsList)
                binding.spModel.adapter = ArrayAdapter(this@MainActivity, R.layout.spinner_item, modelsList)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.spModel.onItemSelectedListener = object :AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (viewModel.getBeforeSelection()) {
                    binding.spModel.setSelection(viewModel.getCurrentModelsList().indexOf("AVALON"))
                    viewModel.setBeforeSelection(false)
                    return
                }
                else {
                    viewModel.modelSelectionHandler(position)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }


        binding.btnSearch.setOnClickListener {
//            addContactDialog.show()
//            singleChoiceDialog.show()
//            multiChoiceDialog.show()

            val selectedYear = viewModel.getSelectedYear()
            val selectedMakeId = viewModel.getSelectedYear()
            val selectedModelId = viewModel.getSelectedYear()

            val searchEntry = viewModel.currentSearchEntry()
            if (viewModel.isDuplicateSearch(searchEntry)) {
                //TODO
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