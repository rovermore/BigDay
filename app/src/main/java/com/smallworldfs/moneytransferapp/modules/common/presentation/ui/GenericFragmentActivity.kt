package com.smallworldfs.moneytransferapp.modules.common.presentation.ui

import android.app.Dialog
import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.domain.migrated.userdata.repository.UserDataRepository
import com.smallworldfs.moneytransferapp.modules.common.presentation.presenter.GenericFragmentPresenterImpl
import com.smallworldfs.moneytransferapp.modules.home.presentation.navigator.HomeNavigator
import com.smallworldfs.moneytransferapp.modules.login.domain.repository.LoginRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Created by luismiguel on 3/5/17.
 */
@AndroidEntryPoint
class GenericFragmentActivity : FragmentActivity() {
    var mErrorViewShowing = false
    private var mSessionExpiredDialog: Dialog? = null
    var mPresenter: GenericFragmentPresenterImpl? = null

    @Inject
    lateinit var loginRepository: LoginRepository

    var userDataRepository: UserDataRepository? = null @Inject set

    override fun onDestroy() {
        super.onDestroy()
        if (mSessionExpiredDialog != null) {
            mSessionExpiredDialog!!.dismiss()
        }
        mSessionExpiredDialog = null
    }

    /**
     * Show Common error view
     *
     * @param text
     * @param subtitle
     * @param mErrorView
     */
    protected fun showErrorView(text: String?, subtitle: String, mErrorView: View) {
        mErrorViewShowing = true
        mErrorView.visibility = View.VISIBLE
        (mErrorView.findViewById<View>(R.id.error_title) as TextView).text = text
        (mErrorView.findViewById<View>(R.id.error_subtitle) as TextView).text = subtitle
        mErrorView.translationY = -mErrorView.height.toFloat()
        mErrorView.animate().translationYBy(mErrorView.height.toFloat()).setDuration(300).start()

        // Set click listener to close view
        mErrorView.findViewById<View>(R.id.close_error).setOnClickListener { mErrorView.animate().translationYBy(-mErrorView.height.toFloat()).duration = 300 }

        // Clickable text error
        if (loginRepository.user != null) {
            val textViewSubtitle = mErrorView.findViewById<TextView>(R.id.error_subtitle)
            if (subtitle.contains(getString(R.string.generic_error_view_action_subtitle))) {
                setClickableString(getString(R.string.generic_error_view_action_subtitle), subtitle, textViewSubtitle)
            }
        } else {
            // If contains contact support string - replace by generic
            if (subtitle.contains(getString(R.string.generic_error_view_action_subtitle))) {
                (mErrorView.findViewById<View>(R.id.error_subtitle) as TextView).text = getString(R.string.generic_subtitle_error)
            }
        }
    }

    // Redirect to Contact Customer Support
    private fun setClickableString(clickableValue: String, wholeValue: String, textView: TextView) {
        val spannableString = SpannableString(wholeValue)
        val startIndex = wholeValue.indexOf(clickableValue)
        val endIndex = startIndex + clickableValue.length
        spannableString.setSpan(
            object : ClickableSpan() {
                override fun onClick(widget: View) {
                    HomeNavigator.navigateToContactSupportActivity(this@GenericFragmentActivity)
                }
            },
            startIndex,
            endIndex,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        textView.setLinkTextColor(Color.WHITE)
        textView.text = spannableString
        textView.movementMethod = LinkMovementMethod.getInstance() // <-- important, onClick in ClickableSpan won't work without this
    }

    /**
     * @param errorView
     */
    protected fun showGenericErrorView(errorView: View) {
        showErrorView(getString(R.string.generic_error_view_text), getString(R.string.generic_error_view_subtitle), errorView)
    }

    /**
     * Hide error view
     *
     * @param mErrorView
     */
    protected fun hideErrorView(mErrorView: View) {
        mErrorViewShowing = false
        if (mErrorView.visibility == View.VISIBLE) {
            mErrorView.animate().translationYBy(-mErrorView.height.toFloat()).duration = 300
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
