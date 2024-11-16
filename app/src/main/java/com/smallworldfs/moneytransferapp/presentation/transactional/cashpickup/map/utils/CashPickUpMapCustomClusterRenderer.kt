package com.smallworldfs.moneytransferapp.presentation.transactional.cashpickup.map.utils

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.google.maps.android.ui.IconGenerator
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.SmallWorldApplication
import com.smallworldfs.moneytransferapp.base.domain.utils.toBitmap

class CashPickUpMapCustomClusterRenderer(val context: Context, val map: GoogleMap?, clusterManager: ClusterManager<CashPickUpMarkerPresentationModel>?) : DefaultClusterRenderer<CashPickUpMarkerPresentationModel>(context, map, clusterManager) {

    companion object {
        const val MAX_ITEMS_BEFORE_CREATE_CLUSTER = 5
    }

    /**
     * Set max items spread before create a cluster
     */
    override fun shouldRenderAsCluster(cluster: Cluster<CashPickUpMarkerPresentationModel>): Boolean = cluster.size > MAX_ITEMS_BEFORE_CREATE_CLUSTER

    /**
     * This method is for an individual location
     */
    override fun onBeforeClusterItemRendered(item: CashPickUpMarkerPresentationModel, markerOptions: MarkerOptions) {
        // The first time that the point is painted
        markerOptions
            .icon(
                (if (item.selected) R.drawable.ic_pin_selected.toBitmap(SmallWorldApplication.app, null) else R.drawable.ic_pin_active.toBitmap(SmallWorldApplication.app, null))?.let {
                    BitmapDescriptorFactory.fromBitmap(
                        it
                    )
                }
            )
            .title(item.locationName)
            .snippet(item.locationAddress)
    }

    override fun onClusterItemUpdated(item: CashPickUpMarkerPresentationModel, marker: Marker) {
        // The next time that the point is painted, this method updated cache
        marker.setIcon(
            (if (item.selected) R.drawable.ic_pin_selected.toBitmap(SmallWorldApplication.app, null) else R.drawable.ic_pin_active.toBitmap(SmallWorldApplication.app, null))?.let {
                BitmapDescriptorFactory.fromBitmap(
                    it
                )
            }
        )
        if (item.selected) marker.showInfoWindow()
    }

    /**
     * This method is for cluster of locations
     */
    @SuppressLint("InflateParams")
    override fun onBeforeClusterRendered(cluster: Cluster<CashPickUpMarkerPresentationModel>, markerOptions: MarkerOptions) {
        // Get the size of the cluster
        val size = cluster.size.toString()

        // Inflate icon throw Icon Generator class
        val iconGenerator = IconGenerator(context)

        // Set the background of the cluster
        iconGenerator.setBackground(ContextCompat.getDrawable(context, R.drawable.background_item_cluster_cash_pick_up_map))

        // Inflate the text view
        val activityView: View = (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(R.layout.item_cluster_cash_pick_up_map, null, false)
        iconGenerator.setContentView(activityView)

        // Set the icon to the clusters
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(iconGenerator.makeIcon(size)))
    }

    override fun onClusterUpdated(cluster: Cluster<CashPickUpMarkerPresentationModel>, marker: Marker) {}
}
