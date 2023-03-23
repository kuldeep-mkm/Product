package com.example.products.view

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.HorizontalScrollView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.products.R
import com.example.products.adapter.ImageAdapter
import com.example.products.adapter.ProductAdapter
import com.example.products.comman.Constant.Companion.PERMISSION_WRITE_EXTERNAL_STORAGE
import com.example.products.comman.Constant.Companion.REQUEST_CODE_CAMERA
import com.example.products.comman.Constant.Companion.REQUEST_CODE_GALLERY
import com.example.products.databinding.ActivityAddProductBinding
import com.example.products.model.ProductsItem
import com.example.products.viewmodel.AddProductViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import pl.aprilapps.easyphotopicker.DefaultCallback
import pl.aprilapps.easyphotopicker.EasyImage
import pl.aprilapps.easyphotopicker.EasyImage.ImageSource
import java.io.File


@AndroidEntryPoint
class AddProductActivity : AppCompatActivity() {

    private val viewModel: AddProductViewModel by viewModels()
    private lateinit var binding: ActivityAddProductBinding
    private lateinit var itemProduct: ProductsItem
    private val items = arrayOf("Camera", "Gallery")
    private var filepath: ArrayList<String?>? = ArrayList()
    private lateinit var adapter : ImageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_product)

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this@AddProductActivity,  LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerView.layoutManager = layoutManager
        adapter = ImageAdapter()
        binding.recyclerView.adapter = adapter


        binding.back.setOnClickListener {
            onBackPressed()
        }

        binding.btnAdd.setOnClickListener {
            addData()
        }

        binding.addImages.setOnClickListener {
            if (filepath?.size!! <= 4) {
                PermissionApply()
            }
        }
    }

    private fun PermissionApply() {
        if (ContextCompat.checkSelfPermission(this@AddProductActivity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this@AddProductActivity, Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this@AddProductActivity, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {
            requestStoragePermissions();
        } else {
            openImage();
        }
    }

    private fun openImage() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Options")
        builder.setItems(items) { _, which ->
            if (items[which] == "Camera") {
                EasyImage.openCamera(this@AddProductActivity, REQUEST_CODE_CAMERA)
            } else if (items[which] == "Gallery") {
                EasyImage.openGallery(this@AddProductActivity, REQUEST_CODE_GALLERY)
            }

        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun requestStoragePermissions() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this@AddProductActivity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        ) {
            Snackbar.make(
                binding.addImages,
                "Storage access permissions are required to upload/download files.",
                Snackbar.LENGTH_LONG
            )
                .setAction("Okay") { view: View? ->
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(
                            arrayOf<String>(
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.CAMERA
                            ),
                            PERMISSION_WRITE_EXTERNAL_STORAGE
                        )
                        openImage()
                    }
                }
                .show()
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(
                    arrayOf<String>(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA
                    ),
                    PERMISSION_WRITE_EXTERNAL_STORAGE
                )
                openImage()
            }
        }
    }
    private fun addData(){

        val title = binding.etTitle.text.toString()
        val description = binding.etDescription.text.toString()
        val price = binding.etPrice.text.toString()
        val discount = binding.etDiscount.text.toString()

        if (filepath!!.size <= 0) {
            Toast.makeText(this,"Please Select Image!",Toast.LENGTH_LONG).show()
        } else if(title.isBlank()){
            Toast.makeText(this,"Enter Title!",Toast.LENGTH_LONG).show()
        } else if (description.isBlank()) {
            Toast.makeText(this,"Enter Description!",Toast.LENGTH_LONG).show()
        } else if (price.isBlank()) {
            Toast.makeText(this,"Enter Price!",Toast.LENGTH_LONG).show()
        } else if (discount.isBlank()) {
            Toast.makeText(this,"Enter Discount!",Toast.LENGTH_LONG).show()
        } else {
            val blog = ProductsItem(discount,
                filepath?.get(0).toString(), filepath, price.toInt(), 3.0f, description, 0, title)
//            viewModel.add(blog)
            val returnIntent = Intent()
            returnIntent.putExtra("test", blog)
            setResult(Activity.RESULT_OK, returnIntent)

            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        EasyImage.handleActivityResult(
            requestCode,
            resultCode,
            data,
            this,
            object : DefaultCallback() {
                override fun onImagePicked(imageFile: File, source: ImageSource?, type: Int) {
                    when (type) {
                        REQUEST_CODE_CAMERA -> {

                            filepath?.add(imageFile.absolutePath)

                            adapter.setImageList(filepath!!)
                           /* Glide.with(this@AddProductActivity)
                                .load(imageFile)
                                .centerCrop()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into<Target<Drawable>>(binding.addImages)
                            filepath = imageFile*/
                        }
                        REQUEST_CODE_GALLERY -> try {

                            filepath?.add(imageFile.absolutePath)
                            adapter.setImageList(filepath!!)

                          /*  Glide.with(this@AddProductActivity)
                                .load(imageFile)
                                .centerCrop()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into<Target<Drawable>>(binding.addImages)
                            filepath = imageFile*/
                        } catch (ex: Exception) {
                            Toast.makeText(
                                this@AddProductActivity,
                                ex.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            })
    }
}