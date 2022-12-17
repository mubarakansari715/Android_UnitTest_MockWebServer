package com.mubarak.android_unittest_mockwebserver.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mubarak.android_unittest_mockwebserver.model.HomeModelData
import com.mubarak.android_unittest_mockwebserver.network.RetrofitClient
import com.mubarak.android_unittest_mockwebserver.repository.MainRepository
import com.mubarak.android_unittest_mockwebserver.utils.Resource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {

    val result = MutableLiveData<Resource<HomeModelData>?>()

    fun fetchData() {
        val apiCall = MainRepository(RetrofitClient.apiInterFace)
        apiCall.getEmployeeDetails()
            .enqueue(object : Callback<HomeModelData> {
                override fun onResponse(
                    call: Call<HomeModelData>,
                    response: Response<HomeModelData>
                ) {
                    result.postValue(Resource.success(response.body()))
                }

                override fun onFailure(call: Call<HomeModelData>, t: Throwable) {
                    result.postValue(Resource.error("Something went wrong! $t", null))
                }
            })
    }

    //This fun return success data
    fun getDetail(): LiveData<Resource<HomeModelData>?> {
        return result
    }
}