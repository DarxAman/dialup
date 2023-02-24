package com.dialupdelta.ui.get_start_activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.dialupdelta.R
import com.dialupdelta.base.BaseActivity
import com.dialupdelta.databinding.ActivityLowVsHighBinding
import com.dialupdelta.model.ModelLowHigh
import com.dialupdelta.ui.anim_login.AnimLoginActivity
import org.kodein.di.generic.instance

class LowVsHighActivity : BaseActivity() {
    private var body = ""
    private var loworhigh = ""
    private val factory: GetStartViewModelFactory by instance()
    private lateinit var viewModel: GetStartViewModel
    private var traitName:String = "O"
    private lateinit var binding:ActivityLowVsHighBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding =  DataBindingUtil.setContentView(this, R.layout.activity_low_vs_high)
        initUi()
    }

    private fun initUi() {
        viewModel = ViewModelProvider(this, factory)[GetStartViewModel::class.java]
        setObserver(viewModel)
       // viewModel.getOceanDataApi(traitName)
        try {
            fragmentClick()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        binding.lowLow.setBackgroundColor(Color.GREEN)
        binding.lowLow.setTextColor(Color.WHITE)
        binding.lowLow.setOnClickListener{
            binding.lowLow.setBackgroundColor(Color.GREEN)
            loworhigh = "low"
            binding.lowLow.setTextColor(Color.WHITE)
            setDataLow()
            binding.highLow.setBackgroundColor(Color.WHITE)
            binding.highLow.setTextColor(Color.BLACK)
        }

        binding.highLow.setOnClickListener{
            loworhigh = "high"
            binding.highLow.setBackgroundColor(Color.GREEN)
            binding.highLow.setTextColor(Color.WHITE)
            setData()
            binding.lowLow.setBackgroundColor(Color.WHITE)
            binding.lowLow.setTextColor(Color.BLACK)
        }

        binding.btnLow.setOnClickListener {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
//                startActivity(new Intent(this, AnimLogin.class));
//            }else{
            startActivity(Intent(this, AnimLoginActivity::class.java))
        }
    }

    private fun fragmentClick() {
        binding.oOption.setOnClickListener {
            body = "o"
            binding.titleLow.text = "Openness"
            binding.descLow.text = viewModel.getSummaryList()?.get(0)?.question_title
            binding.guideline15.setGuidelinePercent(.5f)
            binding.descMinLow.text = viewModel.getSummaryList()?.get(0)?.low_data_point
            binding.tvPercenteageLow.setBackgroundColor(Color.GREEN)
            binding.tvPercenteageLow.text = "50%"
            binding.oOption.setBackgroundColor(Color.GREEN)
            binding.oOption.setTextColor(Color.WHITE)
            optionBackground(binding.eOption, binding.cOption, binding.aOption, binding.nOption)
        }
        binding.cOption.setOnClickListener {
            body = "c"
            binding.titleLow.text = "Conscientiousness"
            binding.descLow.text = viewModel.getSummaryList()?.get(1)?.question_title
            binding.descMinLow.text = viewModel.getSummaryList()?.get(1)?.low_data_point
            binding.guideline15.setGuidelinePercent(.4f)
            binding.tvPercenteageLow.text = "40%"
            binding.tvPercenteageLow.setBackgroundColor(Color.BLUE)
            binding.cOption.setBackgroundColor(Color.BLUE)
            binding.cOption.setTextColor(Color.WHITE)
            optionBackground(binding.oOption, binding.eOption, binding.aOption, binding.cOption)
        }
        binding.eOption.setOnClickListener {
            body = "e"
            binding.titleLow.text = "Extraversion"
            binding.guideline15.setGuidelinePercent(.7f)
            binding.descMinLow.text =viewModel.getSummaryList()?.get(2)?.low_data_point
            binding.tvPercenteageLow.text = "70%"
            binding.tvPercenteageLow.setBackgroundColor(Color.RED)
            binding.eOption.setBackgroundColor(Color.RED)
            binding.eOption.setTextColor(Color.WHITE)
            optionBackground(binding.oOption, binding.cOption, binding.aOption, binding.nOption)
            try {
                binding.descLow.text =viewModel.getSummaryList()?.get(2)?.question_title
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
        binding.aOption.setOnClickListener {
            binding.titleLow.text = "Agreeableness"
            body = "a"
            binding.guideline15.setGuidelinePercent(.4f)
            binding.descMinLow.text = viewModel.getSummaryList()?.get(3)?.low_data_point
            binding.tvPercenteageLow.text = "40%"
            binding.tvPercenteageLow.setBackgroundColor(Color.YELLOW)
            binding.aOption.setBackgroundColor(Color.YELLOW)
            binding.aOption.setTextColor(Color.WHITE)
            optionBackground(binding.oOption, binding.cOption, binding.eOption, binding.nOption)
            try {
                binding.descLow.text = viewModel.getSummaryList()?.get(3)?.question_title
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
        binding.nOption.setOnClickListener {
            binding.titleLow.text = "Neuroticism"
            body = "n"
            binding.guideline15.setGuidelinePercent(.2f)
            binding.tvPercenteageLow.text = "20%"
            binding.tvPercenteageLow.setBackgroundColor(Color.BLACK)
            binding.descMinLow.text = viewModel.getSummaryList()?.get(4)?.low_data_point
            binding.nOption.setBackgroundColor(Color.BLACK)
            binding.nOption.setTextColor(Color.WHITE)
            optionBackground(binding.oOption, binding.cOption, binding.eOption, binding.aOption)
            try {
                binding.descLow.text = viewModel.getSummaryList()?.get(4)?.question_title
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
    }
    private fun optionBackground(tv1: TextView, tv2: TextView, tv3: TextView, tv4: TextView) {
        tv1.setBackgroundColor(Color.WHITE)
        tv1.setTextColor(Color.BLACK)
        tv2.setBackgroundColor(Color.WHITE)
        tv2.setTextColor(Color.BLACK)
        tv3.setBackgroundColor(Color.WHITE)
        tv3.setTextColor(Color.BLACK)
        tv4.setBackgroundColor(Color.WHITE)
        tv4.setTextColor(Color.BLACK)
    }
    private fun setData() {
        if (body.equals("o", ignoreCase = true)) {
            binding.descMinLow.text = viewModel.getSummaryList()?.get(0)?.high_data_point
        } else if (body.equals("c", ignoreCase = true)) {
            binding.descMinLow.text = viewModel.getSummaryList()?.get(1)?.high_data_point
        } else if (body.equals("e", ignoreCase = true)) {
            binding.descMinLow.text = viewModel.getSummaryList()?.get(2)?.high_data_point
        } else if (body.equals("a", ignoreCase = true)) {
            binding.descMinLow.text = viewModel.getSummaryList()?.get(3)?.high_data_point
        } else if (body.equals("n", ignoreCase = true)) {
            binding.descMinLow.text = viewModel.getSummaryList()?.get(4)?.high_data_point
        }
    }

    private fun setDataLow() {
        if (body.equals("o", ignoreCase = true)) {
            binding.descMinLow.text = viewModel.getSummaryList()?.get(0)?.low_data_point
        } else if (body.equals("c", ignoreCase = true)) {
            binding.descMinLow.text = viewModel.getSummaryList()?.get(1)?.low_data_point
        } else if (body.equals("e", ignoreCase = true)) {
            binding.descMinLow.text = viewModel.getSummaryList()?.get(2)?.low_data_point
        } else if (body.equals("a", ignoreCase = true)) {
            binding.descMinLow.text = viewModel.getSummaryList()?.get(3)?.low_data_point
        } else if (body.equals("n", ignoreCase = true)) {
            binding.descMinLow.text = viewModel.getSummaryList()?.get(4)?.low_data_point
        }
    }

    private fun setObserver(viewModel: GetStartViewModel) {
        viewModel.getOceanData.observe(this){
            binding.titleLow.text = "Openness"
            binding.descLow.text = viewModel.getSummaryList()?.get(1)?.question_title
            binding.guideline15.setGuidelinePercent(.5f)
            binding.tvPercenteageLow.setBackgroundColor(Color.GREEN)
            binding.tvPercenteageLow.text = "50%"
            binding.oOption.setBackgroundColor(Color.GREEN)
            binding.oOption.setTextColor(Color.WHITE)
            setData()
        }
        viewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                progress?.showSweetDialog()
            } else {
                progress?.dismissSweet()
            }
        }
    }
}