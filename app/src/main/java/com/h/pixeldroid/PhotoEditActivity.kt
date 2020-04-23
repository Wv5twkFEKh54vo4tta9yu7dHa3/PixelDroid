package com.h.pixeldroid

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.h.pixeldroid.adapters.EditPhotoViewPagerAdapter
import com.h.pixeldroid.fragments.EditImageFragment
import com.h.pixeldroid.fragments.FilterListFragment
import com.h.pixeldroid.interfaces.EditImageFragmentListener
import com.h.pixeldroid.interfaces.FilterListFragmentListener
import com.h.pixeldroid.utils.BitmapUtils
import com.h.pixeldroid.utils.NonSwipeableViewPager
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.zomato.photofilters.imageprocessors.Filter
import com.zomato.photofilters.imageprocessors.subfilters.BrightnessSubFilter
import com.zomato.photofilters.imageprocessors.subfilters.ContrastSubFilter
import com.zomato.photofilters.imageprocessors.subfilters.SaturationSubfilter

import kotlinx.android.synthetic.main.activity_photo_edit.*
import kotlinx.android.synthetic.main.content_photo_edit.*

class PhotoEditActivity : AppCompatActivity(), FilterListFragmentListener, EditImageFragmentListener {

    val BITMAP_CONFIG = Bitmap.Config.ARGB_8888

    internal var originalImage: Bitmap? = null
    internal lateinit var filteredImage: Bitmap
    internal lateinit var finalImage: Bitmap

    internal lateinit var filterListFragment: FilterListFragment
    internal lateinit var editImageFragment: EditImageFragment

    lateinit var viewPager: NonSwipeableViewPager
    lateinit var tabLayout: TabLayout

    internal var brightnessFinal = 0
    internal var saturationFinal = 1.0f
    internal var contrastFinal = 1.0f

    object URI {var picture_uri: Uri? = null}

    init {
        System.loadLibrary("NativeImageProcessor")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_edit)

        URI.picture_uri = intent.getParcelableExtra("uri")

        Log.d("edit", URI.picture_uri.toString())

        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Edit"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(true)

        loadImage()

        viewPager = findViewById(R.id.viewPager)
        tabLayout = findViewById(R.id.tabs)
        setupViewPager(viewPager)
        tabLayout.setupWithViewPager(viewPager)
    }

    private fun loadImage() {
        Log.d("edit", "Load image")
        //originalImage = MediaStore.Images.Media.getBitmap(contentResolver, URI.picture_uri)
        originalImage = BitmapUtils.getBitmapFromAssets(applicationContext, "chat.jpg", 800, 800)
        Log.d("edit", "After image")

        filteredImage = originalImage!!.copy(BITMAP_CONFIG, true)
        finalImage = originalImage!!.copy(BITMAP_CONFIG, true)
        image_preview.setImageBitmap(originalImage)
    }

    private fun setupViewPager(viewPager: NonSwipeableViewPager?) {
        val adapter = EditPhotoViewPagerAdapter(supportFragmentManager)

        filterListFragment = FilterListFragment()
        filterListFragment.setListener(this)

        editImageFragment = EditImageFragment()
        editImageFragment.setListener(this)

        adapter.addFragment(filterListFragment, "FILTERS")
        adapter.addFragment(editImageFragment, "EDIT")

        viewPager!!.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.edit_photo_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        when(id) {
            android.R.id.home -> {
                super.onBackPressed()
            }
            R.id.action_upload -> {
                return true
            }
            R.id.action_save -> {
                saveImageToGallery()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun uploadImage(path: String?) {
        val intent = Intent (applicationContext, PhotoEditActivity::class.java)
        intent.putExtra("uri", Uri.parse(path))
        applicationContext!!.startActivity(intent)
    }

    private fun saveImageToGallery() {
        // runtime permission and process
        Dexter.withActivity(this)
            .withPermissions(android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(object:MultiplePermissionsListener{
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    if(report!!.areAllPermissionsGranted()) {
                        val path = MediaStore.Images.Media.insertImage(contentResolver, finalImage, System.currentTimeMillis().toString() + "_profile.jpg", "")
                        if(!TextUtils.isEmpty(path)) {
                            val snackBar = Snackbar.make(coordinator_edit, "Image saved to gallery", Snackbar.LENGTH_LONG)
                                .setAction("OPEN") {openImage(path)}
                            snackBar.show()
                            //uploadImage(path) ----------------
                        } else {
                            val snackBar = Snackbar.make(coordinator_edit, "Unable to save image", Snackbar.LENGTH_LONG)
                            snackBar.show()
                        }
                    } else {
                        Toast.makeText(applicationContext, "Permission denied", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(permissions: MutableList<PermissionRequest>?, token: PermissionToken?) {
                    token!!.continuePermissionRequest()
                }

            }).check()
    }

    private fun openImage(path: String?) {
        val intent = Intent()
        intent.action = Intent.ACTION_VIEW
        intent.setDataAndType(Uri.parse(path), "image/*")
        startActivity((intent))
    }

    override fun onFilterSelected(filter: Filter) {
        resetControls()
        filteredImage = originalImage!!.copy(BITMAP_CONFIG, true)
        image_preview.setImageBitmap(filter.processFilter(filteredImage))
        finalImage = filteredImage.copy(BITMAP_CONFIG, true)
    }

    private fun resetControls() {
        editImageFragment.resetControl()

        brightnessFinal = 0
        saturationFinal = 1.0f
        contrastFinal = 1.0f
    }

    override fun onBrightnessChange(brightness: Int) {
        brightnessFinal = brightness
        val myFilter = Filter()
        myFilter.addSubFilter(BrightnessSubFilter(brightness))
        image_preview.setImageBitmap(myFilter.processFilter(finalImage.copy(BITMAP_CONFIG, true)))
    }

    override fun onSaturationChange(saturation: Float) {
        saturationFinal = saturation
        val myFilter = Filter()
        myFilter.addSubFilter(SaturationSubfilter(saturation))
        image_preview.setImageBitmap(myFilter.processFilter(finalImage.copy(BITMAP_CONFIG, true)))
    }

    override fun onContrastChange(contrast: Float) {
        contrastFinal = contrast
        val myFilter = Filter()
        myFilter.addSubFilter(ContrastSubFilter(contrast))
        image_preview.setImageBitmap(myFilter.processFilter(finalImage.copy(BITMAP_CONFIG, true)))
    }

    override fun onEditStarted() {

    }

    override fun onEditCompleted() {
        val bitmap = filteredImage.copy(BITMAP_CONFIG, true)
        val myFilter = Filter()
        myFilter.addSubFilter(ContrastSubFilter(contrastFinal))
        myFilter.addSubFilter(SaturationSubfilter(saturationFinal))
        myFilter.addSubFilter(BrightnessSubFilter(brightnessFinal))

        finalImage = myFilter.processFilter(bitmap)
    }
}
