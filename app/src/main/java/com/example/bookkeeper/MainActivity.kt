package com.example.bookkeeper

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
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
                    tab.text = "Bookkeep"
                    tab.setIcon(R.drawable.ic_edit)
                }
                1 -> {
                    tab.text = "Account"
                    tab.setIcon(R.drawable.ic_list)
                }
                else -> {
                    tab.text = "Backup"
                    tab.setIcon(R.drawable.ic_backup)
                }
            }
        }
        tabLayoutMediator.attach()
    }

}