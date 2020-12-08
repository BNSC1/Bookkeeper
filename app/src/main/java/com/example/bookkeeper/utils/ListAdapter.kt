package com.example.bookkeeper.utils

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.example.bookkeeper.R
import com.example.bookkeeper.model.Entry
import com.example.bookkeeper.model.EntryVM
import kotlinx.android.synthetic.main.fragment_bookkeep.view.*

//class ListAdapter(
//    itemList: LiveData<List<Entry>>,
//    private val listener: EntryListItemClickNotify, private val entryVM: EntryVM
//) :
class ListAdapter(
    private val entryVM: EntryVM
) :
    RecyclerView.Adapter<ListAdapter.MyViewHolder>() {
    private var entryList = emptyList<Entry>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        )
    }

    //    inner class MyViewHolder(itemView: View, listener: EntryListItemClickNotify) :
    inner class MyViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var delImgBtn = itemView.findViewById<ImageButton>(R.id.deleteBTN)
        override fun onClick(v: View?) {
        }

    }


    override fun getItemCount(): Int {
        return entryList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = entryList[position]

        holder.itemView.descriptionET.setText(currentItem.description)
        holder.delImgBtn.setOnClickListener {
            Log.v("Img Button", "Clicked $currentItem")
            entryVM.deleteEntry(currentItem)
        }
    }

    fun setData(user: List<Entry>) {
        this.entryList = user
        notifyDataSetChanged()
    }
}
