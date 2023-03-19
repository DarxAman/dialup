package com.dialupdelta.ui.feedback

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.dialupdelta.R
import com.dialupdelta.`interface`.FeedBackItemClickListener
import com.dialupdelta.base.BaseFragment
import com.dialupdelta.databinding.FragmentFeedBackBinding
import com.dialupdelta.ui.feedback.adapter.FeedbackAdapter
import com.dialupdelta.ui.intro_screen.IntroductionFirstVideoActivity
import org.kodein.di.generic.instance

class FeedBackFragment : BaseFragment(), FeedBackItemClickListener {
  private lateinit var binding :FragmentFeedBackBinding

    private val factory: FeedBackViewModelFactory by instance()
    private lateinit var viewModel: FeedBackViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_feed_back, container, false)
       return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
    }

    private fun initUi() {
        viewModel = ViewModelProvider(this, factory)[FeedBackViewModel::class.java]
        setObserver(viewModel)
        val adapter =  FeedbackAdapter(requireActivity(), this)
        binding.feedbackRecyclerView.adapter = adapter
    }

    private fun setObserver(viewModel: FeedBackViewModel) {

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                progress?.showSweetDialog()
            } else {
                progress?.dismissSweet()
            }
        }
    }

    override fun feedBackItemListener() {
        Intent(context, FeedBackDetailsActivity::class.java).also {
           startActivity(it)
        }
    }

}