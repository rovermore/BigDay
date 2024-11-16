package com.smallworldfs.moneytransferapp.presentation.mtn

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.base.presentation.ui.extensions.loadImage
import com.smallworldfs.moneytransferapp.databinding.FlowContainerMtnBinding
import com.smallworldfs.moneytransferapp.databinding.TransactionFlowLayoutBinding
import com.smallworldfs.moneytransferapp.presentation.mtn.model.StatusUIModel
import com.smallworldfs.moneytransferapp.utils.invisible
import com.smallworldfs.moneytransferapp.utils.visible

class TransactionFlow(private val view: View, private val context: Context) {
    private val binding = FlowContainerMtnBinding.bind(view)

    fun addSteps(steps: List<StatusUIModel>) {
        binding.flowContainerMtn.removeAllViews()
        var stepCounter = 1
        steps.forEach {
            val transactionBinding = TransactionFlowLayoutBinding.bind(LayoutInflater.from(context).inflate(R.layout.transaction_flow_layout, null))
            var visibility = View.VISIBLE
            var drawable = -1
            when (it) {
                is StatusUIModel.Done -> {
                    drawable = R.drawable.status_icn_step1done
                }
                is StatusUIModel.Pending -> {
                    transactionBinding.root.alpha = .3f
                    visibility = View.INVISIBLE
                    transactionBinding.flowLineBottom.invisible()
                }
                is StatusUIModel.InProgress -> {
                    transactionBinding.root.alpha = 1f
                    visibility = View.VISIBLE
                    transactionBinding.flowLineBottom.invisible()
                }
                is StatusUIModel.Cancelled -> {
                    drawable = R.drawable.status_icn_cancelled
                    transactionBinding.flowLineBottom.invisible()
                }
            }
            if (stepCounter == 1)
                visibility = View.INVISIBLE
            if (stepCounter == steps.size)
                transactionBinding.flowLineBottom.invisible()

            with(transactionBinding) {
                if (drawable != -1) stepIcon.loadImage(drawable)
                stepText.text = it.title
                flowLine.visibility = visibility
                if (it is StatusUIModel.Pending || it is StatusUIModel.InProgress)
                    stepNumberTextView.text = stepCounter.toString()
            }
            binding.flowContainerMtn.addView(transactionBinding.root)
            stepCounter++
        }
    }

    fun addError(errorMessage: String) {
        val transactionBinding = TransactionFlowLayoutBinding.bind(LayoutInflater.from(context).inflate(R.layout.transaction_flow_layout, null))
        binding.flowContainerMtn.removeAllViews()
        transactionBinding.stepText.text = errorMessage
        transactionBinding.stepIcon.loadImage(R.drawable.status__icn_attenction)
        binding.flowContainerMtn.addView(transactionBinding.root)
        transactionBinding.visible()
        binding.flowContainerMtn.visible()
        transactionBinding.flowLineBottom.invisible()
    }

    fun cleanError() {
        binding.flowContainerMtn.removeAllViews()
    }
}
