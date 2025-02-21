package com.wildan.newsapp.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.wildan.newsapp.R
import com.wildan.newsapp.databinding.ActivityMainBinding
import com.wildan.newsapp.utils.ViewBindingExt.viewBinding

class MainActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityMainBinding::inflate)
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        val navController = getNavController()
        navController?.let { controller ->
            appBarConfiguration = AppBarConfiguration(controller.graph)
            setupActionBarWithNavController(controller, appBarConfiguration)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.bookmark_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_show_bookmark -> {
                startActivity(Intent(this, BookmarksActivity::class.java))
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun getNavController(): NavController? {
        val fragment: Fragment? =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_home)
        if (fragment !is NavHostFragment) {
            throw IllegalStateException(
                "Activity " + this
                        + " does not have a NavHostFragment"
            )
        }
        return (fragment as NavHostFragment?)?.navController
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_activity_home)
        return appBarConfiguration.let { navController.navigateUp(it) } || super.onSupportNavigateUp()
    }
}