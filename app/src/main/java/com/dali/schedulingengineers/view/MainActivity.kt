package com.dali.schedulingengineers.view

import android.app.AlertDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.dali.schedulingengineers.R
import com.dali.schedulingengineers.utils.RepositoryResult
import com.jathwah.apps.kanz.utils.inTransaction
import dmax.dialog.SpotsDialog

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: MainViewModel
    var dialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView(savedInstanceState)
        setupViewModel()

        addFragment(R.id.contentsFrameLayout, EngineersListFragment.newInstance())
    }

    fun setupView(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_main)
    }

    fun setupViewModel() {
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        viewModel.engineersScheduleLiveData.observe(this, Observer { result ->
            result?.let {
                when {
                    result.state == RepositoryResult.START_LOADING -> {
                        dialog = SpotsDialog.Builder()
                            .setContext(this)
                            .setMessage("Finding a schedule")
                            .setCancelable(false)
                            .build()
                            .apply { show() }
                    }
                    result.state == RepositoryResult.SUCCESS -> {
                        dialog?.dismiss()
                        addFragment(
                            R.id.contentsFrameLayout,
                            ScheduleFragment.newInstance(),
                            ScheduleFragment::class.java.canonicalName
                        )
                    }
                    else -> {
                        dialog?.dismiss()
                        Toast.makeText(
                            this,
                            "Couldn't find a schedule with provided constraints",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

            }
        })
    }

    fun addFragment(containerId: Int, fragment: Fragment, backStackTag: String? = null) {
        supportFragmentManager.inTransaction {
            backStackTag?.let {
                add(containerId, fragment)
                addToBackStack(it)
            } ?: kotlin.run {
                add(containerId, fragment)
            }
        }
    }
}
