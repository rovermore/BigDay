package com.smallworldfs.moneytransferapp.presentation.transferdetails

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.SmallWorldApplication.Companion.getStr
import com.smallworldfs.moneytransferapp.modules.beneficiary.domain.model.Transaction
import com.smallworldfs.moneytransferapp.modules.common.presentation.ui.GenericActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TransferDetailActivity : GenericActivity() {

    companion object {
        const val TRANSACTION_EXTRA = "TRANSACTION_EXTRA"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val transactionData = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(TRANSACTION_EXTRA, Transaction::class.java) ?: Transaction()
        } else {
            intent.getParcelableExtra(TRANSACTION_EXTRA) ?: Transaction()
        }

        setContent {
            TransferDetailsLayout(
                transferData = transactionData,
                onClipboardClick = { data -> copyDataToClipboard(data) },
                onBackPressed = { finish() }
            )
        }
    }

    private fun copyDataToClipboard(data: String) {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(getStr(R.string.text_copied_to_clipboard), data)
        clipboard.setPrimaryClip(clip)
    }
}
