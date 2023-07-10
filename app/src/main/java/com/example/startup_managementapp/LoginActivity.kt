package com.example.startup_managementapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)

        btnLogin = findViewById(R.id.btnLogin)
        btnLogin.setOnClickListener {
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()
            login(email, password)
        }

    }
    private fun login(email: String, password: String) {
        val db = FirebaseFirestore.getInstance()
        val usersCollection = db.collection("users")
        val employeesCollection = db.collection("employees")

        usersCollection.whereEqualTo("email", email)
            .get()
            .addOnCompleteListener { userTask ->
                if (userTask.isSuccessful) {
                    val userQuerySnapshot = userTask.result
                    if (!userQuerySnapshot.isEmpty) {
                        // User exists in the "users" collection
                        val user = userQuerySnapshot.documents[0]
                        val userId = user.id

                        // Now you can compare the password or use Firebase Authentication
                        // to authenticate the user

                        // Redirect to corresponding activity based on user's role
                        val intent: Intent? = when (user.getString("role")) {
                            "admin" -> Intent(this@LoginActivity, AdminActivity::class.java)
                            "employee" -> {
                                // Check if the user also exists in the "employees" collection
                                employeesCollection.document(userId).get()
                                    .addOnCompleteListener { employeeTask ->
                                        if (employeeTask.isSuccessful && employeeTask.result.exists()) {
                                            // Employee exists in the "employees" collection
                                            val employee = employeeTask.result
                                            val employeeRole = employee.getString("role")

                                            val intent: Intent? = when (employeeRole) {
                                                "manager" -> Intent(this, ManagerActivity::class.java)
                                                "staff" -> Intent(this, StaffActivity::class.java)
                                                else -> null
                                            }

                                            if (intent != null) {
                                                startActivity(intent)
                                                finish()
                                            } else {
                                                Toast.makeText(this, "Invalid employee role", Toast.LENGTH_SHORT).show()
                                            }
                                        } else {
                                            // Employee does not exist in the "employees" collection
                                            Toast.makeText(this, "Invalid user or employee", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                null // Add this line to return null outside the addOnCompleteListener block
                            }

                            else -> null
                        }

                        if (intent != null) {
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this, "Invalid user role", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        // User does not exist in the "users" collection
                        // Check if the user exists in the "employees" collection
                        employeesCollection.whereEqualTo("email", email)
                            .get()
                            .addOnCompleteListener { employeeTask ->
                                if (employeeTask.isSuccessful) {
                                    val employeeQuerySnapshot = employeeTask.result
                                    if (!employeeQuerySnapshot.isEmpty) {
                                        // Employee exists in the "employees" collection
                                        val employee = employeeQuerySnapshot.documents[0]

                                        // Now you can compare the password or use Firebase Authentication
                                        // to authenticate the employee

                                        // Redirect to corresponding activity based on employee's role
                                        val intent: Intent? = when (employee.getString("role")) {
                                            "manager" -> Intent(this, ManagerActivity::class.java)
                                            "staff" -> Intent(this, StaffActivity::class.java)
                                            else -> null
                                        }

                                        if (intent != null) {
                                            startActivity(intent)
                                            finish()
                                        } else {
                                            Toast.makeText(this, "Invalid employee role", Toast.LENGTH_SHORT).show()
                                        }
                                    } else {
                                        // Employee does not exist in the "employees" collection
                                        Toast.makeText(this, "Invalid user or employee", Toast.LENGTH_SHORT).show()
                                    }
                                } else {
                                    // Error fetching employee data
                                    Toast.makeText(this, "Error fetching employee data", Toast.LENGTH_SHORT).show()
                                }
                            }
                    }
                } else {
                    // Error fetching user data
                    Toast.makeText(this, "Error fetching user data", Toast.LENGTH_SHORT).show()
                }
            }
    }



}