package com.smallworldfs.moneytransferapp.presentation.account.offices.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.compose.colors.black
import com.smallworldfs.moneytransferapp.compose.colors.colorBlueOcean
import com.smallworldfs.moneytransferapp.compose.colors.colorGrayLight
import com.smallworldfs.moneytransferapp.compose.widgets.SWTopError
import com.smallworldfs.moneytransferapp.compose.widgets.SWImageFlag
import com.smallworldfs.moneytransferapp.compose.widgets.SWText
import com.smallworldfs.moneytransferapp.compose.widgets.SWTopAppBar
import com.smallworldfs.moneytransferapp.domain.migrated.base.Action
import com.smallworldfs.moneytransferapp.presentation.account.offices.model.OfficeUIModel
import com.smallworldfs.moneytransferapp.presentation.base.ErrorType
import com.smallworldfs.moneytransferapp.presentation.common.countries.CountryUIModel
import com.smallworldfs.moneytransferapp.utils.Constants

@Composable
fun OfficesActivityLayout(
    onCountrySelectorClick: (List<CountryUIModel>) -> Unit,
    onCitySelectorClick: (List<String>) -> Unit,
    onOfficeClick: (OfficeUIModel) -> Unit,
    onBackPressed: Action,
    registerEventCallback: (String) -> Unit,
    viewModel: OfficesViewModel = viewModel()
) {

    val officeList by viewModel.filteredOfficeList.collectAsStateWithLifecycle()
    val error by viewModel.officesError.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxHeight()
    ) {
        if (error !is ErrorType.None) {
            SWTopError(
                body = stringResource(R.string.generic_error_view_subtitle),
            ) {
                viewModel.hideErrorView()
            }
        }

        SWTopAppBar(
            barTitle = stringResource(id = R.string.offices_title),
            onBackPressed = { onBackPressed() },
            registerEventCallback = registerEventCallback
        )

        LocationCard(
            onCountrySelectorClick = onCountrySelectorClick,
            onCitySelectorClick = onCitySelectorClick,
            registerEventCallback = registerEventCallback,
            viewModel = viewModel
        )

        Offices(
            officesList = officeList,
            onItemClicked = onOfficeClick,
            registerEventCallback = registerEventCallback
        )
    }
}

@Composable
fun LocationCard(
    onCountrySelectorClick: (List<CountryUIModel>) -> Unit,
    onCitySelectorClick: (List<String>) -> Unit,
    registerEventCallback: (String) -> Unit,
    viewModel: OfficesViewModel
) {

    val userCountry by viewModel.selectedCountry.collectAsStateWithLifecycle()
    val countryList by viewModel.countryList.collectAsStateWithLifecycle()
    val cityList by viewModel.cityList.collectAsStateWithLifecycle()
    val filterName by viewModel.filterSelected.collectAsStateWithLifecycle()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .clickable { onCountrySelectorClick(countryList) }
            ) {
                Row {
                    SWImageFlag(
                        modifier = Modifier
                            .width(24.dp)
                            .height(24.dp),
                        imageUrl = Constants.COUNTRY.FLAG_IMAGE_ASSETS + userCountry.iso3 + Constants.COUNTRY.FLAG_IMAGE_EXTENSION
                    )

                    Icon(
                        modifier = Modifier
                            .width(24.dp)
                            .height(24.dp),
                        painter = painterResource(id = R.drawable.account_icn_arrowactivitycard),
                        contentDescription = "logo"
                    )
                }
            }

            Box(
                modifier = Modifier
                    .clickable {
                        onCitySelectorClick(cityList)
                        registerEventCallback("click_all_locations")
                    }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SWText(
                        text = filterName.ifEmpty { stringResource(id = R.string.offices_all_cities) },
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Icon(
                        modifier = Modifier
                            .width(24.dp)
                            .height(24.dp),
                        tint = colorBlueOcean,
                        painter = painterResource(id = R.drawable.account_icn_arrowactivitycard),
                        contentDescription = "logo"
                    )
                }
            }
        }
    }
}

@Composable
fun Offices(
    officesList: List<OfficeUIModel>,
    onItemClicked: (OfficeUIModel) -> Unit,
    registerEventCallback: (String) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(officesList) { office ->
            OfficeItem(
                office = office,
                onItemClicked = {
                    onItemClicked(office)
                    registerEventCallback("click_${it.city}")
                },
            )
        }
    }
}

@Composable
fun OfficeItem(
    office: OfficeUIModel,
    onItemClicked: (OfficeUIModel) -> Unit
) {
    Card(
        modifier = Modifier
            .clickable {
                onItemClicked(office)
            }
    ) {
        Row(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .clickable { onItemClicked(office) },
            ) {
                SWText(
                    text = office.name,
                    color = black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                SWText(
                    text = office.address,
                    color = colorGrayLight,
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Icon(
                modifier = Modifier
                    .width(24.dp)
                    .height(24.dp),
                tint = colorBlueOcean,
                painter = painterResource(id = R.drawable.account_icn_arrowactivitycard),
                contentDescription = "logo"
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 420)
@Composable
fun Preview() {
    OfficesActivityLayout({}, {}, {}, {}, {})
}
