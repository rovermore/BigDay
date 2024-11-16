package com.smallworldfs.moneytransferapp.presentation.common.countries

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.smallworldfs.moneytransferapp.base.presentation.ui.extensions.hide
import com.smallworldfs.moneytransferapp.databinding.ActivitySearchCountryBinding
import com.smallworldfs.moneytransferapp.modules.common.presentation.ui.GenericActivity
import com.smallworldfs.moneytransferapp.utils.TextChangeCallback
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchStatesActivity : GenericActivity() {

    companion object {
        val SELECTED_STATE_KEY = "SELECTED"
        val TITLE_KEY = "TITLE"
        val STATES_KEY = "STATES"
        val SEPARATOR_INDEX = "SEPARATOR_INDEX"
    }

    private var _binding: ActivitySearchCountryBinding? = null
    private val binding get() = _binding!!

    private val textChangeCallback = object : TextChangeCallback {
        override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
            if (!text.isNullOrBlank()) {
                (binding.recyclerview.adapter as StateSearchAdapter).filterByName(text.toString())
            } else {
                (binding.recyclerview.adapter as StateSearchAdapter).restoreInitialData()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySearchCountryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupViews()
    }

    private fun setupViews() {
        binding.genericToolbarTitle.text = intent.getStringExtra(TITLE_KEY) ?: ""
        binding.recyclerview.apply {
            adapter = StateSearchAdapter(context, intent.getIntExtra(SEPARATOR_INDEX, -1)).apply {
                setOnStateSelectedCallback {
                    setResult(Activity.RESULT_OK)
                    returnResult(it)
                }
                binding.loadingLayout.hide()
                setData(intent.getParcelableArrayListExtra(STATES_KEY) ?: emptyList())
            }
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
        }
        binding.searchEditText.addTextChangedListener(textChangeCallback)
    }

    override fun onDestroy() {
        binding.searchEditText.removeTextChangedListener(textChangeCallback)
        _binding = null
        super.onDestroy()
    }

    private fun returnResult(country: StateUIModel) {
        val intent = Intent().apply {
            putExtra(SELECTED_STATE_KEY, country)
        }
        setResult(RESULT_OK, intent)
        finish()
    }
}
