package com.dicoding.asclepius.view

import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedDispatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.dicoding.asclepius.R
import com.dicoding.asclepius.data.Classifications
import com.dicoding.asclepius.databinding.ActivityResultBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding
    private lateinit var backPressedDispatcher: OnBackPressedDispatcher
    private lateinit var imageUri: String
    private lateinit var result: String
    private var confidenceScore: Float = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)


        imageUri = intent.getStringExtra(EXTRA_IMAGE_URI) ?: ""
        result = intent.getStringExtra(EXTRA_RESULT) ?: ""
        confidenceScore = intent.getFloatExtra(EXTRA_CONFIDENCE_SCORE, 0f)



        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        backPressedDispatcher = onBackPressedDispatcher

        val classifications = intent.getParcelableArrayListExtra<Classifications>(EXTRA_CLASSIFICATIONS)
        val imageUriString = intent.getStringExtra(EXTRA_IMAGE_URI)

        imageUriString?.let {
            val imageUri = Uri.parse(it)
            binding.resultImage.setImageURI(imageUri)
        }

        classifications?.let {
            val resultTextView: TextView = findViewById(R.id.result_text)
            val isCancer = classifications[0].category == "Cancer"
            val confidenceScore = classifications[0].score

            val resultMessage = if (isCancer) {
                "Gambar yang diambil masuk dalam penyakit kanker."
            } else {
                "Gambar yang diambil tidak masuk dalam penyakit kanker."
            }

            val confidenceText = "Confidence Score: $confidenceScore"
            resultTextView.text = "$resultMessage\n$confidenceText"
        } ?: showToast("Failed to get classification results")
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                backPressedDispatcher.onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }



    companion object {
        const val EXTRA_CLASSIFICATIONS = "extra_classifications"
        const val EXTRA_IMAGE_URI = "extra_image_uri"
        const val EXTRA_RESULT = "extra_result"
        const val EXTRA_CONFIDENCE_SCORE = "extra_confidence_score"
    }
}
