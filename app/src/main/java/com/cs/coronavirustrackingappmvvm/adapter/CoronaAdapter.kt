package com.cs.coronavirustrackingappmvvm.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cs.coronavirustrackingappmvvm.R
import com.cs.coronavirustrackingappmvvm.model.Attributes

class CoronaAdapter(var context: Context, var countryList: ArrayList<Attributes>): RecyclerView.Adapter<CoronaAdapter.coronaViewHolder>() {
    inner class coronaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): coronaViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_corona, parent,false)
        return coronaViewHolder(view)
    }

    override fun getItemCount() = countryList.size

    override fun onBindViewHolder(holder: coronaViewHolder, position: Int) {

    }
}