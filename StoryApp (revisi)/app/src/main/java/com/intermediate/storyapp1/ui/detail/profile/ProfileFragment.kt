package com.intermediate.storyapp1.ui.detail.profile

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.model.LatLng
import com.intermediate.storyapp1.R
import com.intermediate.storyapp1.data.model.LoginModel
import com.intermediate.storyapp1.data.model.LoginPreference
import com.intermediate.storyapp1.data.model.SettingPreferences
import com.intermediate.storyapp1.data.viewmodel.SettingViewModel
import com.intermediate.storyapp1.data.viewmodel.SettingViewModelFactory
import com.intermediate.storyapp1.databinding.FragmentProfileBinding
import com.intermediate.storyapp1.ui.login.LoginActivity
import com.intermediate.storyapp1.ui.maps.MapsFragment
import java.util.ArrayList

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var mLoginPreference: LoginPreference
    private lateinit var loginModel: LoginModel
    private var listMap : ArrayList<LatLng>? = null
    private var listMapName: ArrayList<String>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val ProfileViewModel =
            ViewModelProvider(this).get(ProfileViewModel::class.java)

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        mLoginPreference = LoginPreference(root.context)
        loginModel = mLoginPreference.getUser()

        playAnimation()
        setupUi()
        logoutHandler()
        languageHandler()
        mapsHandler()
        getCurrentTheme(root.context)
        changeThemeHandler(root.context)

        return root
    }

    private fun setupUi() {
        binding.nameTextView.text = loginModel.name
    }

    private fun logoutHandler() {
        binding.buttonLogout.setOnClickListener {
            mLoginPreference.removeUser()
            val i = Intent(activity, LoginActivity::class.java)
            startActivity(i)
            activity?.finish()
        }
    }

    private fun mapsHandler() {
        binding.buttonMaps.setOnClickListener(View.OnClickListener  {
            val mapsFragment = MapsFragment()
            val transaction: FragmentTransaction =  fragmentManager!!.beginTransaction()
            transaction.replace(R.id.buttonMaps,mapsFragment)
            transaction.commit()
        })
    }

    private fun languageHandler() {
        binding.languageCardView.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }
    }

    private fun getCurrentTheme(context: Context) {
        var themeTextView = binding.themeTextView
        val pref = context.let { SettingPreferences.getInstance(it.dataStore) }
        val settingViewModel = ViewModelProvider(this, SettingViewModelFactory(pref)).get(
            SettingViewModel::class.java
        )

        settingViewModel.getThemeSettings().observe(viewLifecycleOwner
        ) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.switchTheme.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.switchTheme.isChecked = false
            }

        }
    }

    private fun changeThemeHandler(context: Context) {
        val pref = context.let { SettingPreferences.getInstance(it.dataStore) }
        val settingViewModel = ViewModelProvider(this, SettingViewModelFactory(pref)).get(
            SettingViewModel::class.java
        )
        binding.switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            settingViewModel.saveThemeSetting(isChecked)
        }
    }

    private fun playAnimation() {
        val userDetailCardView = ObjectAnimator.ofFloat(binding.userDetailCardView, View.ALPHA, 1f).setDuration(500)
        val settingTextView = ObjectAnimator.ofFloat(binding.settingTextView, View.ALPHA, 1f).setDuration(500)
        val themeCardView = ObjectAnimator.ofFloat(binding.themeCardView, View.ALPHA, 1f).setDuration(500)
        val languageCardView = ObjectAnimator.ofFloat(binding.languageCardView, View.ALPHA, 1f).setDuration(500)
        val buttonLogout = ObjectAnimator.ofFloat(binding.buttonLogout, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(userDetailCardView, settingTextView, themeCardView, languageCardView, buttonLogout)
            start()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}