package com.example.bookkeeper

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_menu_example, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val viewPager2 = findViewById<ViewPager2>(R.id.viewPager)
        viewPager2.adapter = mPagerAdapter(this)
        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        val tabLayoutMediator = TabLayoutMediator(
            tabLayout, viewPager2
        ) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = getString(R.string.bookkeep_tab)
                    tab.setIcon(R.drawable.ic_edit)
                }
                1 -> {
                    tab.text = getString(R.string.account_tab)
                    tab.setIcon(R.drawable.ic_list)
                }
                else -> {
                    tab.text = getString(R.string.chart_tab)
                    tab.setIcon(R.drawable.ic_chart)
                }
            }
        }
        tabLayoutMediator.attach()
    }

}