package com.dialupdelta.ui.splash


import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import com.dialupdelta.R
import com.dialupdelta.base.BaseActivity
import com.dialupdelta.data.network.response.get_language_response.LanguageData
import com.dialupdelta.data.repositories.Repository
import com.dialupdelta.databinding.ActivitySplashBinding
import com.dialupdelta.ui.intro_screen.IntroductionFirstVideoActivity
import com.dialupdelta.ui.login_signup.LoginActivity
import com.dialupdelta.ui.transition.TransitionActivity
import com.dialupdelta.utils.*
import org.kodein.di.generic.instance
import java.util.Locale

class SplashActivity : BaseActivity() {
    private val repository: Repository by instance()
    private lateinit var binding:ActivitySplashBinding
    private var languageList:ArrayList<LanguageData> = ArrayList()
    private var selectedId:Int? = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)

        initUi()
    }

    private fun initUi() {

        binding.buttonSplashSubmit.setOnClickListener {
            val customProgressDialog = CustomProgressDialog(this)
            customProgressDialog.showSweetDialog()
            val lang = Locale.getDefault().language;
            val languageIdOrName = getLanguageIdOrLanguageName(lang)
            val languageId: Int? = languageIdOrName.toIntOrNull()

            Log.e("languageId -> ", lang)

            if (languageId != null) {
                // If it's a number (language ID), do something with it
                selectedId?.let { it1 -> repository.setLanguage(it1) }
                // Perform actions specific to a numeric language ID
            } else {
                // If it's not a number (language name), do something else with it
                repository.setLanguage(1)
                // Perform actions specific to a language name
            }

            Log.e("getLogin  : ", repository.getLogin().toString())
            Handler(Looper.getMainLooper()).postDelayed({
                customProgressDialog.dismissSweet()
                if (repository.getLogin()){
                    Intent(this, TransitionActivity::class.java).also {
                        startActivity(it)
                        finish()
                    }
                }
                else{
                    Intent(this, IntroductionFirstVideoActivity::class.java).also {
                        startActivity(it)
                    }
                }
            },1000)

        }

        binding.loginOnSplash.setOnClickListener {
                Intent(this, LoginActivity::class.java).also {
                    startActivity(it)
                }
        }
        getLanguageApi()
    }

    fun getLanguageIdOrLanguageName(systemLanguage: String): String {
        val languageMap = mapOf(
            "en" to "1",
            "sp" to "2",
            "hn" to "3",
            "de" to "4",
            "ko" to "5",
            "zh" to "6",
            "ja" to "7"
            // Add more mappings as needed
        )

        // Convert the system language to lowercase for case-insensitive matching
        val lowercaseSystemLanguage = systemLanguage.toLowerCase()

        // Try to find a matching ID or language name in the map
        return languageMap[lowercaseSystemLanguage] ?: lowercaseSystemLanguage
    }


    private fun selectLanguage() {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, languageList)
        binding.spinnerLanguageSplash.adapter = adapter
        binding.spinnerLanguageSplash.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?, position: Int, id: Long
            ) {
                selectedId = languageList[position].id

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
                            languageList.addAll(languageResponse.result.languageData)
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