package com.smallworldfs.moneytransferapp.presentation.account.documents.form

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.databinding.ActivityTipsBinding

class TipsActivity : AppCompatActivity() {

    companion object {
        const val TIP_DATA = "TIP_DATA"
    }

    private lateinit var binding: ActivityTipsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTipsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent?.getStringExtra(TIP_DATA)?.let { setupView(it) }
    }

    private fun setupView(type: String) {
        setSupportActionBar(binding.tipsIdToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.tipsIdToolbar.navigationIcon = resources.getDrawable(R.drawable.ic_action_close_white)
        binding.close.setOnClickListener {
            finish()
        }
        when (type) {
            "FRONT", "BACK" -> {
                supportActionBar?.title = getString(R.string.toolbar_tip_title_id)
                binding.tipTextView.text = getString(R.string.tip_text_id)
            }

            "FACE_VERIFICATION" -> {
                supportActionBar?.title = getString(R.string.toolbar_tip_title_selfie)
                binding.tipTextView.text = getString(R.string.tip_text_tax_selfie)
            }

            "TAX_CODE_DOCUMENT" -> {
                supportActionBar?.title = getString(R.string.toolbar_tip_title_tax_code)
                binding.tipTextView.text = getString(R.string.tip_text_tax_code)
            }

            else -> {
                supportActionBar?.title = getString(R.string.toolbar_tip_title_compliance)
                binding.tipTextView.text = getString(R.string.tip_text_tax_compliance)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
