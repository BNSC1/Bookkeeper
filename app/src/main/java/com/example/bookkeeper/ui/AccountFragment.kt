package com.example.bookkeeper.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bookkeeper.R
import com.example.bookkeeper.databinding.FragmentAccountBinding
import com.example.bookkeeper.model.EntryVM
import com.example.bookkeeper.utils.ListAdapter
import kotlinx.android.synthetic.main.fragment_account.view.*
import java.text.DecimalFormat

class AccountFragment : Fragment() {
    private lateinit var entryVM: EntryVM

    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        entryVM = ViewModelProvider(this).get(EntryVM::class.java)

        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = ListAdapter(entryVM)
        val recyclerView = view.recyclerview
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        entryVM.readAllEntry.observe(viewLifecycleOwner, { entry ->
            adapter.setData(entry)
        })
        setBalance()
    }

    private fun setBalance() {
        entryVM.readBalance.observe(viewLifecycleOwner, { balance ->
            binding.balanceTV.text =
                getString(R.string.msg_balance, DecimalFormat("$#,###").format(balance))
            if (balance < 0) {
                binding.balanceTV.setTextColor(
                    resources.getColor(
                        R.color.design_default_color_error,
                        null
                    )
                )
            } else {
                binding.balanceTV.setTextColor(
                    resources.getColor(
                        R.color.text_color,
                        null
                    )
                )
            }
        })
    }

}