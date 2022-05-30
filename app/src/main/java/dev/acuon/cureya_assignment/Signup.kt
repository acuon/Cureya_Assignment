package dev.acuon.cureya_assignment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dev.acuon.cureya_assignment.databinding.ActivitySignupBinding
import dev.acuon.cureya_assignment.model.User
import dev.acuon.cureya_assignment.ui.MainActivity

class Signup : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference

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
                val name = signUpName.text.toString()
                val email = signUpEmail.text.toString()
                val password = signUpPassword.text.toString()
                signUp(name, email, password)
            }
        }
    }

    private fun signUp(name: String, email: String, password: String) {
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // code for if login is successful
                    //adding user to database
                    addUserToDatabase(name, email, mAuth.uid!!)
                    // i.e jumping to MainActivity
                    val intent = Intent(this@Signup, MainActivity::class.java)
                    finish()
                    startActivity(intent)
                    Toast.makeText(this@Signup, "Sign-Up Successful", Toast.LENGTH_SHORT).show()
                } else {
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
}