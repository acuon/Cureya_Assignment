package dev.acuon.cureya_assignment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import dev.acuon.cureya_assignment.databinding.ActivityLoginBinding
import dev.acuon.cureya_assignment.ui.MainActivity

class Login : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var mAuth: FirebaseAuth

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
                val email = loginEmail.text.toString()
                val password = loginPassword.text.toString()
                login(email, password)
            }
        }
    }

    //logic for login
    private fun login(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // code for logging in user
                    val intent = Intent(this@Login, MainActivity::class.java)
                    finish()
                    startActivity(intent)
                } else {
                    Toast.makeText(this@Login, "User does not exist", Toast.LENGTH_SHORT).show()
                }
            }
    }
}