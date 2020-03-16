package com.cs.coronavirustrackingappmvvm.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cs.coronavirustrackingappmvvm.R
import com.cs.coronavirustrackingappmvvm.model.Attributes
import com.cs.coronavirustrackingappmvvm.model.Feature

class CoronaAdapter(var context: Context, var countryList: ArrayList<Feature>): RecyclerView.Adapter<CoronaAdapter.coronaViewHolder>() {

    private lateinit var txt_country: TextView
    private lateinit var txt_infected_value: TextView
    private lateinit var death_value: TextView
    private lateinit var txt_rec_value: TextView

    inner class coronaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        init {
            txt_country = itemView.findViewById(R.id.txt_country)
            txt_infected_value = itemView.findViewById(R.id.txt_infected_value)
            death_value = itemView.findViewById(R.id.death_value)
            txt_rec_value = itemView.findViewById(R.id.txt_rec_value)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): coronaViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_corona, parent,false)
        return coronaViewHolder(view)
    }

    override fun getItemCount() = countryList.size

    override fun onBindViewHolder(holder: coronaViewHolder, position: Int) {
        val item = countryList[position]
        txt_country.text = item.attributes.Country_Region
        txt_infected_value.text = item.attributes.Confirmed.toString()
        death_value.text = item.attributes.Deaths.toString()
        txt_rec_value.text = item.attributes.Recovered.toString()

    }
}