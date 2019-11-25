package com.dali.schedulingengineers.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dali.schedulingengineers.R
import com.dali.schedulingengineers.model.models.Engineer
import kotlinx.android.synthetic.main.cell_engineer.view.*

class EngineerListAdapter(val engineersList: List<Engineer>?) :
    RecyclerView.Adapter<EngineerListAdapter.EngineerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EngineerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.cell_engineer, parent, false)
        return EngineerViewHolder(view)
    }

    override fun getItemCount() = engineersList?.size ?: 0

    override fun onBindViewHolder(holder: EngineerViewHolder, position: Int) {
        holder.bind(engineersList?.get(position))
    }

    class EngineerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(engineer: Engineer?) {
            engineer?.let {
                itemView.engineerNameTextView.text = engineer.name
            }
        }
    }
}