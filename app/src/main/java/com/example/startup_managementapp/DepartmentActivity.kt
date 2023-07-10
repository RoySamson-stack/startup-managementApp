package com.example.startup_managementapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

data class Department(val name: String, val otherField: String) {
    // Add other fields you want to display in the RecyclerView
}
class DepartmentAdapter(private val departments: List<Department>) :
    RecyclerView.Adapter<DepartmentAdapter.DepartmentViewHolder>() {

    class DepartmentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val departmentNameTextView: TextView = itemView.findViewById(R.id.departmentNameTextView)
        // Add other views you want to display the data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DepartmentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_department, parent, false)
        return DepartmentViewHolder(view)
    }

    override fun onBindViewHolder(holder: DepartmentViewHolder, position: Int) {
        val department = departments[position]
        holder.departmentNameTextView.text = department.name
        // Bind other data to the respective views
    }

    override fun getItemCount(): Int {
        return departments.size
    }
}

class DepartmentActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private val departmentList = mutableListOf<Department>()
    private val departmentAdapter = DepartmentAdapter(departmentList)



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_department)

        val createDepartmentButton = findViewById<Button>(R.id.createDepartmentButton)
        createDepartmentButton.setOnClickListener {
            showCreateDepartmentDialog()
        }
        val recyclerView = findViewById<RecyclerView>(R.id.departmentTable)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = departmentAdapter

        // Fetch department data from Firestore
        fetchDepartments()



    }
    private fun showCreateDepartmentDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dalog_createdeaprment, null)
        val editTextDepartmentName = dialogView.findViewById<EditText>(R.id.editTextDepartmentName)
        val buttonCreate = dialogView.findViewById<Button>(R.id.buttonCreate)

        val dialogBuilder = AlertDialog.Builder(this)
            .setView(dialogView)
            .setTitle("Create Department")

        val alertDialog = dialogBuilder.create()
        alertDialog.show()

        // Handle "Create" button click
        buttonCreate.setOnClickListener {
            val departmentName = editTextDepartmentName.text.toString().trim()
            if (departmentName.isNotEmpty()) {
                // Create a new department document in the "departments" collection
                val departmentData = hashMapOf(
                    "name" to departmentName
                    // Add other data you want to save along with the department name
                )

                // Add the department data to the "departments" collection
                db.collection("departments")
                    .add(departmentData)
                    .addOnSuccessListener {
                        // Department data added successfully
                        alertDialog.dismiss()
                    }
                    .addOnFailureListener { e ->
                        // Handle any errors that occurred during the operation
                        Toast.makeText(this, "Failed to add department: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            } else {
                editTextDepartmentName.error = "Department name cannot be empty."
            }
        }
    }
    private fun fetchDepartments() {
        db.collection("departments")
            .get()
            .addOnSuccessListener { result ->
                departmentList.clear()
                for (document in result) {
                    val name = document.getString("name")
                    val otherField = document.getString("otherField")
                    val department = Department(name ?: "", otherField ?: "")
                    departmentList.add(department)
                }
                departmentAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                // Handle any errors that occurred during the data retrieval
                Toast.makeText(this, "Failed to fetch departments: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}