package com.surya_yasa_antariksa.localdatetime

import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.surya_yasa_antariksa.localdatetime.database.entity.DateTimeEntity
import org.w3c.dom.Text

class UserAdapter(var list: List<DateTimeEntity>, val listener: OnItemClickListener):RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    inner class ViewHolder(view:View): RecyclerView.ViewHolder(view){
        var nama: TextView
        var tanggal: TextView
        var kegiatan: TextView
        var updateButton: ImageButton
        var deleteButton: ImageButton
        init {
            nama = view.findViewById(R.id.nama)
            tanggal = view.findViewById(R.id.tanggal_kegiatan)
            kegiatan = view.findViewById(R.id.kegiatan)
            updateButton = view.findViewById(R.id.edit_button)
            deleteButton = view.findViewById(R.id.delete_button)

            updateButton.setOnClickListener {
                listener.onUpdateClick(adapterPosition)
            }

            deleteButton.setOnClickListener {
                listener.onDeleteClick(adapterPosition)
            }
        }
    }

    interface OnItemClickListener {
        fun onUpdateClick(position: Int)
        fun onDeleteClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_adapter, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.nama.text = list[position].name
        holder.tanggal.text = list[position].localDateTime.toString()
        holder.kegiatan.text = list[position].kegiatan
    }

    override fun getItemCount(): Int {
        return list.size
    }

}