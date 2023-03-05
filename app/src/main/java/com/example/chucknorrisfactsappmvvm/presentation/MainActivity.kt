package com.example.chucknorrisfactsappmvvm.presentation

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.chucknorrisfactsappmvvm.data.FactApp
import com.example.chucknorrisfactsappmvvm.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        val textViewSetup = binding.textViewSetup
        val textViewPunchline = binding.textViewPunchline
        val button = binding.actionButton
        val progressBar = binding.progressBar
        setContentView(binding.root)

        viewModel = (application as FactApp).viewModel

        binding.showFavoriteCheckbox.setOnCheckedChangeListener { _, isChecked ->
            viewModel.chooseFavorite(isChecked)
        }

        binding.favoriteImageButton.setOnClickListener {
            viewModel.changeFactStatus()
        }

        button.setOnClickListener {
            button.isEnabled = false
            binding.progressBar.visibility = View.VISIBLE
            viewModel.getFact()
        }

        viewModel.init(object : FactUiCallback {
            override fun provideText(setup: String, punchline: String) = runOnUiThread {
                button.isEnabled = true
                progressBar.visibility = View.INVISIBLE
                textViewSetup.text = setup
                textViewPunchline.text = punchline
            }

            override fun provideIconResId(iconResId: Int) = runOnUiThread {
                binding.favoriteImageButton.setImageResource(iconResId)
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.clear()
    }
}