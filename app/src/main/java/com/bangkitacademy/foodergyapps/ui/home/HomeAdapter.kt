package com.bangkitacademy.foodergyapps.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bangkitacademy.foodergyapps.R
import com.bangkitacademy.foodergyapps.data.model.DataHistory

class HomeAdapter(private val listHistory: ArrayList<DataHistory>): RecyclerView.Adapter<HomeAdapter.ListViewHolder>(){

    override fun getItemCount(): Int = listHistory.size
    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvNameKemasan: TextView = itemView.findViewById(R.id.tvName)
        var tvKomposisi: TextView = itemView.findViewById(R.id.tvKomposisi)

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_row_alergi, parent, false)
        return ListViewHolder(view)
    }

        override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
            val user = listHistory[position]
            holder.tvNameKemasan.text = user.namaMakanan
            holder.tvKomposisi.text = user.komposisi

}

private fun ImageView.setImageResource(rekomendasi: String?) {
    if(rekomendasi == "True"){
        this.setImageResource(R.drawable.ic_bahaya)
    }else{
        this.setImageResource(R.drawable.ic_aman)
    }
}

}
