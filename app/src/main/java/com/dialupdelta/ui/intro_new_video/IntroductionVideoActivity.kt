package com.dialupdelta.ui.intro_new_video

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.dialupdelta.R
import com.dialupdelta.base.BaseActivity
import com.dialupdelta.data.network.MyApi
import com.dialupdelta.data.network.response.intro_video_response.IntroVideo
import com.dialupdelta.ui.get_start_activity.GetStartViewModel
import com.dialupdelta.ui.get_start_activity.GetStartViewModelFactory
import com.dialupdelta.utils.MyKeys
import org.kodein.di.generic.instance

class IntroductionVideoActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_introduction_video)
    }

    fun getVideoListUrl():IntroVideo {
        return intent.getSerializableExtra(MyKeys.introVideoData) as IntroVideo
    }
}