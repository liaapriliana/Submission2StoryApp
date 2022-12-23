package com.intermediate.storyapp1.ui.detail.createStory

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.intermediate.storyapp1.R
import com.intermediate.storyapp1.data.constant.Constant
import com.intermediate.storyapp1.data.utils.createCustomTempFile
import com.intermediate.storyapp1.data.utils.uriToFile
import com.intermediate.storyapp1.databinding.ActivityCreateStoryBinding
import com.intermediate.storyapp1.ui.customView.CustomAlertDialog
import com.intermediate.storyapp1.utils.ViewModelFactory
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import com.intermediate.storyapp1.data.presenter.Result
import com.intermediate.storyapp1.data.utils.reduceFileImage
import okhttp3.RequestBody.Companion.asRequestBody

class CreateStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateStoryBinding
    private lateinit var currentPhotoPath: String
    private var getFile: File? = null
    private lateinit var factory: ViewModelFactory
    private val createStoryViewModel: CreateStoryViewmodel by viewModels{ factory }
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var latitude: Double? = null
    private var longitude: Double? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        setupViewModel()
        setupToolbar()
        getMyLastLocation()
        buttonGalleryHandler()
        buttonCameraHandler()
        buttonSubmitStoryHandler()
    }

    private fun setupViewModel() {
        factory = ViewModelFactory.getInstance(binding.root.context)
    }

    private fun setupToolbar() {
        title = resources.getString(R.string.create_story)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return true
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                    getMyLastLocation()
                }
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                    getMyLastLocation()
                }
                permissions[Manifest.permission.CAMERA] ?: false -> {}
                permissions[Manifest.permission.READ_EXTERNAL_STORAGE] ?: false -> {}
                else -> {
                    Toast.makeText(this@CreateStoryActivity, R.string.permission_denied, Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun getMyLastLocation() {
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION) &&
            checkPermission(Manifest.permission.CAMERA) &&
            checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    latitude = location.latitude
                    longitude = location.longitude
                } else {
                    Toast.makeText(
                        this@CreateStoryActivity,
                        getString(R.string.no_location),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
                )
            )
        }
    }

    private fun buttonGalleryHandler() {
        binding.createStoryLayout.galleryButton.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            val chooser = Intent.createChooser(intent, "Choose a Picture")
            launcherIntentGallery.launch(chooser)
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)
            getFile = myFile
            val result = BitmapFactory.decodeFile(myFile.path)

            binding.createStoryLayout.imagePickerView.setImageBitmap(result)
        }
    }

    private fun buttonCameraHandler() {
        binding.createStoryLayout.cameraButton.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.resolveActivity(packageManager)

            createCustomTempFile(applicationContext).also {
                val photoURI: Uri = FileProvider.getUriForFile(
                    this@CreateStoryActivity,
                    "com.intermediate.storyapp1.mycamera",
                    it
                )
                currentPhotoPath = it.absolutePath
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                launcherIntentCamera.launch(intent)
            }
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this@CreateStoryActivity)
            getFile = myFile
            binding.createStoryLayout.imagePickerView.setImageURI(selectedImg)
        }
    }

    private fun buttonSubmitStoryHandler() {
        binding.createStoryLayout.submitStoryButton.setOnClickListener {
            val description = binding.createStoryLayout.descriptionEditText.text.toString()
            if (!TextUtils.isEmpty(description) && getFile != null && latitude != null && longitude != null) {
                createStory(description)
            } else {
                CustomAlertDialog(this, R.string.error_validation, R.drawable.error_form).show()
            }
        }
    }

    private fun convertImage(): MultipartBody.Part {
        val file = reduceFileImage(getFile as File)
        val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())

        return MultipartBody.Part.createFormData(
            "photo",
            file.name,
            requestImageFile
        )
    }

    private fun convertDescription(description: String): RequestBody {
        return description.toRequestBody("text/plain".toMediaType())
    }

    private fun createStory(description: String) {
        val image = convertImage()
        val desc = convertDescription(description)
        createStoryViewModel.postCreateStory(
            image,
            desc,
            latitude!!,
            longitude!!
        ).observe(this@CreateStoryActivity) { result ->
            if (result != null) {
                when(result) {
                    is Result.Loading -> {
                        loadingHandler(true)
                    }
                    is Result.Error -> {
                        loadingHandler(false)
                        errorHandler()
                    }
                    is Result.Success -> {
                        successHandler()
                    }
                }
            }
        }
    }

    private fun loadingHandler(isLoading: Boolean) {
        if (isLoading) {
            binding.loadingLayout.root.visibility = View.VISIBLE
            binding.createStoryLayout.root.visibility = View.GONE
        } else {
            binding.loadingLayout.root.visibility = View.GONE
            binding.createStoryLayout.root.visibility = View.VISIBLE
        }
    }

    private fun errorHandler() {
        CustomAlertDialog(this, R.string.error_message, R.drawable.tryagain).show()
    }

    private fun successHandler() {
        CustomAlertDialog(
            this,
            R.string.success_create_story,
            R.drawable.story_created,
        ).show()
        binding.createStoryLayout.imagePickerView.setImageResource(R.drawable.choose_photo)
        binding.createStoryLayout.descriptionEditText.text?.clear()
        finish()
    }


    }
