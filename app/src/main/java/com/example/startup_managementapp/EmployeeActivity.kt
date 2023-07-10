package com.example.startup_managementapp

import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

data class Employee(
    val name: String = "",
    val department: String = "",
    val id: String = ""
) {
    // Add a no-argument constructor
    constructor() : this("", "", "")
}

class EmployeeActivity : AppCompatActivity() {

    private lateinit var tableLayout: TableLayout
    private lateinit var db: FirebaseFirestore
    private lateinit var employeesCollection: CollectionReference
    private lateinit var employeesListener: ListenerRegistration

    private lateinit var addEmployeeButton: Button
    private lateinit var firestore: FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_employee)

        tableLayout = findViewById(R.id.tableLayout)
        db = FirebaseFirestore.getInstance()
        employeesCollection = db.collection("employees")
        addEmployeeButton = findViewById(R.id.btnEmployee)
        firestore = FirebaseFirestore.getInstance()

        addEmployeeButton.setOnClickListener {
            showAddEmployeeDialog()
        }


        setupTable()
        loadData()
    }

    private fun showAddEmployeeDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_employee, null)

        val nameEditText = dialogView.findViewById<EditText>(R.id.etEmployeeName)
        val departmentSpinner = dialogView.findViewById<Spinner>(R.id.spnDepartments)
        val employeeRole = dialogView.findViewById<EditText>(R.id.etEmployeeRole)

        // Read department names from Firestore and populate the spinner
        firestore.collection("departments").get()
            .addOnSuccessListener { result ->
                val departments = mutableListOf<String>()

                for (document in result) {
                    val departmentName = document.getString("name")
                    departmentName?.let { departments.add(it) }
                }

                val adapter = ArrayAdapter(
                    this@EmployeeActivity,
                    android.R.layout.simple_spinner_item,
                    departments
                )
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                departmentSpinner.adapter = adapter
            }
            .addOnFailureListener { exception ->
                // Handle the error
            }

        val dialogBuilder = AlertDialog.Builder(this)
            .setView(dialogView)
            .setTitle("Add Employee")
            .setPositiveButton("Add") { dialog, _ ->
                val name = nameEditText.text.toString().trim()
                val department = departmentSpinner.selectedItem.toString()
                val role = employeeRole.text.toString().trim()

                // Add the employee data to the "employees" collection in Firestore
                val employee = hashMapOf(
                    "name" to name,
                    "department" to department,
                    "role" to role
                )

                firestore.collection("employees")
                    .add(employee)
                    .addOnSuccessListener {
                        // Data successfully added to Firestore
                        dialog.dismiss()
                    }
                    .addOnFailureListener { exception ->
                        // Handle the error
                    }
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }

        val dialog = dialogBuilder.create()
        dialog.show()
    }
    private fun setupTable() {
        val headerRow = TableRow(this)

        val nameHeader = TextView(this)
        nameHeader.text = "Name"
        nameHeader.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
        nameHeader.setTypeface(null, Typeface.BOLD)
        nameHeader.setPadding(16, 16, 16, 16)

        val departmentHeader = TextView(this)
        departmentHeader.text = "Department"
        departmentHeader.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
        departmentHeader.setTypeface(null, Typeface.BOLD)
        departmentHeader.setPadding(16, 16, 16, 16)

        val updateHeader = TextView(this)
        updateHeader.text = "Update"
        updateHeader.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
        updateHeader.setTypeface(null, Typeface.BOLD)
        updateHeader.setPadding(16, 16, 16, 16)

        val deleteHeader = TextView(this)
        deleteHeader.text = "Delete"
        deleteHeader.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
        deleteHeader.setTypeface(null, Typeface.BOLD)
        deleteHeader.setPadding(16, 16, 16, 16)

        headerRow.addView(nameHeader)
        headerRow.addView(departmentHeader)
        headerRow.addView(updateHeader)
        headerRow.addView(deleteHeader)

        tableLayout.addView(headerRow)
    }
    private fun loadData() {
        employeesListener = employeesCollection.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                // Handle error
                return@addSnapshotListener
            }

            // Clear existing table rows
            tableLayout.removeViews(1, tableLayout.childCount - 1)

            // Loop through the documents in the snapshot
            for (document in snapshot!!.documents) {
                val employee = document.toObject(Employee::class.java)

                if (employee != null) {
                    val row = TableRow(this)

                    val nameTextView = TextView(this)
                    nameTextView.text = employee.name
                    nameTextView.setPadding(16, 16, 16, 16)

                    val departmentTextView = TextView(this)
                    departmentTextView.text = employee.department
                    departmentTextView.setPadding(16, 16, 16, 16)

                    val updateButton = Button(this)
                    updateButton.text = "Update"
                    updateButton.setPadding(16, 16, 16, 16)
                    updateButton.setOnClickListener {
                        // Handle update button click
                        // You can start a new activity or show a dialog to edit the employee details
                        Toast.makeText(this, "Update clicked for ${employee.name}", Toast.LENGTH_SHORT).show()
                    }

                    val deleteButton = Button(this)
                    deleteButton.text = "Delete"
                    deleteButton.setTextColor(Color.WHITE)
                    deleteButton.setBackgroundColor(Color.RED)
                    deleteButton.setPadding(16, 16, 16, 16)
                    deleteButton.setOnClickListener {
                        // Handle delete button click
                        // You can show a confirmation dialog before deleting the employee
                        Toast.makeText(this, "Delete clicked for ${employee.name}", Toast.LENGTH_SHORT).show()
                    }

                    row.addView(nameTextView)
                    row.addView(departmentTextView)
                    row.addView(updateButton)
                    row.addView(deleteButton)

                    tableLayout.addView(row)
                }
            }
        }
    }
    override fun onStop() {
        super.onStop()
        employeesListener.remove()
    }

}