package com.example.tangestate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.serialization.json.Json

fun createJson() = Json {
    isLenient = true
    ignoreUnknownKeys = true
    useAlternativeNames = false
}

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recommendationsFragment : Fragment = RecommendationsFragment()
        val searchFragment : Fragment = SearchFragment()
        val favoriteFragment : Fragment = FavoriteFragment()
        val profileFragment : Fragment = ProfileFragment()

        val bottomNavigation : BottomNavigationView = findViewById(R.id.bottom_navigation)

        bottomNavigation.setOnItemSelectedListener { item ->
            lateinit var fragment : Fragment
            when(item.itemId) {
                R.id.nav_suggestions -> fragment = recommendationsFragment
                R.id.nav_browse -> fragment = searchFragment
                R.id.nav_favorites -> fragment = favoriteFragment
                R.id.nav_profile -> fragment = profileFragment
            }
            replaceFragment(fragment)
            true
        }
        bottomNavigation.selectedItemId = R.id.nav_browse
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout,fragment)
        fragmentTransaction.commit()
    }
}