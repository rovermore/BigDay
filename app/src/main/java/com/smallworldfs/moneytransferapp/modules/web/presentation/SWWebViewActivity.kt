package com.smallworldfs.moneytransferapp.modules.web.presentation

import android.os.Bundle
import android.view.MenuItem
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.databinding.ActivitySwwebviewBinding
import com.smallworldfs.moneytransferapp.modules.common.presentation.ui.GenericActivity

class SWWebViewActivity : GenericActivity() {

    companion object {
        const val FAQS_URL = "FAQS_URL"
    }

    private var _binding: ActivitySwwebviewBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySwwebviewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = getString(R.string.contact_faqs)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        initActivity()
    }

    private fun initActivity() {
        val faqsURL = intent.extras!!.getString(FAQS_URL, "")
        with(binding.webView) {
            settings.javaScriptEnabled = true
            settings.loadWithOverviewMode = true
            settings.useWideViewPort = true
            settings.domStorageEnabled = true
            loadUrl(faqsURL)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> super.onBackPressed()
        }
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
