package com.dali.schedulingengineers.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dali.schedulingengineers.R
import com.dali.schedulingengineers.model.models.Engineer
import kotlinx.android.synthetic.main.cell_engineer.view.*

class EngineersScheduleAdapter(val schedule: Array<Array<Engineer?>>) :
    RecyclerView.Adapter<EngineersScheduleAdapter.EngineerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EngineerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.cell_engineer, parent, false)
        return EngineerViewHolder(view)
    }

    override fun getItemCount(): Int {
        val shiftTitlesOffset = schedule[0].size
        val dayTitleOffset = schedule.size + 1
        return shiftTitlesOffset + dayTitleOffset + (schedule.size * schedule[0].size)
    }

    override fun onBindViewHolder(holder: EngineerViewHolder, position: Int) {
        val day = position / (schedule[0].size + 1)
        val shift = position % (schedule[0].size + 1)

        val isPositionShiftTitle = day == 0
        val isDayTitle = shift == 0
        when {
            isPositionShiftTitle -> {
                if (position == 0)
                    holder.bind("/")
                else holder.bind("Shift $position")
            }
            isDayTitle -> {
                holder.bind("Day $day")
            }
            else -> {
                holder.bind(schedule[day - 1][shift - 1])
            }
        }
    }

    class EngineerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(engineer: Engineer?) {
            engineer?.let {
                itemView.engineerNameTextView.text = engineer.name
            }
        }

        fun bind(text: String) {
            itemView.engineerNameTextView.text = text
        }
    }
}