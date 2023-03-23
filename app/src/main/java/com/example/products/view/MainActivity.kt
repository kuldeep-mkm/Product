package com.example.products.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.products.R
import com.example.products.adapter.ProductAdapter
import com.example.products.adapter.ProductGridAdapter
import com.example.products.databinding.ActivityMainBinding
import com.example.products.model.ProductsItem
import com.example.products.reciver.ConnectionReceiver
import com.example.products.viewmodel.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), ConnectionReceiver.ReceiverListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ProductAdapter
    private lateinit var adapter1: ProductGridAdapter
    private var productList = mutableListOf<ProductsItem?>()
    private lateinit var mNetworkReceiver : ConnectionReceiver
    private val viewModel: ProductViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)


        mNetworkReceiver = ConnectionReceiver()
        registerNetworkBroadcastForNougat()

        checkConnection()
//        if (Constant.isInternetConnected(this@MainActivity)) {
//            viewModel.getProductViewModel()
//        } else {
//            return
//        }
        binding.imgGridList.setOnClickListener{
            binding.imgGridList.visibility=View.INVISIBLE
            binding.imgLinearList.visibility=View.VISIBLE
            val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(this@MainActivity,2)
            binding.recyclerView.layoutManager = layoutManager

            binding.recyclerView.adapter = adapter1
        }

        binding.imgLinearList.setOnClickListener{
            binding.imgGridList.visibility=View.VISIBLE
            binding.imgLinearList.visibility=View.INVISIBLE
            val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this@MainActivity)
            binding.recyclerView.layoutManager = layoutManager
            binding.recyclerView.adapter = adapter
        }

        binding.add.setOnClickListener {

            val intent = Intent(this, AddProductActivity::class.java)
           // getResult.launch(intent)
            startActivityForResult(intent, 1001)

        }

        adapter1 = ProductGridAdapter()

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this@MainActivity)
        binding.recyclerView.layoutManager = layoutManager
        adapter = ProductAdapter()
        binding.recyclerView.adapter = adapter

        lifecycleScope.launchWhenStarted {
            viewModel.conversion.collect { value: ProductViewModel.ProductEvent ->
                when (value) {
                    is ProductViewModel.ProductEvent.Success -> {
                        productList= value.result as MutableList<ProductsItem?>
                        binding.progressBar.visibility = View.GONE
                        adapter.setProductList(value.result as List<ProductsItem>)
                        adapter1.setProductList(value.result as List<ProductsItem>)
                    }
                    is ProductViewModel.ProductEvent.Failure -> {
                        binding.progressBar.visibility = View.GONE
                    }
                    is ProductViewModel.ProductEvent.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    else -> Unit
                }
            }
        }
    }
    private val getResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {

            val modelData: ProductsItem = it!!.data?.getParcelableExtra<ProductsItem>("test")!!
            adapter.setProductData(modelData)
            adapter1.setProductData(modelData)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1001 && resultCode == RESULT_OK) {
           val modelData: ProductsItem = data!!.getParcelableExtra<ProductsItem>("test")!!
            adapter.setProductData(modelData)
            adapter1.setProductData(modelData)
        }
    }

    fun checkConnection() {
        val intentFilter = IntentFilter()
        intentFilter.addAction("android.new.conn.CONNECTIVITY_CHANGE")
        registerReceiver(ConnectionReceiver(), intentFilter)
        ConnectionReceiver().Listener = this@MainActivity
        val manager : ConnectivityManager  = applicationContext.getSystemService(
            Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = manager.activeNetworkInfo
         var isConnected : Boolean = networkInfo != null && networkInfo.isConnectedOrConnecting
        ShowData(isConnected)
    }

    private fun ShowData(connected: Boolean) {
        if (connected) {
            viewModel.getProductViewModel()
        } else {
               Toast.makeText(this@MainActivity, "No Internet Available", Toast.LENGTH_SHORT).show()
            return
        }
    }

    override fun onNetworkChange(isConnected: Boolean) {
        ShowData(isConnected)
    }

    override fun onResume() {
        super.onResume()
//        checkConnection()
    }

    private fun registerNetworkBroadcastForNougat() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            registerReceiver(
                mNetworkReceiver,
                IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
            )
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            registerReceiver(
                mNetworkReceiver,
                IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
            )
        }
    }

    protected fun unregisterNetworkChanges() {
        try {
            unregisterReceiver(mNetworkReceiver)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterNetworkChanges()
    }

}