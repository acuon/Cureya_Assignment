package dev.acuon.cureya_assignment.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import dev.acuon.cureya_assignment.R
import dev.acuon.cureya_assignment.databinding.ActivityMainBinding
import dev.acuon.cureya_assignment.ui.fragments.FragmentProfile
import dev.acuon.cureya_assignment.ui.fragments.FragmentUsers

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {
//            navigationBar.setOnItemReselectedListener((NavigationBarView.OnItemReselectedListener) listener);
            navigationBar.setOnNavigationItemSelectedListener(navigationListener)
        }
//        supportFragmentManager.beginTransaction()
//            .replace(R.id.frameLayout_for_Fragments, FragmentProfile()).commit()
        openFragment(FragmentProfile())

    }

    private val listener =
        NavigationBarView.OnItemSelectedListener { item ->
            var selectedFragment: Fragment? = null
            when (item.itemId) {
                R.id.nav_profie -> selectedFragment = FragmentProfile()
                R.id.nav_users -> selectedFragment = FragmentUsers()
            }
            supportFragmentManager.beginTransaction().replace(
                R.id.frameLayout_for_Fragments,
                selectedFragment!!
            ).commit()
            true
        }

    private val navigationListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            var selectedFragment: Fragment? = null
            when (item.itemId) {
                R.id.nav_profie -> selectedFragment = FragmentProfile()
                R.id.nav_users -> selectedFragment = FragmentUsers()
            }
//            supportFragmentManager.beginTransaction().replace(
//                R.id.frameLayout_for_Fragments,
//                selectedFragment!!
//            ).commit()
            openFragment(selectedFragment)
            true
        }

    private fun openFragment(fragment: Fragment?) {
        supportFragmentManager.beginTransaction().replace(
            R.id.frameLayout_for_Fragments,
            fragment!!
        ).commit()
    }
}