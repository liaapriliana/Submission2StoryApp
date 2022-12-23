package com.intermediate.storyapp1.ui.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.intermediate.storyapp1.R
import com.intermediate.storyapp1.data.utils.isValidEmail
import com.intermediate.storyapp1.data.utils.validateMinLength
import com.intermediate.storyapp1.databinding.ActivityRegisterBinding
import com.intermediate.storyapp1.ui.customView.CustomAlertDialog
import com.intermediate.storyapp1.utils.ViewModelFactory
import com.intermediate.storyapp1.data.presenter.Result

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var factory: ViewModelFactory
    private val registerViewModel: RegisterViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        playAnimation()
        setupToolbar()
        backButtonHandler()
        emailEditTextHandler()
        nameEditTextHandler()
        passwordEditTextHandler()
        registerButtonHandler()
        setMyButtonEnable()
    }
        private fun setupViewModel() {
            factory = ViewModelFactory.getInstance(binding.root.context)
        }


    private fun setupToolbar() {
        title = resources.getString(R.string.register)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return true
    }

    private fun backButtonHandler() {
        binding.registerLayout.registerButton.setOnClickListener {
            finish()
        }
    }

    private fun loadingHandler(isLoading: Boolean) {
        if (isLoading) {
            binding.loadingLayout.root.visibility = View.VISIBLE
            binding.registerLayout.root.visibility = View.GONE
        } else {
            binding.loadingLayout.root.visibility = View.GONE
            binding.registerLayout.root.visibility = View.VISIBLE
        }
    }

    private fun errorHandler(){
            CustomAlertDialog(this, R.string.error_message, R.drawable.tryagain).show()

    }

    private fun registerHandler() {
        CustomAlertDialog(this, R.string.success_create_user, R.drawable.user_created).show()
            binding.registerLayout.emailEditText.text?.clear()
            binding.registerLayout.passwordEditText.text?.clear()
            binding.registerLayout.nameEditText.text?.clear()

    }

    private fun registerButtonHandler() {
        binding.registerLayout.registerButton.setOnClickListener {
            val email = binding.registerLayout.emailEditText.text.toString()
            val password = binding.registerLayout.passwordEditText.text.toString()
            val name = binding.registerLayout.nameEditText.text.toString()

            if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(name)) {
                registerViewModel.postRegister(name, email, password)
            } else {
                CustomAlertDialog(this, R.string.error_validation, R.drawable.error_form).show()
            }
        }
    }

    private fun handlingRegister(name: String, email: String, password: String) {
        registerViewModel.postRegister(name, email, password).observe(this@RegisterActivity) { result ->
            if (result != null) {
                when(result) {
                    is Result.Loading -> {
                        loadingHandler(true)
                    }
                    is Result.Error -> {
                        loadingHandler(false)
                        errorHandler()
                    }
                    is Result.Success -> {
                        loadingHandler(false)
                        registerHandler()
                    }
                }
            }
        }
    }

    private fun playAnimation() {
        val registerAkun = ObjectAnimator.ofFloat(binding.registerLayout.registerAkun, View.ALPHA, 1f)
            .setDuration(500)
        val emailTextView =
            ObjectAnimator.ofFloat(binding.registerLayout.emailTextView, View.ALPHA, 1f)
                .setDuration(500)
        val emailEditTextLayout =
            ObjectAnimator.ofFloat(binding.registerLayout.emailEditTextLayout, View.ALPHA, 1f)
                .setDuration(500)
        val emailEditText =
            ObjectAnimator.ofFloat(binding.registerLayout.emailEditText, View.ALPHA, 1f)
                .setDuration(500)

        val nameTextView =
            ObjectAnimator.ofFloat(binding.registerLayout.nameTextView, View.ALPHA, 1f)
                .setDuration(500)
        val nameEditTextLayout =
            ObjectAnimator.ofFloat(binding.registerLayout.nameEditTextLayout, View.ALPHA, 1f)
                .setDuration(500)
        val nameEditText =
            ObjectAnimator.ofFloat(binding.registerLayout.nameEditText, View.ALPHA, 1f)
                .setDuration(500)

        val passwordTextView =
            ObjectAnimator.ofFloat(binding.registerLayout.passwordTextView, View.ALPHA, 1f)
                .setDuration(500)
        val passwordEditTextLayout =
            ObjectAnimator.ofFloat(binding.registerLayout.passwordEditTextLayout, View.ALPHA, 1f)
                .setDuration(500)
        val passwordEditText =
            ObjectAnimator.ofFloat(binding.registerLayout.passwordEditText, View.ALPHA, 1f)
                .setDuration(500)

        val registerButton =
            ObjectAnimator.ofFloat(binding.registerLayout.registerButton, View.ALPHA, 1f)
                .setDuration(500)




        AnimatorSet().apply {
            playSequentially(
                registerAkun, emailTextView, emailEditTextLayout, emailEditText,
                nameTextView, nameEditTextLayout, nameEditText,
                passwordTextView, passwordEditTextLayout, passwordEditText, registerButton
            )
            start()
        }
    }

    private fun setMyButtonEnable() {
        val emailEditText = binding.registerLayout.emailEditText.text
        val passwordEditText = binding.registerLayout.passwordEditText.text
        val nameEditText = binding.registerLayout.nameEditText.text
        binding.registerLayout.registerButton.isEnabled =
            isValidEmail(emailEditText.toString()) && validateMinLength(nameEditText.toString()) && validateMinLength(
                passwordEditText.toString()
            )
    }

    private fun emailEditTextHandler() {
        val emailEditText = binding.registerLayout.emailEditText
        emailEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                setMyButtonEnable()
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
    }

    private fun nameEditTextHandler() {
        val nameEditText = binding.registerLayout.nameEditText
        nameEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (validateMinLength(nameEditText.text.toString())) {
                    nameEditText.error = null
                } else {
                    nameEditText.error = getString(R.string.invalid_name)
                }
                setMyButtonEnable()
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
    }

    private fun passwordEditTextHandler() {
        binding.registerLayout.passwordEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                setMyButtonEnable()
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
    }


    }
