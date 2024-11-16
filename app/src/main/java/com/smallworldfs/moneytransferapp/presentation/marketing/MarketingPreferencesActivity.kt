package com.smallworldfs.moneytransferapp.presentation.marketing

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.smallworldfs.moneytransferapp.modules.common.presentation.ui.GenericActivity
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MarketingPreferencesActivity : GenericActivity() {

    companion object {
        const val TITLE: String = "TITLE"
        const val FROM: String = "FROM"
    }

    private val viewModel: MarketingPreferencesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MarketingPreferenceLayout(
                title = intent?.getStringExtra(TITLE) ?: STRING_EMPTY,
                from = intent?.getStringExtra(FROM) ?: STRING_EMPTY,
                onBackPressed = { onBackPressedDispatcher.onBackPressed() },
                finish = { finish() }
            )
        }
        viewModel.requestPreferences(intent?.getStringExtra(FROM) ?: STRING_EMPTY)
    }
}
