package com.mubarak.android_unittest_mockwebserver

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.mubarak.android_unittest_mockwebserver.utils.Status
import com.mubarak.android_unittest_mockwebserver.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]

        mainViewModel.fetchData()

        mainViewModel.result.observe(this) { status ->

            if (status?.status == null) return@observe

            when (status.status) {
                Status.LOADING -> {
                    Log.e(TAG, "onCreate: Loading")
                }
                Status.SUCCESS -> {

                    Log.e(TAG, "onCreate: SUCCESS : ${status.responseData}")

                    status.responseData?.data?.let {
                        txtResponseData.text =
                            "Employee Name : ${it[0].employee_name} \n"+"Employee Salary : ${it[0].employee_salary}"
                    }


                }
                Status.ERROR -> {
                    Log.e(TAG, "onCreate: Error ${status.message}")
                }
            }
        }

    }
}