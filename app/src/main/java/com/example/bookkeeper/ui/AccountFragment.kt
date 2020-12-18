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
import com.example.bookkeeper.utils.MoneyFormatHelper
import kotlinx.android.synthetic.main.fragment_account.view.*

class AccountFragment : Fragment() {
    private lateinit var entryVM: EntryVM
    private lateinit var moneyFormatHelper: MoneyFormatHelper
    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        entryVM = ViewModelProvider(this).get(EntryVM::class.java)
        moneyFormatHelper = MoneyFormatHelper(requireContext())
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun refreshBalance() {
        entryVM.readBalance.observe(viewLifecycleOwner, { balance ->
            binding.balanceTV.text =
                getString(
                    R.string.msg_balance,
                    moneyFormatHelper.getPrefMoneyString(balance)
                )
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

    override fun onResume() {
        super.onResume()
        refreshEntries()
        refreshBalance()
    }

    fun refreshEntries() {
        val adapter = ListAdapter(entryVM)
        val recyclerView = requireView().recyclerview
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        entryVM.readAllEntry.observe(viewLifecycleOwner, { entry ->
            adapter.setData(entry)
        })
    }
}