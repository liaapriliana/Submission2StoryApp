package com.intermediate.storyapp1.ui.splashscreen

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.intermediate.storyapp1.data.constant.Constant
import com.intermediate.storyapp1.data.model.LoginModel
import com.intermediate.storyapp1.data.model.LoginPreference
import com.intermediate.storyapp1.data.model.SettingPreferences
import com.intermediate.storyapp1.data.viewmodel.SettingViewModel
import com.intermediate.storyapp1.data.viewmodel.SettingViewModelFactory
import com.intermediate.storyapp1.databinding.ActivitySplashScreenBinding
import com.intermediate.storyapp1.ui.login.LoginActivity
import com.intermediate.storyapp1.ui.main.MainActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    private lateinit var mLoginPreference: LoginPreference
    private lateinit var loginModel: LoginModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mLoginPreference = LoginPreference(this)

        loginModel = mLoginPreference.getUser()

        themeSettingHandler()
        splashScreenHandler()
    }

    private fun splashScreenHandler() {
        if (loginModel.name != null && loginModel.userId != null && loginModel.token != null) {
            val intent = Intent(this, MainActivity::class.java)
            navigate(intent)
        } else {
            val intent = Intent(this, LoginActivity::class.java)
            navigate(intent)
        }
    }

    private fun navigate(intent: Intent) {
        val SPLASH_TIME : Long = 2000
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(intent)
            finish()
        }, SPLASH_TIME)
    }

    private fun themeSettingHandler() {
        val pref = SettingPreferences.getInstance(dataStore)
        val settingViewModel = ViewModelProvider(this, SettingViewModelFactory(pref)).get(
            SettingViewModel::class.java
        )
        settingViewModel.getThemeSettings().observe(this
        ) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }

        }
    }
}