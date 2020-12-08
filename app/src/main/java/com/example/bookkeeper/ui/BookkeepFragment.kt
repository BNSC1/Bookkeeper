package com.example.bookkeeper.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.bookkeeper.R
import com.example.bookkeeper.databinding.FragmentBookkeepBinding
import com.example.bookkeeper.model.Entry
import com.example.bookkeeper.model.EntryVM
import com.example.bookkeeper.utils.ListAdapter
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.time.ZoneOffset
import java.util.*
import kotlin.collections.ArrayList

private const val TAG = "BookkeepFragment"

class BookkeepFragment : Fragment() {
    private var timezone = TimeZone.getDefault()
    private lateinit var cal: Calendar

    private var _binding: FragmentBookkeepBinding? = null
    private val binding get() = _binding!!
    private lateinit var datePicker: MaterialDatePicker<*>

    private lateinit var entryVM: EntryVM

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dateBuilder: MaterialDatePicker.Builder<*> = MaterialDatePicker.Builder.datePicker()
        val today: Long = MaterialDatePicker.todayInUtcMilliseconds()
        val constraints = CalendarConstraints.Builder()
            .setOpenAt(today)
            .build()
        dateBuilder.setCalendarConstraints(constraints)
        dateBuilder.setTitleText("Select a Date")
        datePicker = dateBuilder.build()

        _binding = FragmentBookkeepBinding.inflate(inflater, container, false)
        entryVM = ViewModelProviders.of(this).get(EntryVM::class.java)
        val adapter = ListAdapter(entryVM)
        entryVM.readAllEntry.observe(viewLifecycleOwner, { entry ->
            adapter.setData(entry)
        })
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

        val spinnerList: ArrayList<String> = ArrayList()
        spinnerList.add(getString(R.string.item_expense))
        spinnerList.add(getString(R.string.item_revenue))
        val spinnerAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            spinnerList
        )
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        datePicker.addOnPositiveButtonClickListener {
            binding.dateTV.text = datePicker.headerText
        }
        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.revenueRB) {
                binding.description.hint = getString(R.string.hint_revenue_description)
            } else {
                binding.description.hint = getString(R.string.hint_expense_description)
            }
        }

        binding.confirmBTN.setOnClickListener {
            if (binding.amountET.text!!.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.err_fill_fields),
                    Toast.LENGTH_SHORT
                ).show()
                if (binding.amountET.text.isNullOrEmpty()) binding.amountET.error =
                    getString(R.string.err_required)
            } else {
                val amount = binding.amountET.text.toString().toInt()
                val description = binding.descriptionET.text.toString()
                Log.d(TAG, "" + cal.toInstant().atOffset(ZoneOffset.UTC))
                entryVM.insertEntry(
                    Entry(
                        0,
                        description,
                        amount,
                        cal.toInstant().atOffset(ZoneOffset.UTC)
                    )
                )
                Log.d(TAG, "INSERT ENTRY")
                Toast.makeText(
                    requireContext(),
                    getString(R.string.msg_entry_successful),
                    Toast.LENGTH_SHORT
                ).show()
                cleanFields()
            }
        }

        binding.nowTV.setOnClickListener {
            setCurrentTime()
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

}