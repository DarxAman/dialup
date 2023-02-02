package com.dialupdelta.ui.transition

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.dialupdelta.R
import com.dialupdelta.base.BaseActivity
import com.dialupdelta.databinding.ActivityTransitionBinding
import com.dialupdelta.ui.get_to_sleep.GetToSleepFragment
import com.dialupdelta.ui.journal.JournalFragment
import com.dialupdelta.ui.sleep_enhancer.SleepEnhancerFragment
import com.dialupdelta.ui.wake_up.WakeUpFragment
import kotlinx.coroutines.flow.combine

class TransitionActivity : BaseActivity() {
    private lateinit var binding:ActivityTransitionBinding
    private lateinit var getToSleepFragment:GetToSleepFragment
    private lateinit var sleepEnhancerFragment: SleepEnhancerFragment
    private lateinit var wakeUpFragment: WakeUpFragment
    private lateinit var journalFragment: JournalFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding  = DataBindingUtil.setContentView(this, R.layout.activity_transition)
        initUI()
    }

    private fun initUI() {
        getToSleepFragment = GetToSleepFragment()
        wakeUpFragment = WakeUpFragment()
        sleepEnhancerFragment = SleepEnhancerFragment()
        journalFragment = JournalFragment()

        setCurrentFragment(getToSleepFragment)

        binding.bottomNavigation.setOnItemSelectedListener {
            when(it.itemId){
                R.id.getToSleep->setCurrentFragment(getToSleepFragment)
                R.id.sleepEnhancer->setCurrentFragment(sleepEnhancerFragment)
                R.id.wakeUp->setCurrentFragment(wakeUpFragment)
                R.id.journal->setCurrentFragment(journalFragment)

            }
            true
        }
    }

    private fun setCurrentFragment(fragment: Fragment)=
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.container,fragment)
            commit()
        }

    fun navigateToSleepEnhancerFragment(){
        setCurrentFragment(sleepEnhancerFragment)
    }
}
