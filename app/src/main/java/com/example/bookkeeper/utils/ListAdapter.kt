package com.example.bookkeeper.utils

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.bookkeeper.R
import com.example.bookkeeper.model.Entry
import com.example.bookkeeper.model.EntryVM
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.list_item.view.*

//class ListAdapter(
//    itemList: LiveData<List<Entry>>,
//    private val listener: EntryListItemClickNotify, private val entryVM: EntryVM
//) :
class ListAdapter(
    private val entryVM: EntryVM,
) :
    RecyclerView.Adapter<ListAdapter.MyViewHolder>() {
    private var entryList = emptyList<Entry>()
    private lateinit var moneyFormatHelper: MoneyFormatHelper
    private lateinit var context: Context
    private lateinit var resources: Resources

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        context = parent.context
        resources = context.resources
        moneyFormatHelper = MoneyFormatHelper(context)
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        )
    }

    //    inner class MyViewHolder(itemView: View, listener: EntryListItemClickNotify) :
    inner class MyViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val delImgBtn: ImageButton = itemView.findViewById(R.id.deleteBTN)
        val viewItem: ConstraintLayout = itemView.findViewById(R.id.constraintlayout)
        override fun onClick(v: View?) {
        }

    }


    override fun getItemCount(): Int {
        return entryList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = entryList[position]
        holder.itemView.descriptionTV.text = currentItem.description
        holder.itemView.amountTV.text = moneyFormatHelper.getPrefMoneyString(currentItem.amount!!)
        if (currentItem.amount > 0) {
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
            MaterialAlertDialogBuilder(context)
                .setTitle(resources.getString(R.string.dialog_delete_title))
                .setNegativeButton(resources.getString(R.string.dialog_delete_cancel)) { _, _ ->
                }
                .setPositiveButton(resources.getString(R.string.dialog_delete_confirm)) { _, _ ->
                    entryVM.deleteEntry(currentItem)
                }
                .show()
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
