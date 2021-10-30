package com.example.kotlinpullapart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinpullapart.databinding.ListItemBinding
import com.example.kotlinpullapart.models.LotItem

class ListAdapter(
    var locations: List<LotItem>
) : RecyclerView.Adapter<ListAdapter.ListViewHolder>() {
    inner class ListViewHolder(val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.binding.apply {
            val currentItem = locations[position]
            tvRow.text = currentItem.row.toString()
            tvCarTitle.text = "${currentItem.modelYear} ${currentItem.makeName} ${currentItem.modelName}"
            tvDate.text = parseDateString(currentItem.dateYardOn)
        }
    }

    override fun getItemCount(): Int {
        return locations.size
    }



    fun parseDateString(str: String) : String {
        val year = str.substring(2,4)
        val month = str.substring(5,7)
        val day = str.substring(8,10)
        return "$month/$day/$year"
    }
}