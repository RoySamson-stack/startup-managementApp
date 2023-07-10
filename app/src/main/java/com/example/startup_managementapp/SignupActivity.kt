package com.example.startup_managementapp

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore

class SignupActivity : AppCompatActivity() {
    private lateinit var nameEditText: EditText
    private lateinit var usernameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText


    private lateinit var signupButton:Button

    private val firestore: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }
    data class User(
        val name: String = "",
        val username: String = "",
        val email: String = "",
        val password: String = ""
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        nameEditText = findViewById(R.id.etName)
        usernameEditText = findViewById(R.id.etUsername)
        emailEditText = findViewById(R.id.etEmail)
        passwordEditText = findViewById(R.id.etPassword)

        signupButton = findViewById(R.id.btnSignUp)
        signupButton.setOnClickListener {
            signUpUser()
        }

    }
    private fun signUpUser() {
        val name = nameEditText.text.toString()
        val username = usernameEditText.text.toString()
        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()

        // You should perform validation on the data here before proceeding with signup

        // Create a new user object with the data
        val user = User(name, username, email, password)

        // Store the user data in Firestore under the "Users" collection
        firestore.collection("users")
            .add(user)
            .addOnSuccessListener { documentReference ->
                     Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")


            }
            .addOnFailureListener {e ->
                Log.w(TAG, "Error adding document", e)
            }
    }
}