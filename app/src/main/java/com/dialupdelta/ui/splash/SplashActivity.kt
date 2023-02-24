package com.dialupdelta.ui.splash


import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import com.dialupdelta.R
import com.dialupdelta.base.BaseActivity
import com.dialupdelta.data.network.response.get_language_response.Data
import com.dialupdelta.data.repositories.Repository
import com.dialupdelta.databinding.ActivitySplashBinding
import com.dialupdelta.ui.intro_screen.IntroductionFirstVideoActivity
import com.dialupdelta.ui.login_signup.LoginActivity
import com.dialupdelta.ui.transition.TransitionActivity
import com.dialupdelta.utils.*
import org.kodein.di.generic.instance

class SplashActivity : BaseActivity() {
    private val repository: Repository by instance()
    private lateinit var binding:ActivitySplashBinding
    private var languageList:ArrayList<Data> = ArrayList()
    private var adapterPosition:Int? = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)

        initUi()
    }

    private fun initUi() {

        binding.buttonSplashSubmit.setOnClickListener {

 //           Handler(Looper.getMainLooper()).postDelayed({
//                if (repository.getLogin()){
//                    Intent(this, TransitionActivity::class.java).also {
//                        startActivity(it)
//                        finish()
//                    }
//                }
//                else{
                    Intent(this, IntroductionFirstVideoActivity::class.java).also {
                        startActivity(it)
                    }
//                }
//            },1000)

        }

        binding.loginOnSplash.setOnClickListener {
                Intent(this, LoginActivity::class.java).also {
                    startActivity(it)
                }
        }
        getLanguageApi()
    }
    private fun selectLanguage() {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, languageList)
        binding.spinnerLanguageSplash.adapter = adapter
        binding.spinnerLanguageSplash.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?, position: Int, id: Long
            ) {
                repository.setLanguage(languageList[position].id )
            }
            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
    }

    private fun getLanguageApi(){
        progress?.showSweetDialog()
        Coroutines.io {
            try {
                val languageResponse = repository.getLanguageApi()
                Coroutines.main {
                    progress?.dismissSweet()
                    if (languageResponse.status) {
                        languageList.addAll(languageResponse.result)
                        selectLanguage()
                    }
                    return@main
                }
            } catch (e: ApiException) {
                progress?.dismissSweet()
            } catch (e: NoInternetException) {
                progress?.dismissSweet()
            }
        }
    }
}