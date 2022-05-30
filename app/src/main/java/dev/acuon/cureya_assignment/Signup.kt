package dev.acuon.cureya_assignment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dev.acuon.cureya_assignment.databinding.ActivitySignupBinding
import dev.acuon.cureya_assignment.model.User
import dev.acuon.cureya_assignment.ui.MainActivity
import java.util.regex.Pattern

class Signup : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference
    private val expression = "^[\\w.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        mAuth = FirebaseAuth.getInstance()

        binding.apply {
            backToLogin.setOnClickListener {
                val intent = Intent(this@Signup, Login::class.java)
                startActivity(intent)
            }
            signUpBtn.setOnClickListener {
                if (emptyCheck()) {
                    signUpLayout.alpha = 0.5f
                    signUpLayout.isClickable = false
                    progressbar.visibility = View.VISIBLE
                    val name = signUpName.text.toString()
                    val email = signUpEmail.text.toString()
                    val password = signUpPassword.text.toString()
                    signUp(name, email, password)
                }
            }
        }
    }

    private fun signUp(name: String, email: String, password: String) {
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    binding.progressbar.visibility = View.GONE
                    // code for if login is successful
                    //adding user to database
                    addUserToDatabase(name, email, mAuth.uid!!)
                    // i.e jumping to MainActivity
                    val intent = Intent(this@Signup, MainActivity::class.java)
                    finish()
                    startActivity(intent)
                    Toast.makeText(this@Signup, "Sign-Up Successful", Toast.LENGTH_SHORT).show()
                } else {
                    binding.apply {
                        signUpLayout.alpha = 1f
                        progressbar.visibility = View.GONE
                        signUpLayout.isClickable = true
                    }
                    // If sign in fails, display a message to the user.
                    Toast.makeText(this@Signup, "Some Error Occurred", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun addUserToDatabase(name: String, email: String, uid: String) {
        mDbRef = FirebaseDatabase.getInstance().reference
        val user = User(name, email, uid)
        mDbRef.child("users").child(uid).setValue(user)
    }

    private fun emptyCheck(): Boolean {
        var emailCheck = false
        var nameCheck = false
        var passwordCheck = false
        binding.apply {
            val email = signUpEmail.text.toString()
            var name = signUpName.text.toString()
            val password = signUpPassword.text.toString()
            if (email.isEmpty()) {
                signUpEmail.error = "Enter the email address"
            } else {
                if (isValidEmail(email)) {
                    emailCheck = true
                } else {
                    signUpEmail.error = "Enter valid email address"
                }
            }
            when {
                name.isEmpty() -> {
                    signUpName.error = "Name cannot be empty"
                }
                name.length < 3 -> {
                    signUpName.error = "Name length should be greater than 3"
                }
                else -> {
                    nameCheck = true
                }
            }
            when {
                password.length >= 6 -> {
                    passwordCheck = true
                }
                password.isEmpty() -> {
                    signUpPassword.error = "Password cannot be empty"
                }
                else -> {
                    signUpPassword.error = "Password length cannot be less then 6"
                }
            }
        }
        return emailCheck && nameCheck && passwordCheck
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