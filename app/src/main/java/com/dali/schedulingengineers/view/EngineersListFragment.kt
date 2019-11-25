package com.dali.schedulingengineers.view


import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.dali.schedulingengineers.R
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.fragment_engineers_list.*

/**
 * A simple [Fragment] subclass.
 */
class EngineersListFragment : Fragment() {

    companion object {
        fun newInstance(): EngineersListFragment {
            return EngineersListFragment()
        }
    }

    lateinit var adapter: EngineerListAdapter
    lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_engineers_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        setupView()
        getData(false)
    }

    fun setupViewModel() {
        activity?.let {
            viewModel = ViewModelProviders.of(it).get(MainViewModel::class.java)
            viewModel.viewStateLiveData.observe(this, Observer { result ->
                result?.let {
                    when (result) {
                        MainViewModel.EngineersListViewState.LOADING -> {
                            if (!engineersListSwipeRefresh.isRefreshing)
                                engineersListSwipeRefresh.isRefreshing = true
                            else {

                            }
                        }
                        MainViewModel.EngineersListViewState.SHOW_LIST -> {
                            engineersListSwipeRefresh.isRefreshing = false
                            adapter.notifyDataSetChanged()
                        }
                        MainViewModel.EngineersListViewState.ERROR -> {
                            context?.apply {
                                Toast.makeText(this, result.errorMessage, Toast.LENGTH_SHORT).show()
                            }
                        }
                        else -> {
                        }
                    }
                }
            })
        }
    }

    fun setupView() {
        engineersListSwipeRefresh.setOnRefreshListener { getData(true) }
        adapter = EngineerListAdapter(viewModel.engineersList)
        engineersListRecyclerView.adapter = adapter

        findScheduleButton.setOnClickListener {
            activity?.let {
                viewModel.findSchedule()
            }
        }
    }

    fun getData(isRefresh: Boolean) {
        viewModel.getEngineersList(isRefresh)
    }
}
