package com.smallworldfs.moneytransferapp.presentation.passwordconfirm

import android.os.Bundle
import android.text.TextUtils
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.MenuItem
import androidx.activity.viewModels
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.base.presentation.ui.extensions.showSingleActionInfoDialog
import com.smallworldfs.moneytransferapp.base.presentation.viewmodel.EventObserver
import com.smallworldfs.moneytransferapp.databinding.ActivityPasswordConfirmBinding
import com.smallworldfs.moneytransferapp.modules.common.presentation.ui.GenericActivity
import com.smallworldfs.moneytransferapp.presentation.quicklogin.PassCodeActivity
import com.smallworldfs.moneytransferapp.utils.KeyboardUtils
import com.smallworldfs.moneytransferapp.utils.gone
import com.smallworldfs.moneytransferapp.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_change_password.*
import javax.inject.Inject

@AndroidEntryPoint
class PasswordConfirmActivity : GenericActivity() {

    private val viewModel: PasswordConfirmViewModel by viewModels()

    @Inject
    lateinit var navigator: PasswordConfirmNavigator

    private var _binding: ActivityPasswordConfirmBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityPasswordConfirmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpObservers()
        setupView()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setUpObservers() {
        viewModel.passwordCorrect.observe(
            this,
            EventObserver {
                binding.progressBar.gone()
                binding.submitPassword.visible()
                KeyboardUtils.hideKeyboard(this)
                if (intent.extras?.getString(PassCodeActivity.PASSCODE_ACTIVITY_KEY).toString()
                    == PassCodeActivity.CHANGE_PASSCODE_FROM_SETTINGS
                )
                    navigator.navigateToCreatePasscode()
                else
                    navigator.finishWithSuccess()
            }
        )

        viewModel.passwordError.observe(
            this,
            EventObserver {
                binding.progressBar.gone()
                binding.submitPassword.visible()
                this.showSingleActionInfoDialog(
                    getString(R.string.incorrect_password),
                    getString(R.string.incorrect_password_text),
                    getString(R.string.change),
                    true
                ) {}
            }
        )
    }

    private fun setupView() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.enter_password)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.submitPassword.setOnClickListener {
            viewModel.checkPassword(binding.inputEdit.text.toString().toCharArray())
            binding.submitPassword.gone()
            binding.progressBar.visible()
        }

        binding.showPasswordButton.setOnClickListener {
            showHidePassword()
        }
    }

    fun showHidePassword() {
        if (binding.inputEdit.transformationMethod is PasswordTransformationMethod && !TextUtils.isEmpty(binding.inputEdit.text)) {
            binding.inputEdit.transformationMethod = HideReturnsTransformationMethod.getInstance()
            binding.inputEdit.text?.let { binding.inputEdit.setSelection(it.length) }
            binding.showPasswordButton.setImageDrawable(resources.getDrawable(R.drawable.login_icn_hidepassword))
        } else {
            binding.inputEdit.transformationMethod = PasswordTransformationMethod.getInstance()
            binding.inputEdit.text?.let { binding.inputEdit.setSelection(it.length) }
            binding.showPasswordButton.setImageDrawable(resources.getDrawable(R.drawable.login_icn_showpassword))
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> super.onBackPressed()
        }
        return true
    }
}
