package com.smallworldfs.moneytransferapp.modules.changepassword.ui

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.base.presentation.ui.BaseAppCompatActivity
import com.smallworldfs.moneytransferapp.base.presentation.ui.extensions.showPasswordNotChangedErrorDialog
import com.smallworldfs.moneytransferapp.base.presentation.ui.extensions.showSessionExpiredDialog
import com.smallworldfs.moneytransferapp.databinding.ActivityChangePasswordBinding
import com.smallworldfs.moneytransferapp.modules.changepassword.viewmodel.ChangePasswordCommonError
import com.smallworldfs.moneytransferapp.modules.changepassword.viewmodel.ChangePasswordFormLoaded
import com.smallworldfs.moneytransferapp.modules.changepassword.viewmodel.ChangePasswordViewModel
import com.smallworldfs.moneytransferapp.modules.changepassword.viewmodel.Error
import com.smallworldfs.moneytransferapp.modules.changepassword.viewmodel.Loaded
import com.smallworldfs.moneytransferapp.modules.changepassword.viewmodel.Loading
import com.smallworldfs.moneytransferapp.modules.changepassword.viewmodel.NoConnection
import com.smallworldfs.moneytransferapp.modules.changepassword.viewmodel.PasswordChanged
import com.smallworldfs.moneytransferapp.modules.changepassword.viewmodel.PasswordNotChanged
import com.smallworldfs.moneytransferapp.modules.changepassword.viewmodel.SessionExpired
import com.smallworldfs.moneytransferapp.presentation.form.adapter.FormAdapter
import com.smallworldfs.moneytransferapp.presentation.form.adapter.FormNavigator
import com.smallworldfs.moneytransferapp.presentation.quicklogin.PassCodeActivity
import com.smallworldfs.moneytransferapp.utils.forms.Action
import com.smallworldfs.moneytransferapp.utils.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_change_password.*
import kotlinx.android.synthetic.main.error_screen_layout.*
import javax.inject.Inject

@AndroidEntryPoint
class ChangePasswordActivity : BaseAppCompatActivity<ChangePasswordViewModel, ChangePasswordNavigator, ActivityChangePasswordBinding>(ChangePasswordViewModel::class.java) {

    @Inject
    lateinit var formAdapter: FormAdapter

    @Inject
    lateinit var formNavigator: FormNavigator

    override val bindingInflater: (LayoutInflater) -> ActivityChangePasswordBinding = ActivityChangePasswordBinding::inflate

    override fun configureView() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.change_password_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        formAdapter.initViewModel(this)

        form.layoutManager = LinearLayoutManager(this)
        form.adapter = formAdapter

        errorLayoutParent = binding.rootLayout

        retry_button.setOnClickListener {
            initActivity()
        }
    }

    override fun configureViewModel() {
        formAdapter.getEvents().observe(
            this,
            { action ->
                if (action == Action.SEND_FORM) {
                    hideKeyboard()
                    currentFocus?.clearFocus()
                    viewModel.changePassword(formAdapter.getFormData())
                }
            },
        )

        viewModel.getChangePasswordState().observe(
            this,
            Observer { state ->
                when (state) {
                    Loading -> {
                        loading_layout.visibility = View.VISIBLE
                        change_password_content.visibility = View.GONE
                        error_layout.visibility = View.GONE
                    }
                    is Loaded -> {
                        loading_layout.visibility = View.GONE
                        change_password_content.visibility = View.VISIBLE
                        error_layout.visibility = View.GONE
                        when (state.loaded) {
                            is ChangePasswordFormLoaded -> {
                                formAdapter.setFormList(state.loaded.changePasswordForm.form)
                            }
                            PasswordChanged -> {
                                setResult(Activity.RESULT_OK)
                                finish()
                            }
                        }
                    }
                    is Error -> {
                        loading_layout.visibility = View.GONE
                        error_layout.visibility = View.GONE
                        when (state.error) {
                            NoConnection -> {
                                error_layout.visibility = View.VISIBLE
                                change_password_content.visibility = View.GONE
                            }
                            SessionExpired -> {
                                showSessionExpiredDialog { dialog ->

                                    dialog.dismiss()

                                    val intent = Intent(this@ChangePasswordActivity, PassCodeActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                                    startActivity(intent)
                                    finish()
                                }
                                return@Observer
                            }
                            is PasswordNotChanged -> {
                                change_password_content.visibility = View.VISIBLE
                                showPasswordNotChangedErrorDialog(state.error.title, state.error.message)
                            }
                            ChangePasswordCommonError -> {
                                change_password_content.visibility = View.VISIBLE
                                showGenericError("", "", com.smallworldfs.moneytransferapp.base.presentation.viewmodel.Error.UNKNOWN_ERROR)
                            }
                        }
                    }
                }
            },
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> super.onBackPressed()
        }
        return true
    }

    override fun extractIntentData() {
    }

    override fun initActivity() {
        viewModel.initViewModel()
    }
}
