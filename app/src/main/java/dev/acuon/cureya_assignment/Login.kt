package dev.acuon.cureya_assignment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import dev.acuon.cureya_assignment.databinding.ActivityLoginBinding
import dev.acuon.cureya_assignment.ui.MainActivity
import java.util.regex.Pattern

class Login : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var mAuth: FirebaseAuth
    private val expression = "^[\\w.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        supportActionBar?.hide()
        mAuth = FirebaseAuth.getInstance()

        binding.apply {
            loginSignUpBtn.setOnClickListener {
                val intent = Intent(this@Login, Signup::class.java)
                finish()
                startActivity(intent)
            }
            loginBtn.setOnClickListener {
                if (emptyCheck()) {
                    loginLayout.alpha = 0.5f
                    loginLayout.isClickable = false
                    progressbar.visibility = View.VISIBLE
                    val email = loginEmail.text.toString()
                    val password = loginPassword.text.toString()
                    login(email, password)
                }
            }
        }
    }

    //logic for login
    private fun login(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // code for logging in user
                    binding.progressbar.visibility = View.GONE
                    val intent = Intent(this@Login, MainActivity::class.java)
                    finish()
                    startActivity(intent)
                } else {
                    binding.apply {
                        loginLayout.alpha = 1f
                        progressbar.visibility = View.GONE
                        loginLayout.isClickable = true
                    }
                    Toast.makeText(this@Login, "User does not exist", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun emptyCheck(): Boolean {
        var emailCheck = false
        var passwordCheck = false
        binding.apply {
            val email = loginEmail.text.toString()
            val password = loginPassword.text.toString()
            if (email.isEmpty()) {
                loginEmail.error = "Enter the email address"
            } else {
                if (isValidEmail(email)) {
                    emailCheck = true
                } else {
                    loginEmail.error = "Enter valid email address"
                }
            }
            when {
                password.length >= 6 -> {
                    passwordCheck = true
                }
                password.isEmpty() -> {
                    loginPassword.error = "Password cannot be empty"
                }
                else -> {
                    loginPassword.error = "Password length cannot be less then 6"
                }
            }
        }
        return emailCheck && passwordCheck
    }

    private fun isValidEmail(email: CharSequence): Boolean {
        var isValid = true
        val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(email)
        if (!matcher.matches()) {
            isValid = false
        }
        return isValid
    }
}