package com.dali.schedulingengineers.view


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.dali.schedulingengineers.R
import kotlinx.android.synthetic.main.fragment_schedule.*

/**
 * A simple [Fragment] subclass.
 */
class ScheduleFragment : Fragment() {

    companion object {
        fun newInstance(): ScheduleFragment {
            return ScheduleFragment()
        }
    }

    lateinit var viewModel: MainViewModel
    lateinit var adapter: EngineersScheduleAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_schedule, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
    }

    private fun setupViewModel() {
        activity?.let {
            viewModel = ViewModelProviders.of(it).get(MainViewModel::class.java)

            viewModel.engineersScheduleLiveData.value?.data?.let { schedule ->
                scheduleRecyclerView.layoutManager =
                    GridLayoutManager(activity, schedule[0].size + 1)
                adapter = EngineersScheduleAdapter(schedule)
                scheduleRecyclerView.adapter = adapter

                for (day in 0 until schedule.size) {
                    for (shift in 0 until schedule[day].size)
                        print("${schedule[day][shift]?.name} ")
                    println()
                }
            }
        }
    }
}
