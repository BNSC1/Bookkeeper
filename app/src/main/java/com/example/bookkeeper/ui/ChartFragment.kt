package com.example.bookkeeper.ui

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.bookkeeper.R
import com.example.bookkeeper.databinding.FragmentChartBinding
import com.example.bookkeeper.model.EntryVM
import com.example.bookkeeper.utils.MyValueFormatter
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry


class ChartFragment : Fragment() {

    private lateinit var entryVM: EntryVM
    private var _binding: FragmentChartBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        entryVM = ViewModelProvider(this).get(EntryVM::class.java)
        _binding = FragmentChartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val green = requireContext().getColor(R.color.green)
        val red = requireContext().getColor(R.color.red)

        binding.pieChart.setUsePercentValues(false)
        binding.pieChart.description.isEnabled = false
        binding.pieChart.setExtraOffsets(5f, 10f, 5f, 5f)

        binding.pieChart.dragDecelerationFrictionCoef = 0.75f
        // enable rotation of the chart by touch
        binding.pieChart.isRotationEnabled = true
        binding.pieChart.isDrawHoleEnabled = false
        binding.pieChart.legend.isEnabled = false
        // entry label styling
        binding.pieChart.setEntryLabelColor(Color.WHITE)
        binding.pieChart.setEntryLabelTextSize(12f)
        // undo all highlights
        binding.pieChart.highlightValues(null)


        entryVM.readRevenue.observe(viewLifecycleOwner, { revenue ->
            entryVM.readExpense.observe(viewLifecycleOwner, { expense ->
                val pieEntries = ArrayList<PieEntry>()
                pieEntries.add(PieEntry(revenue.toFloat(), getString(R.string.item_revenue)))
                pieEntries.add(PieEntry(expense.toFloat(), getString(R.string.item_expense)))
                val dataSet = PieDataSet(pieEntries, "Revenue/Expense")
                dataSet.setDrawIcons(false)
                dataSet.sliceSpace = 3f
                dataSet.selectionShift = 5f

                val data = PieData(dataSet)
                data.setValueFormatter(MyValueFormatter(requireContext()))
                data.setValueTextSize(11f)
                data.setValueTextColor(Color.WHITE)
                binding.pieChart.data = data
                // add a lot of colors
                val colors = ArrayList<Int>()
                colors.add(green)
                colors.add(red)
                dataSet.colors = colors

                binding.pieChart.invalidate()
            })
        })
    }

}