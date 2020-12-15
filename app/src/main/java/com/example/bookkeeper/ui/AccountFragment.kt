package com.example.bookkeeper.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bookkeeper.R
import com.example.bookkeeper.model.EntryVM
import com.example.bookkeeper.utils.ListAdapter
import kotlinx.android.synthetic.main.fragment_account.view.*

class AccountFragment : Fragment() {
    private lateinit var mEntryVM: EntryVM

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view= inflater.inflate(R.layout.fragment_account, container, false)
        mEntryVM = ViewModelProvider(this).get(EntryVM::class.java)
        val adapter = ListAdapter(mEntryVM)
        val recyclerView = view.recyclerview
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        mEntryVM.readAllEntry.observe(viewLifecycleOwner, { entry ->
            adapter.setData(entry)
        })

        return view
    }


}