package com.dicoding.asclepius.view

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import com.dicoding.asclepius.R
import com.dicoding.asclepius.databinding.ActivityMainBinding
import com.dicoding.asclepius.helper.ImageClassifierHelper
import com.yalantis.ucrop.UCrop
import org.tensorflow.lite.task.vision.classifier.Classifications
import java.io.File

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private var currentImageUri: Uri? = null

    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            currentImageUri = uri
            val outputUri = File(cacheDir, "cropped_image.jpg").toUri()
            val uCrop = UCrop.of(uri, outputUri)
                .withAspectRatio(5f, 5f)
                .withMaxResultSize(800, 800)
            cropImage.launch(uCrop.getIntent(this@MainActivity))
        } ?: showToast("Failed to get image URI")
    }

    private val cropImage = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val resultUri = UCrop.getOutput(result.data!!)
            resultUri?.let {
                binding.previewImageView.setImageURI(it)
                analyzeImage(it)
            } ?: showToast("Failed to crop image")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.galleryButton.setOnClickListener {
            startGallery()
        }

        binding.analyzeButton.setOnClickListener {
            currentImageUri?.let {
                analyzeImage(it)
            } ?: showToast("No image selected")


        }
    }

    private fun startGallery() {
        getContent.launch("image/*")
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun analyzeImage(imageUri: Uri) {
        val classifier = ImageClassifierHelper(context = this, classifierListener = object : ImageClassifierHelper.ClassifierListener {
            override fun onError(error: String) {
                showToast(error)
            }

            override fun onResults(results: List<Classifications>?, inferenceTime: Long) {
                val resultData = results?.map {
                    com.dicoding.asclepius.data.Classifications(it.categories[0].label, it.categories[0].score)
                }

                val intent = Intent(this@MainActivity, ResultActivity::class.java)
                intent.putParcelableArrayListExtra(ResultActivity.EXTRA_CLASSIFICATIONS, ArrayList(resultData))
                intent.putExtra(ResultActivity.EXTRA_IMAGE_URI, imageUri.toString())
                startActivity(intent)
            }
        })

        classifier.classifyImage(imageUri)
    }



}
