package com.example.bookkeeper.utils

//import kotlinx.android.synthetic.main.fragment_bookkeep.view.*
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.bookkeeper.R
import com.example.bookkeeper.model.Entry
import com.example.bookkeeper.model.EntryVM
import kotlinx.android.synthetic.main.list_item.view.*
import java.text.DecimalFormat

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
        val delImgBtn = itemView.findViewById<ImageButton>(R.id.deleteBTN)!!
        val viewItem = itemView.findViewById<ConstraintLayout>(R.id.constraintlayout)!!
        override fun onClick(v: View?) {
        }

    }


    override fun getItemCount(): Int {
        return entryList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = entryList[position]
        Log.d("[TEST]", currentItem.date.toString())
        holder.itemView.descriptionTV.text = currentItem.description
        holder.itemView.amountTV.text = DecimalFormat("$#,###").format(currentItem.amount)
        if (currentItem.amount!! > 0) {
            holder.itemView.amountTV.setTextColor(Color.parseColor("#00CD00"))
        } else {
            holder.itemView.amountTV.setTextColor(Color.parseColor("#CD0000"))
        }
//        holder.itemView.timeTV.text = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(currentItem.date)
        val string =
            currentItem.date!!.toLocalDate().toString() + " " + currentItem.date.toLocalTime()
                .toString().substring(0, 5)
        holder.itemView.timeTV.text = string
        holder.delImgBtn.setOnClickListener {
            Log.v("Img Button", "Clicked $currentItem")
            entryVM.deleteEntry(currentItem)
        }
        holder.viewItem.setOnClickListener {
            //TODO: implement click to modify.
        }
    }

    fun setData(user: List<Entry>) {
        this.entryList = user
        notifyDataSetChanged()
    }
}
