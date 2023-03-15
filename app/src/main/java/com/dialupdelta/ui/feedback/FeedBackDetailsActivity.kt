package com.dialupdelta.ui.feedback

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.dialupdelta.R
import com.dialupdelta.base.BaseActivity
import com.dialupdelta.databinding.ActivityFeedBackDetailsBinding
import com.dialupdelta.ui.feedback.adapter.AllFeedBackListAdapter
import org.kodein.di.generic.instance

class FeedBackDetailsActivity : BaseActivity() {
    private lateinit var binding:ActivityFeedBackDetailsBinding
    private val factory: FeedBackViewModelFactory by instance()
    private lateinit var viewModel: FeedBackViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding =  DataBindingUtil.setContentView(this, R.layout.activity_feed_back_details)

        initUI()
    }

    private fun initUI() {
        viewModel = ViewModelProvider(this, factory)[FeedBackViewModel::class.java]
        setObserver(viewModel)
        viewModel.allFeedbackDetails()
    }

    private fun setObserver(viewModel: FeedBackViewModel) {

        viewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                progress?.showSweetDialog()
            } else {
                progress?.dismissSweet()
            }
        }

        viewModel.feedBackDetailsResponse.observe(this){
          binding.allFeedBackRecycler.adapter = AllFeedBackListAdapter(this, viewModel.getAllFeedBackList())
        }
    }
}