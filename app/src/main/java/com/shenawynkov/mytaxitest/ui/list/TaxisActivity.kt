package com.shenawynkov.mytaxitest.ui.list

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.shenawynkov.domain.model.Poi
import com.shenawynkov.mytaxitest.R
import com.shenawynkov.mytaxitest.ui.map.MapsActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TaxisActivity : AppCompatActivity(), TaxisAdapter.TaxisListener {
    private val viewModel: TaxisViewModel by viewModels()
    private val taxisAdapter: TaxisAdapter by lazy {
        TaxisAdapter(ArrayList(), this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel.fetchPois()
        setUp()

    }

    private fun setUp() {
        //init views
        rv_taxis.apply {
            adapter = taxisAdapter
            layoutManager = LinearLayoutManager(this@TaxisActivity)
        }
        bt_map.setOnClickListener {
            moveToMap(viewModel.pois.value as ArrayList<Poi>)
        }
        //observers
        lifecycleScope.launch {
            viewModel.loading.collectLatest {
                when (it) {
                    true -> progressBar.visibility = View.VISIBLE
                    false -> progressBar.visibility = View.GONE
                }
            }
        }
        lifecycleScope.launch {
            viewModel.pois.collect {
                taxisAdapter.setNewList(it)
            }
        }
    }

    override fun onPoiSelected(poi: Poi) {
        moveToMap(viewModel.pois.value as ArrayList<Poi>, poi)
    }

    private fun moveToMap(pois: ArrayList<Poi>, selectedPoi: Poi? = null) {
        val intent = Intent(this, MapsActivity::class.java)
        intent.putExtra(MapsActivity.POIS, pois)
        intent.putExtra(MapsActivity.POI, selectedPoi)
        startActivity(intent)
    }
}