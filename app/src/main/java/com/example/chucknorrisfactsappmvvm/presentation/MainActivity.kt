package com.example.chucknorrisfactsappmvvm.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.chucknorrisfactsappmvvm.data.FactApp
import com.example.chucknorrisfactsappmvvm.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        val textView = binding.textView
        val button = binding.actionButton
        val progressBar = binding.progressBar
        setContentView(binding.root)

        viewModel = (application as FactApp).viewModel

        binding.showFavoriteCheckbox.setOnCheckedChangeListener { _, isChecked ->
            viewModel.changeFavorite(isChecked)
        }

        binding.favoriteImageButton.setOnClickListener {
            //todo
        }

        button.setOnClickListener {
            button.isEnabled = false
            binding.progressBar.visibility = View.VISIBLE
            viewModel.getFact()
        }

        viewModel.init(object : TextCallback {
            override fun provideText(text: String) = runOnUiThread {
                button.isEnabled = true
                progressBar.visibility = View.INVISIBLE
                textView.text = text
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