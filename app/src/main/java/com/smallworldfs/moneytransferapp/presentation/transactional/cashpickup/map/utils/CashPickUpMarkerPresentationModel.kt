package com.smallworldfs.moneytransferapp.presentation.transactional.cashpickup.map.utils

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import com.smallworldfs.moneytransferapp.utils.DOUBLE_ZERO
import com.smallworldfs.moneytransferapp.utils.INT_ZERO
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import java.util.Locale

data class CashPickUpMarkerPresentationModel(
    val location: LatLng,
    var locationState: String? = STRING_EMPTY,
    var locationCity: String? = STRING_EMPTY,
    var representativeName: String? = STRING_EMPTY,
    var locationName: String? = STRING_EMPTY,
    var locationAddress: String? = STRING_EMPTY,
    var locationCode: String? = STRING_EMPTY,
    var representativeCode: Int? = INT_ZERO,
    var fee: Double? = DOUBLE_ZERO,
    var rate: String? = STRING_EMPTY,
    var deliveryTime: String? = STRING_EMPTY,
    var selected: Boolean = false
) : ClusterItem {

    override fun getPosition(): LatLng = location

    override fun getTitle(): String? = locationName

    override fun getSnippet(): String? = locationAddress

    override fun hashCode() = representativeCode!!
}

data class CashPickUpMarkerPresentationModelFilter(
    val state: String = "",
    val city: String = "",
    val paymentNetwork: String = "",
    val searchText: String = "",
)

fun List<CashPickUpMarkerPresentationModel>.filter(filter: CashPickUpMarkerPresentationModelFilter): List<CashPickUpMarkerPresentationModel> {
    val filteredData = this.toMutableList()

    if (filter.state.isNotBlank()) {
        filteredData.removeAll { it.locationState.isNullOrEmpty() || it.locationState != filter.state }
    }
    if (filter.city.isNotBlank()) {
        filteredData.removeAll { it.locationCity.isNullOrEmpty() || it.locationCity != filter.city }
    }
    if (filter.paymentNetwork.isNotBlank()) {
        filteredData.removeAll { it.representativeName.isNullOrEmpty() || it.representativeName != filter.paymentNetwork }
    }
    if (filter.searchText.isNotBlank()) {
        filteredData.removeAll {
            !(
                (it.locationName != null && it.locationName!!.toLowerCase(Locale.getDefault()).contains(filter.searchText.toLowerCase(Locale.getDefault()))) ||
                    (it.locationAddress != null && it.locationAddress!!.toLowerCase(Locale.getDefault()).contains(filter.searchText.toLowerCase(Locale.getDefault())))
                )
        }
    }
    return filteredData.toList()
}
