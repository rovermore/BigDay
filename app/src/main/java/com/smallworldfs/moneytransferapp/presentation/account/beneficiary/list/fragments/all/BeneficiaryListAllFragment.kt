package com.smallworldfs.moneytransferapp.presentation.account.beneficiary.list.fragments.all

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.base.presentation.viewmodel.EventObserver
import com.smallworldfs.moneytransferapp.databinding.FragmentBeneficiaryListAllBinding
import com.smallworldfs.moneytransferapp.presentation.account.beneficiary.list.fragments.common.MyBeneficiariesNavigator
import com.smallworldfs.moneytransferapp.modules.common.presentation.ui.GenericFragment
import com.smallworldfs.moneytransferapp.presentation.account.beneficiary.list.fragments.common.BeneficiaryListAdapter
import com.smallworldfs.moneytransferapp.presentation.account.beneficiary.list.fragments.common.BeneficiaryListListener
import com.smallworldfs.moneytransferapp.presentation.account.beneficiary.list.fragments.common.BeneficiaryListViewModel
import com.smallworldfs.moneytransferapp.presentation.account.beneficiary.list.model.BeneficiaryUIModel
import com.smallworldfs.moneytransferapp.utils.Constants.MY_BENEFICIARIES_PAGE_ITEMS
import com.smallworldfs.moneytransferapp.utils.INT_ONE
import com.smallworldfs.moneytransferapp.utils.gone
import com.smallworldfs.moneytransferapp.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BeneficiaryListAllFragment : GenericFragment() {

    private var _binding: FragmentBeneficiaryListAllBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var navigator: MyBeneficiariesNavigator

    private val viewModel: BeneficiaryListViewModel by viewModels()

    private lateinit var mAdapter: BeneficiaryListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentBeneficiaryListAllBinding.inflate(inflater, container, false)

        setupRefreshLayoutAction()
        setupRecyclerView()
        setupObservers()

        viewModel.onViewInitialized()

        return binding.root
    }

    private fun setupObservers() {
        with(viewModel) {
            loadingLiveData.observe(
                requireActivity(),
                EventObserver { loading ->
                    if (loading) {
                        showLoadingView()
                    }
                }
            )

            beneficiaryListLiveData.observe(
                requireActivity(),
                EventObserver {
                    hideLoadingView()
                    showData(it)
                }
            )

            errorBeneficiaryListLiveData.observe(
                requireActivity(),
                EventObserver {
                    hideLoadingView()
                    binding.genericScreenErrorView.visible()
                }
            )
        }
    }

    private fun showLoadingView() {
        binding.genericLoadingView.visible()
        mAdapter.isLoading = true
    }

    private fun hideLoadingView() {
        binding.genericLoadingView.gone()
        mAdapter.isLoading = true
    }

    private fun showData(data: List<BeneficiaryUIModel>) {
        binding.myBeneficiariesEmptyView.gone()
        binding.genericScreenErrorView.gone()

        if (data.isNotEmpty()) {
            mAdapter.removeFooter()
            mAdapter.replaceAll(data)

            if (data.size >= MY_BENEFICIARIES_PAGE_ITEMS.PAGE_ITEMS) {
                mAdapter.addLoadingFooter()
                mAdapter.isLastPage = false
            } else {
                mAdapter.isLastPage = true
            }
        } else {
            binding.myBeneficiariesEmptyView.visible()
        }
    }

    private fun setupRefreshLayoutAction() {
        binding.swipeRefreshLayout.setColorSchemeColors(
            ContextCompat.getColor(binding.swipeRefreshLayout.context, R.color.main_blue),
            ContextCompat.getColor(binding.swipeRefreshLayout.context, R.color.main_blue),
            ContextCompat.getColor(binding.swipeRefreshLayout.context, R.color.main_blue)
        )

        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.swipeRefreshLayout.isRefreshing = false
            viewModel.onActionRefreshLayout()
        }
    }

    private fun setupRecyclerView() {
        binding.beneficiaryRecyclerview.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)

        mAdapter = BeneficiaryListAdapter(
            mutableListOf(),
            object : BeneficiaryListListener {
                override fun onItemClick(item: BeneficiaryUIModel, position: Int) {
                    navigator.navigateToBeneficiaryDetail(item)
                }
            }
        )

        binding.beneficiaryRecyclerview.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity)
            adapter = mAdapter
            itemAnimator = DefaultItemAnimator()
        }

        binding.beneficiaryRecyclerview.adapter = mAdapter

        binding.beneficiaryRecyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(INT_ONE)) {
                    if (!mAdapter.isLoading && !mAdapter.isLastPage) {
                        mAdapter.isLoading = true
                        viewModel.onActionScrollDownLoadMore()
                    }
                }
            }
        })
    }
}
