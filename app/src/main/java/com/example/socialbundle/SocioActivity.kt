package com.example.socialbundle

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.socialbundle.Fragments.ClipsFragment
import com.example.socialbundle.Fragments.HomeFragment
import com.example.socialbundle.Fragments.ProfileFragment
import com.example.socialbundle.Fragments.SearchFragment
import com.example.socialbundle.databinding.ActivitySocioBinding
import com.google.android.material.navigation.NavigationBarView

class SocioActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySocioBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySocioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Disable animation for Bottom Navigation Bar icons
        binding.bottomNavigation.labelVisibilityMode = NavigationBarView.LABEL_VISIBILITY_UNLABELED



        // Set text accessibility off
        binding.bottomNavigation.itemTextAppearanceInactive = 0

        binding.bottomNavigation.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.bottom_home -> {
                    replaceFragment(HomeFragment())
                    true
                }
                R.id.bottom_profile -> {
                    replaceFragment(ProfileFragment())
                    true
                }
                R.id.bottom_search -> {
                    replaceFragment(SearchFragment())
                    true
                }
                R.id.bottom_clips -> {
                    replaceFragment(ClipsFragment())
                    true
                }
                R.id.bottom_add -> {
                    startActivity(Intent(this, PostActivity::class.java))
                    true
                }
                else -> false
            }
        }

        replaceFragment(HomeFragment())
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(binding.frameContainer.id, fragment).commit()
    }
}
