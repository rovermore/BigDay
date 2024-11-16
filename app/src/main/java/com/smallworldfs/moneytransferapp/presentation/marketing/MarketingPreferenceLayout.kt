package com.smallworldfs.moneytransferapp.presentation.marketing

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.smallworldfs.moneytransferapp.compose.widgets.SWCircularLoader
import com.smallworldfs.moneytransferapp.compose.widgets.SWErrorScreenLayout
import com.smallworldfs.moneytransferapp.compose.widgets.SWText
import com.smallworldfs.moneytransferapp.compose.widgets.SWTopAppBar
import com.smallworldfs.moneytransferapp.compose.widgets.form.SWFormLazyColumn
import com.smallworldfs.moneytransferapp.domain.migrated.base.Action
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.Field
import com.smallworldfs.moneytransferapp.presentation.base.ErrorType

@Composable
fun MarketingPreferenceLayout(
    viewModel: MarketingPreferencesViewModel = viewModel(),
    title: String,
    from: String,
    onBackPressed: () -> Unit,
    finish: Action
) {

    val preferences by viewModel.preferences.collectAsStateWithLifecycle()
    val isErrorView by viewModel.operationError.collectAsStateWithLifecycle()
    val preferencesSaved by viewModel.onPreferencesSaved.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()

    LaunchedEffect(preferencesSaved) {
        if (preferencesSaved)
            finish()
    }

    Content(
        preferences = preferences.fields ?: emptyList(),
        title = title,
        onBackPressed = onBackPressed,
        savePreferences = { viewModel.savePreferences() },
        updatePreferences = { hashMap -> viewModel.updatePreferences(hashMap) },
        requestPreferences = { viewModel.requestPreferences(from) },
        isErrorView = isErrorView,
        isLoading = isLoading,
    )
}

@Composable
private fun Content(
    preferences: List<Field>,
    title: String,
    onBackPressed: () -> Unit,
    savePreferences: Action,
    updatePreferences: (HashMap<String, String>) -> Unit,
    requestPreferences: Action,
    isErrorView: ErrorType,
    isLoading: Boolean
) {

    Column {
        Box {
            SWTopAppBar(barTitle = title, onBackPressed = { onBackPressed() })
            Row(
                Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center)
                    .padding(end = 20.dp),
                horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.CenterVertically,
            ) {
                SWText(
                    modifier = Modifier.clickable {
                        savePreferences()
                    },
                    text = "SEND",
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                )
            }
        }

        if (isErrorView != ErrorType.None) {
            SWErrorScreenLayout { requestPreferences() }
        } else {
            Box {
                if (isLoading) {
                    SWCircularLoader()
                }
                SWFormLazyColumn(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
                    fieldList = preferences,
                    updateFields = { _, hashMap ->
                        updatePreferences(hashMap)
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 420)
@Composable
fun Preview() {
    Content(
        mutableListOf(),
        "title",
        {},
        {},
        {},
        {},
        ErrorType.None,
        false
    )
}
