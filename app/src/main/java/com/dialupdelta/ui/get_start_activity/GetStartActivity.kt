package com.dialupdelta.ui.get_start_activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.dialupdelta.R
import com.dialupdelta.base.BaseActivity
import com.dialupdelta.databinding.ActivityGetStartBinding

import com.dialupdelta.utils.setGone
import com.dialupdelta.utils.setVisible
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import org.kodein.di.generic.instance

class GetStartActivity : BaseActivity() {
    private val factory: GetStartViewModelFactory by instance()
    private lateinit var viewModel: GetStartViewModel
    private var binding: ActivityGetStartBinding? = null
    private var selectedAge: Int = 0
    private var simpleExoPlayer: SimpleExoPlayer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_get_start)

        initUI()
    }

    private fun initUI() {
        viewModel = ViewModelProvider(this, factory)[GetStartViewModel::class.java]
        setObservers(viewModel)
        binding?.progressBar?.setVisible()
        binding?.btnGetStarted?.setOnClickListener {
            startActivity(Intent(this, LowVsHighActivity::class.java))
            finish()
        }
        viewModel.getAgeGenderApi()
    }

    private fun setObservers(viewModel: GetStartViewModel) {

        viewModel.getStartSuccess.observe(this) {
            binding?.progressBar?.setGone()
            selectGender()
            selectAge()
        }

        viewModel.getStartVideoLink.observe(this){
            videoPlay(it)
        }

        viewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                progress?.showSweetDialog()
            } else {
                progress?.dismissSweet()
            }
        }
    }

    private fun selectAge() {
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            viewModel.getAgeList()!!
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding?.spinnerAgeGet?.adapter = adapter
        binding?.spinnerAgeGet?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parentView: AdapterView<*>?,
                    selectedItemView: View,
                    position: Int,
                    id: Long
                ) {
                    selectedAge = position
                }

                override fun onNothingSelected(parentView: AdapterView<*>?) {

                }
            }
    }

    private fun selectGender() {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, viewModel.getGenderList()!!)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding?.spinnerGenderGet?.adapter = adapter
        binding?.spinnerGenderGet?.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parentView: AdapterView<*>?,
                    selectedItemView: View,
                    position: Int,
                    id: Long
                ) {
                     viewModel.setGender(viewModel.getGenderList()?.get(position)?.id)
                }

                override fun onNothingSelected(parentView: AdapterView<*>?) {

                }
            }
    }

    private fun videoPlay(newUrl:String) {
            val mediaItem: MediaItem = newUrl.let { MediaItem.fromUri(it) }
            simpleExoPlayer = SimpleExoPlayer.Builder(this).build().also { it ->
                binding?.getStartedVideo?.player = it
                binding?.getStartedVideo?.hideController()
                binding?.getStartedVideo?.setControllerVisibilityListener {
                    if(it == View.VISIBLE){
                        binding?.getStartedVideo?.hideController()
                    }
                }
                it.setMediaItem(mediaItem)
                it.prepare()
                it.play()
                simpleExoPlayer?.volume  = 0f

            }
        }
    }