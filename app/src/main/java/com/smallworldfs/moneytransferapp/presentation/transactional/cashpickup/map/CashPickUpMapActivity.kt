package com.smallworldfs.moneytransferapp.presentation.transactional.cashpickup.map

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.SmallWorldApplication
import com.smallworldfs.moneytransferapp.base.domain.utils.toBitmap
import com.smallworldfs.moneytransferapp.base.presentation.ui.BaseStateAppCompatActivity
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.ScreenCategory
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.ScreenName
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.UserActionEvent
import com.smallworldfs.moneytransferapp.base.presentation.ui.extensions.setTextIfHasChanged
import com.smallworldfs.moneytransferapp.base.presentation.viewmodel.BaseExtraData
import com.smallworldfs.moneytransferapp.base.presentation.viewmodel.EventObserver
import com.smallworldfs.moneytransferapp.databinding.ActivityCashPickUpMapBinding
import com.smallworldfs.moneytransferapp.presentation.form.selector.FormSelectorItem
import com.smallworldfs.moneytransferapp.presentation.transactional.cashpickup.map.CashPickUpMapNavigator.Companion.REQUEST_CODE_CITY
import com.smallworldfs.moneytransferapp.presentation.transactional.cashpickup.map.CashPickUpMapNavigator.Companion.REQUEST_CODE_PAYMENT_NETWORK
import com.smallworldfs.moneytransferapp.presentation.transactional.cashpickup.map.CashPickUpMapNavigator.Companion.REQUEST_CODE_STATE
import com.smallworldfs.moneytransferapp.presentation.transactional.cashpickup.map.utils.CashPickUpMapAdapter
import com.smallworldfs.moneytransferapp.presentation.transactional.cashpickup.map.utils.CashPickUpMapCustomClusterRenderer
import com.smallworldfs.moneytransferapp.presentation.transactional.cashpickup.map.utils.CashPickUpMarkerPresentationModel
import com.smallworldfs.moneytransferapp.presentation.transactional.cashpickup.map.utils.CashPickUpMarkerPresentationModelFilter
import com.smallworldfs.moneytransferapp.presentation.transactional.cashpickup.map.utils.OnMapCameraAnimationFinishCallback
import com.smallworldfs.moneytransferapp.presentation.transactional.cashpickup.map.utils.filter
import com.smallworldfs.moneytransferapp.utils.INPUT_STATE
import com.smallworldfs.moneytransferapp.utils.INT_ONE
import com.smallworldfs.moneytransferapp.utils.INT_ZERO
import com.smallworldfs.moneytransferapp.utils.KeyboardUtils
import com.smallworldfs.moneytransferapp.utils.KeyboardUtils.SoftKeyboardToggleListener
import com.smallworldfs.moneytransferapp.utils.RESULT_ITEM
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import com.smallworldfs.moneytransferapp.utils.gone
import com.smallworldfs.moneytransferapp.utils.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CashPickUpMapActivity : BaseStateAppCompatActivity<CashPickUpMapState, CashPickUpMapViewModel, CashPickUpMapNavigator, ActivityCashPickUpMapBinding>(CashPickUpMapViewModel::class.java), OnMapReadyCallback, SoftKeyboardToggleListener {

    companion object {
        const val MAP_PADDING = 200
        const val ZOOM = 14f
    }

    override val bindingInflater: (LayoutInflater) -> ActivityCashPickUpMapBinding = ActivityCashPickUpMapBinding::inflate
    private lateinit var vm: CashPickUpMapViewModel
    private lateinit var map: GoogleMap
    private lateinit var clusterManager: ClusterManager<CashPickUpMarkerPresentationModel>
    private lateinit var mAdapter: CashPickUpMapAdapter
    private var showList: Boolean = false
    private lateinit var locationAddress: String

    override fun setInputState(intent: Intent?): CashPickUpMapState = intent?.getSerializableExtra(INPUT_STATE) as CashPickUpMapState

    override fun onViewModelInitialized(viewModel: CashPickUpMapViewModel) {
        vm = viewModel

        setupToolbar(false)
        setupMap()
        setupSelectorListeners()
        setupButtonListener()
        setupEditTextListener()
        setupRecyclerView()
        setupObservers()
    }

    override fun onStateNormal(data: CashPickUpMapState) {
        // Hide loading indicator
        binding.activityCashPickUpMapProgressBar.hide()

        // Set options selected
        if (data.stateList.size > INT_ZERO) {
            binding.activityCashPickUpMapTextInputEditTextState.setText(data.stateList[data.stateListSelected])
        }
        if (data.cityList.size > INT_ZERO) {
            binding.activityCashPickUpMapTextInputEditTextCity.setText(data.cityList[data.cityListSelected])
        }
        if (data.paymentNetworkList.size > INT_ZERO) {
            binding.activityCashPickUpMapTextInputEditTextPaymentNetwork.setText(data.paymentNetworkList[data.paymentNetworkListSelected])
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            binding.activityCashPickUpMapTextInputEditTextState.foreground = if (data.stateList.size <= INT_ONE) ContextCompat.getDrawable(this, R.color.white_transparency_60) else null
            binding.activityCashPickUpMapTextInputEditTextCity.foreground = if (data.cityList.size <= INT_ONE) ContextCompat.getDrawable(this, R.color.white_transparency_60) else null
            binding.activityCashPickUpMapTextInputEditTextPaymentNetwork.foreground = if (data.paymentNetworkList.size <= INT_ONE) ContextCompat.getDrawable(this, R.color.white_transparency_60) else null
        }

        // Update list
        vm.mapLocations.value?.let {
            val stateSelected = if (data.stateListSelected > INT_ZERO) data.stateList[data.stateListSelected] else STRING_EMPTY
            val citySelected = if (data.cityListSelected > INT_ZERO) data.cityList[data.cityListSelected] else STRING_EMPTY
            val paymentNetworkSelected = if (data.paymentNetworkListSelected > INT_ZERO) data.paymentNetworkList[data.paymentNetworkListSelected] else STRING_EMPTY
            val searchText = data.searchBoxValue
            val filteredLocations = it.peekContent().filter(
                CashPickUpMarkerPresentationModelFilter(
                    stateSelected, citySelected, paymentNetworkSelected, searchText,
                ),
            )
            val cashPickUpMarkerPresentationModelList = mutableListOf<CashPickUpMarkerPresentationModel>()
            data.allLocationResponses.forEach { cashPickUpMarkerPresentationModelList.add(it.toCashPickUpMarkerPresentationModel()) }
            updateList(cashPickUpMarkerPresentationModelList)
            updateMap(filteredLocations)
        }

        // Set text inside the search bar
        binding.activityCashPickUpMapEditTextSearch.setTextIfHasChanged(data.searchBoxValue)

        // Set visibility of map/recycler view
        if (data.stateListSelected == INT_ZERO) {
            showList = false
        }
        checkIfShouldShowListOrMap()

        /**
         * Layout change when Anywhere Pick Up
         */
        if (data.isAnyWherePickUp) {
            binding.activityCashPickUpMapMaterialButtonSelectLocation.gone()
            setupToolbar(true)
            showSearchBox()
        } else {
            // Show or hide list button and search bar
            if (data.cityList.size > INT_ONE) {
                if (data.stateListSelected == INT_ZERO) {
                    if (data.stateList.size > INT_ONE) {
                        // when a state is not selected yet
                        hideSearchBox()
                    } else {
                        // when there is no state in the list
                        showSearchBox()
                    }
                } else {
                    // when a state is selected
                    showSearchBox()
                }
            } else {
                // hide while loading data
                hideSearchBox()
            }
        }
        locationAddress = data.locationSelected?.locationAddress ?: ""
    }

    private fun hideSearchBox() {
        binding.activityCashPickUpMapButtonChangeMapList.gone()
        binding.activityCashPickUpMapEditTextSearch.gone()
    }

    private fun showSearchBox() {
        binding.activityCashPickUpMapButtonChangeMapList.visible()
        binding.activityCashPickUpMapEditTextSearch.visible()
    }

    private fun setupObservers() {
        vm.mapLocations.observe(
            this,
            EventObserver {
                if (this::map.isInitialized) {
                    drawPoints(it)
                }
            }
        )
        vm.selectedLocation.observe(
            this,
            EventObserver { selectedLocation ->
                updateMarkerSelection(selectedLocation)
                updateListSelection(selectedLocation)
            }
        )
    }

    private fun updateMarkerSelection(selectedLocation: CashPickUpMarkerPresentationModel) {
        map.animateCamera(
            CameraUpdateFactory.newLatLngZoom(selectedLocation.location, ZOOM),
            object : OnMapCameraAnimationFinishCallback() {
                override fun onAnimationFinish() {
                    val elements = clusterManager.algorithm.items.toMutableList()
                    val previousSelection = elements.find { it.selected }?.apply { selected = false }
                    previousSelection?.let {
                        clusterManager.updateItem(it)
                        (clusterManager.renderer as CashPickUpMapCustomClusterRenderer).getMarker(it)?.apply {
                            setIcon(
                                R.drawable.ic_pin_active.toBitmap(SmallWorldApplication.app, null)
                                    ?.let { it1 -> BitmapDescriptorFactory.fromBitmap(it1) },
                            )
                            hideInfoWindow()
                        }
                    }
                    val newSelection = elements.find { it.location == selectedLocation.location }!!.apply { selected = true }
                    clusterManager.updateItem(newSelection)
                    clusterManager.cluster()
                    (clusterManager.renderer as CashPickUpMapCustomClusterRenderer).getMarker(newSelection)?.apply {
                        setIcon(
                            R.drawable.ic_pin_selected.toBitmap(SmallWorldApplication.app, null)
                                ?.let { BitmapDescriptorFactory.fromBitmap(it) },
                        )
                        showInfoWindow()
                    }
                    binding.activityCashPickUpMapMaterialButtonSelectLocation.isEnabled = true
                    binding.activityCashPickUpMapMaterialButtonSelectLocation.text = getString(R.string.activityCashPickUpMapMaterialButtonSelectLocationTextEnabled)
                }
            },
        )
    }

    private fun updateListSelection(selectedLocation: CashPickUpMarkerPresentationModel) {
        val elements = mAdapter.listItems
        val previousSelection = elements.find { it.selected }?.apply { selected = false }
        previousSelection?.let {
            mAdapter.updateItem(it)
        }
        val newSelection = elements.find { it.location == selectedLocation.location }!!.apply { selected = true }
        mAdapter.updateItem(newSelection)
    }

    private fun updateMap(items: List<CashPickUpMarkerPresentationModel>) {
        binding.activityCashPickUpMapMaterialButtonSelectLocation.isEnabled = false
        binding.activityCashPickUpMapMaterialButtonSelectLocation.text = getString(R.string.activityCashPickUpMapMaterialButtonSelectLocationTextDisabled)
        clusterManager.algorithm.items.find { it.selected }?.let {
            it.selected = false
            clusterManager.updateItem(it)
            (clusterManager.renderer as CashPickUpMapCustomClusterRenderer).getMarker(it)?.apply {
                setIcon(
                    R.drawable.ic_pin_active.toBitmap(SmallWorldApplication.app, null)
                        ?.let { it1 -> BitmapDescriptorFactory.fromBitmap(it1) },
                )
                hideInfoWindow()
            }
        }
        clusterManager.clearItems()
        if (items.isNotEmpty()) {
            clusterManager.addItems(items)
            with(map) {
                val bounds = LatLngBounds.Builder().apply { items.forEach { include(it.location) } }
                animateCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), MAP_PADDING))
            }
        }
        clusterManager.cluster()
    }

    private fun updateList(list: List<CashPickUpMarkerPresentationModel>?) {
        mAdapter.listItems = list?.toMutableList() ?: mutableListOf()
        mAdapter.notifyDataSetChanged()
    }

    override fun onStateAlternative(data: BaseExtraData) {
        binding.activityCashPickUpMapProgressBar.show()
    }

    override fun onStateError(error: Throwable) {
    }

    private fun setupToolbar(isAnyWherePickup: Boolean) {
        setSupportActionBar(binding.activityCashPickUpMapToolbar)
        if (isAnyWherePickup) binding.toolbarTitle.text = getString(R.string.anywhere_pickup_map_toolbar_title)
        else binding.toolbarTitle.text = getString(R.string.cashPickUpMapToolbarTitle)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupMap() {
        (supportFragmentManager.findFragmentById(R.id.activityCashPickUpMapGoogleMapFragment) as SupportMapFragment).getMapAsync(this)
    }

    private fun setupSelectorListeners() {
        binding.activityCashPickUpMapButtonState.setOnClickListener {
            vm.onActionOpenStateSelector(ScreenName.STATE_OR_REGION_SCREEN.value)
            registerEvent("click_select_state_or_province", STRING_EMPTY)
        }

        binding.activityCashPickUpMapButtonCity.setOnClickListener {
            vm.onActionOpenStateCitySelector(ScreenName.CITY_SCREEN.value)
            registerEvent("click_select_city", STRING_EMPTY)
        }

        binding.activityCashPickUpMapButtonPaymentNetwork.setOnClickListener {
            vm.onActionOpenPaymentNetworkSelector(ScreenName.CHOOSE_PAYMENT_NETWORK_SCREEN.value)
            registerEvent("click_select_payment_network", STRING_EMPTY)
        }
    }

    private fun setupButtonListener() {
        binding.activityCashPickUpMapMaterialButtonSelectLocation.setOnClickListener {
            vm.onActionSelectLocationButton()
            registerEvent("click_confirm_location", locationAddress)
        }
        binding.activityCashPickUpMapButtonChangeMapList.setOnClickListener {
            showList = !showList
            checkIfShouldShowListOrMap()
            trackScreen(ScreenName.CHOOSE_BENEFICIARY_PICKUP_LOCATION_SCREEN.value)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupEditTextListener() {
        binding.activityCashPickUpMapEditTextSearch.addTextChangedListener(
            object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {
                    binding.activityCashPickUpMapEditTextSearch.setCompoundDrawablesWithIntrinsicBounds(INT_ZERO, INT_ZERO, if (p0?.isNotEmpty() == true) R.drawable.background_activity_cash_pick_up_map_field_cross_icon else R.drawable.background_activity_cash_pick_up_map_field_search_icon, INT_ZERO)
                    registerEvent("search_beneficiary_pickup_location", p0.toString(), "search")
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    vm.onActionChangeSearchText(s.toString())
                }
            },
        )

        binding.activityCashPickUpMapEditTextSearch.setOnTouchListener(
            View.OnTouchListener { _, event ->
                if (event.action == MotionEvent.ACTION_UP) {
                    if (event.rawX >= (binding.activityCashPickUpMapEditTextSearch.right - binding.activityCashPickUpMapEditTextSearch.compoundPaddingRight)) {
                        binding.activityCashPickUpMapEditTextSearch.setText(STRING_EMPTY)
                        return@OnTouchListener true
                    }
                }
                return@OnTouchListener false
            },
        )
    }

    private fun setupRecyclerView() {
        mAdapter = CashPickUpMapAdapter(
            mutableListOf(),
            object : CashPickUpMapAdapter.OnItemClickListener {
                override fun onItemClick(item: CashPickUpMarkerPresentationModel) {
                    vm.onActionSelectLocation(item)
                    registerEvent("click_pickup_location_list", item.locationAddress ?: STRING_EMPTY)
                }
            },
        )

        binding.activityCashPickUpMapRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@CashPickUpMapActivity)
            adapter = mAdapter
            itemAnimator = DefaultItemAnimator()
        }
    }

    /**
     * Map functions
     */
    @SuppressLint("PotentialBehaviorOverride")
    override fun onMapReady(googleMap: GoogleMap) {
        // When the map is ready request information from the backend
        vm.requestInformation()
        googleMap.let {
            map = googleMap
            clusterManager = ClusterManager(this@CashPickUpMapActivity, googleMap)

            map.setOnCameraIdleListener(clusterManager)
            map.setOnMarkerClickListener(clusterManager)

            clusterManager.renderer = CashPickUpMapCustomClusterRenderer(this, googleMap, clusterManager)

            clusterManager.setOnClusterClickListener { cluster -> onUserClicksCluster(cluster) }
            clusterManager.setOnClusterItemClickListener { marker -> onUserClicksMarker(marker) }

            clusterManager.markerCollection.setInfoWindowAdapter(
                object : GoogleMap.InfoWindowAdapter {
                    override fun getInfoWindow(marker: Marker): View? = null

                    @SuppressLint("InflateParams")
                    override fun getInfoContents(marker: Marker) =
                        layoutInflater.inflate(R.layout.adapter_cash_pick_up_map_info_view_window, null).apply {
                            (findViewById<View>(R.id.adapterCashPickUpMapInfoViewWindowTextViewTitle) as TextView).text = marker.title
                            (findViewById<View>(R.id.adapterCashPickUpMapInfoViewWindowTextViewSubtitle) as TextView).text = marker.snippet
                        }
                },
            )
        } ?: kotlin.run { finish() }
    }

    private fun onUserClicksCluster(cluster: Cluster<CashPickUpMarkerPresentationModel>): Boolean {
        val bounds = LatLngBounds.builder().apply {
            cluster.items.forEach { item -> include(item.position) }
        }.build()
        map.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, MAP_PADDING))
        registerEvent("click_map_location_bubble", STRING_EMPTY)
        return true
    }

    private fun onUserClicksMarker(location: CashPickUpMarkerPresentationModel): Boolean {
        vm.onActionSelectLocation(location)
        registerEvent("click_map_location_pin", location.locationAddress ?: STRING_EMPTY)
        return true
    }

    private fun drawPoints(locations: List<CashPickUpMarkerPresentationModel>) {
        if (locations.size > INT_ZERO) {
            with(map) {
                clusterManager.addItems(locations)
                clusterManager.cluster()
                val bounds = LatLngBounds.Builder().apply {
                    locations.forEach { include(it.location) }
                }.build()
                animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, MAP_PADDING))
            }
        }
    }

    /**
     * Update map and list
     */
    private fun checkIfShouldShowListOrMap() {
        binding.activityCashPickUpMapRecyclerView.visibility = if (showList) View.VISIBLE else View.GONE
        binding.activityCashPickUpMapGoogleMapFragment.visibility = if (!showList) View.VISIBLE else View.GONE
        binding.activityCashPickUpMapButtonChangeMapList.setImageDrawable(ContextCompat.getDrawable(this, if (!showList) R.drawable.ic_list_bulleted else R.drawable.ic_list_map))
        if (showList)
            registerEvent("click_list_icon", STRING_EMPTY)
        else
            registerEvent("click_map_icon", STRING_EMPTY)
    }

    /**
     * On activity result
     */
    override fun whenActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_STATE,
                REQUEST_CODE_CITY,
                REQUEST_CODE_PAYMENT_NETWORK -> {
                    val result = data?.getSerializableExtra(RESULT_ITEM) as FormSelectorItem
                    vm.onActionNewFieldSelected(requestCode, result.key)
                }
            }
        }
    }

    /**
     * Keyboard listener
     */
    override fun onToggleSoftKeyboard(isVisible: Boolean, heightDifference: Int) {
        binding.activityCashPickUpMapConstraintLayoutState.visibility = if (isVisible) View.GONE else View.VISIBLE
        binding.activityCashPickUpMapConstraintLayoutCity.visibility = if (isVisible) View.GONE else View.VISIBLE
        binding.activityCashPickUpMapConstraintLayoutPaymentNetwork.visibility = if (isVisible) View.GONE else View.VISIBLE
        binding.activityCashPickUpMapViewSeparator1.visibility = if (isVisible) View.GONE else View.VISIBLE
    }

    override fun onResume() {
        super.onResume()
        KeyboardUtils.addKeyboardToggleListener(this, this)
    }

    override fun onPause() {
        super.onPause()
        KeyboardUtils.removeKeyboardToggleListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            registerEvent("click_back", STRING_EMPTY)
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun registerEvent(eventAction: String, eventLabel: String, formType: String = "checkout") {
        trackEvent(
            UserActionEvent(
                ScreenCategory.TRANSFER.value,
                eventAction,
                eventLabel,
                getHierarchy(STRING_EMPTY),
                formType,
                "pick_up",
                STRING_EMPTY,
            ),
        )
    }
}
