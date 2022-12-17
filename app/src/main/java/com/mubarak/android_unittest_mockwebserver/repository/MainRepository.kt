package com.mubarak.android_unittest_mockwebserver.repository

import com.mubarak.android_unittest_mockwebserver.model.HomeModelData
import com.mubarak.android_unittest_mockwebserver.network.ApiInterface
import retrofit2.Call

class MainRepository(private val apiInterface: ApiInterface) {

    fun getEmployeeDetails(): Call<HomeModelData> = apiInterface.getEmployeeDetails()
}