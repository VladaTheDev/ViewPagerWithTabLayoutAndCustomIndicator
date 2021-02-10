package com.vladadev.viewpagerwithtablayoutandcustomindicator

import android.os.Bundle
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.vladadev.viewpagerwithtablayoutandcustomindicator.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

private const val FIRST_PAGE_INDEX = 0
private const val LAST_PAGE_INDEX = 2

class MainActivity : AppCompatActivity() {

    private var currentPageIndex = 0

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = OnBoardingAdapter(this)
        binding.container.adapter = adapter
        binding.container.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                if (position == LAST_PAGE_INDEX && currentPageIndex == LAST_PAGE_INDEX) {
                    finish()
                }
                currentPageIndex = position
            }

            override fun onPageSelected(position: Int) {
                for (i in FIRST_PAGE_INDEX..LAST_PAGE_INDEX) {
                    when {
                        i == position -> changeTabImage(i, R.drawable.ic_indicator_show_now)
                        i < position -> changeTabImage(i, R.drawable.ic_indicator_showed)
                        i > position -> changeTabImage(i, R.drawable.ic_indicator_not_showed)
                    }
                }
            }
        })

        prepareTabLayout()

        binding.bNext.setOnClickListener {
            val nextPageIndex = currentPageIndex + 1
            if (nextPageIndex > LAST_PAGE_INDEX) {
                finish()
            } else {
                GlobalScope.launch(context = Dispatchers.Default) {
                    runOnUiThread {
                        binding.container.setCurrentItem(nextPageIndex, false)
                    }
                }
            }
        }
    }

    /**
     * Method created to link TabLayout and ViewPager and prepare UI of tabs.
     */
    private fun prepareTabLayout() {
        TabLayoutMediator(binding.tabIndicator, binding.container) { _, _ ->
        }.attach()
        for (i in FIRST_PAGE_INDEX..LAST_PAGE_INDEX) {
            binding.tabIndicator.getTabAt(i)?.setCustomView(R.layout.layout_tab)
        }
        for (i in FIRST_PAGE_INDEX..LAST_PAGE_INDEX) {
            if (i == FIRST_PAGE_INDEX) {
                changeTabImage(i, R.drawable.ic_indicator_show_now)
            } else {
                changeTabImage(i, R.drawable.ic_indicator_not_showed)
            }
        }
    }

    /**
     * Method created to setup image to ImageView which contains at TabLayout.
     * @param tabIndex Int index of tab.
     * @param image Int drawable resource to setup to ImageView.
     */
    private fun changeTabImage(tabIndex: Int, @DrawableRes image: Int) {
        val tab = binding.tabIndicator.getTabAt(tabIndex)
        tab?.apply {
            customView?.apply {
                val imageView = findViewById<ImageView>(R.id.indicator)
                imageView.setBackgroundResource(image)
            }
        }
    }

    /**
     * Override onDestroy method to remove reference to [_binding].
     */
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
