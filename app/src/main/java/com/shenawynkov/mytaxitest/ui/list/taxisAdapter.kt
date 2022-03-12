package com.shenawynkov.mytaxitest.ui.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.shenawynkov.domain.model.Poi

import com.shenawynkov.mytaxitest.R


class TaxisAdapter(var list: List<Poi>, private val taxisListener: TaxisListener) :
    RecyclerView.Adapter<TaxisAdapter.ViewHolder>() {


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvType: TextView
        val tvLat: TextView
        val tvLon: TextView

        init {
            // Define click listener for the ViewHolder's View.
            tvLat = view.findViewById(R.id.tv_lat)
            tvLon = view.findViewById(R.id.tv_lon)
            tvType = view.findViewById(R.id.tv_type)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_taxi, viewGroup, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val item = list[position]
        // set data
        viewHolder.tvType.text = item.fleetType
        viewHolder.tvLon.text = item.coordinate.longitude.toString()
        viewHolder.tvLat.text = item.coordinate.latitude.toString()
        //nav actions
        viewHolder.itemView.setOnClickListener {
            taxisListener.onPoiSelected(list[viewHolder.adapterPosition])
        }
    }

    override fun getItemCount() = list.size


    fun setNewList(newList: List<Poi>) {
        this.list = newList
        notifyDataSetChanged()
    }

    interface TaxisListener {
        fun onPoiSelected(poi: Poi)
    }
}
