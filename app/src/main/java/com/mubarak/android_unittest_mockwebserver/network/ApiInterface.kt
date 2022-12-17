package com.mubarak.android_unittest_mockwebserver.network

import com.mubarak.android_unittest_mockwebserver.model.HomeModelData
import retrofit2.Call
import retrofit2.http.GET

interface ApiInterface {

    @GET("api/v1/employees")
     fun getEmployeeDetails(): Call<HomeModelData>
}