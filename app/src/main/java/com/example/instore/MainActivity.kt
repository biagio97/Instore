package com.example.instore

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import models.Database
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.*
import androidx.navigation.ui.*
import com.example.instore.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    val queryListener = QueryListener()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Database.getElencoProdotti()
        binding = DataBindingUtil.setContentView<ActivityMainBinding>(this,R.layout.activity_main)
        drawerLayout = binding.drawerLayout
        navController = findNavController(R.id.navHostFragment)
        appBarConfiguration = AppBarConfiguration(navController.graph,drawerLayout)
        NavigationUI.setupWithNavController(binding.navView,navController)
        NavigationUI.setupActionBarWithNavController(this,navController,appBarConfiguration)
        binding.navView.setNavigationItemSelectedListener {
            goToClothes(it.title as String)
        }
        Log.i("MainActivity","Create Main Activity")

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        when(navController.currentDestination?.id){
            R.id.homeFragment,R.id.clothesFragment -> {
                menuInflater.inflate(R.menu.cart_menu,menu)
                (menu?.findItem(R.id.searchItem)?.actionView as SearchView).apply {
                setOnSearchClickListener{
                    Log.i("HomeFragment","search Pressed")
                }
                setOnQueryTextListener(queryListener)
                }
            }
        }
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.cartItem -> navController.navigate(R.id.cartFragment)
        }
        return super.onOptionsItemSelected(item)
    }

    fun goToClothes(categoria: String): Boolean{
        drawerLayout.closeDrawer(binding.navView)
        val bundle = Bundle()
        bundle.putString("categoria",categoria)
        navController.navigate(R.id.clothesFragment,bundle, NavOptions.Builder().setPopUpTo(R.id.homeFragment,false).build())
        return true
    }


    override fun onSupportNavigateUp(): Boolean {
        drawerLayout.closeDrawer(binding.navView)
        return super.onSupportNavigateUp() || navController.navigateUp(drawerLayout)
    }

    override fun onBackPressed() {
        drawerLayout.closeDrawer(binding.navView)
        super.onBackPressed()
    }
}
