package com.example.bookkeeper.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.bookkeeper.R
import com.example.bookkeeper.databinding.FragmentBookkeepBinding
import com.example.bookkeeper.model.Entry
import com.example.bookkeeper.model.EntryVM
import java.text.SimpleDateFormat
import java.time.OffsetDateTime
import java.util.*


class BookkeepFragment : Fragment() {
    private var revenueFlag = -1
    private var timezone = TimeZone.getDefault()
    private lateinit var cal: Calendar

    private var _binding: FragmentBookkeepBinding? = null
    private val binding get() = _binding!!

    private lateinit var entryVM: EntryVM

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        entryVM = ViewModelProvider(this).get(EntryVM::class.java)
        _binding = FragmentBookkeepBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setCurrentTime()

        binding.dateTV.setOnClickListener {
            val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, month)
                cal.set(Calendar.DAY_OF_MONTH, day)
                binding.dateTV.text = SimpleDateFormat("yyyy/M/dd", Locale.TAIWAN).format(cal.time)
            }
            val date = DatePickerDialog(
                requireContext(),
                dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            )
            date.show()
        }

        binding.timeTV.setOnClickListener {
            val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                binding.timeTV.text = SimpleDateFormat("HH:mm", Locale.TAIWAN).format(cal.time)
            }
            val time = TimePickerDialog(
                requireContext(),
                timeSetListener,
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                true
            )
            time.show()
        }

        binding.nowTV.setOnClickListener {
            setCurrentTime()
        }

        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.revenueRB) {
                binding.description.hint = getString(R.string.hint_revenue_description)
                revenueFlag = 1
            } else {
                binding.description.hint = getString(R.string.hint_expense_description)
                revenueFlag = -1
            }
        }

        binding.confirmBTN.setOnClickListener {
            if (binding.amountET.text!!.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.msg_entry_unsuccessful),
                    Toast.LENGTH_SHORT
                ).show()
                if (binding.amountET.text.isNullOrEmpty()) binding.amountET.error =
                    getString(R.string.err_invalid)
            } else {
                val amount = revenueFlag * binding.amountET.text.toString().toDouble()
                val description = binding.descriptionET.text.toString()
                entryVM.insertEntry(
                    Entry(
                        0,
                        description,
                        amount,
                        cal.toInstant().atOffset(OffsetDateTime.now().offset)
                    )
                )
                Toast.makeText(
                    requireContext(),
                    getString(R.string.msg_entry_successful),
                    Toast.LENGTH_SHORT
                ).show()
                cleanFields()
            }
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        timezone = TimeZone.getDefault()
    }

    private fun cleanFields() {
        binding.descriptionET.setText("")
        binding.amountET.setText("")
    }

    private fun setCurrentTime() {
        cal = Calendar.getInstance()
        binding.dateTV.text = SimpleDateFormat("yyyy/M/dd", Locale.TAIWAN).format(cal.time)
        binding.timeTV.text = SimpleDateFormat("HH:mm", Locale.TAIWAN).format(cal.time)
    }

    companion object {
//        private const val TAG = "BookkeepFragment"
    }

}