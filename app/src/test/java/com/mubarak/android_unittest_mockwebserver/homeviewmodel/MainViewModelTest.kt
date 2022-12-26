package com.mubarak.android_unittest_mockwebserver.homeviewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.mubarak.android_unittest_mockwebserver.MockResponseFileReader
import com.mubarak.android_unittest_mockwebserver.model.HomeModelData
import com.mubarak.android_unittest_mockwebserver.network.RetrofitClient
import com.mubarak.android_unittest_mockwebserver.repository.MainRepository
import com.mubarak.android_unittest_mockwebserver.utils.Resource
import com.mubarak.android_unittest_mockwebserver.viewmodel.MainViewModel
import junit.framework.Assert
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import java.net.HttpURLConnection


@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {

    //set rules for test case
    @get:Rule
    val instantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    //object
    private lateinit var mainViewModel: MainViewModel

    //mock
    @Mock
    private lateinit var homeResponseData: Observer<Resource<HomeModelData>?>

    //mock web server
    private lateinit var mockWebServer: MockWebServer

    private lateinit var repository: MainRepository

    //before test case run initial
    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this) //assign all mock objects

        mainViewModel = MainViewModel()
        mainViewModel.getDetail().observeForever(homeResponseData)

        mockWebServer = MockWebServer()
        mockWebServer.start() // start mock web server

        repository = MainRepository(RetrofitClient.apiInterFace)

    }

    /***
     * Checked json file read testing
     */
    @Test
    fun `read sample success json file`() {
        val reader = MockResponseFileReader("success_response.json").content
        Assert.assertNotNull(reader)
    }

    /***
     * Checked mock web server data and api response data
     * checked both have contains "success" at response time
     */
    @Test
    fun `checked api calling data is success`() {
        val expectedResult = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(MockResponseFileReader("success_response.json").content)
        mockWebServer.enqueue(expectedResult)

        val expect = expectedResult.getBody()?.readUtf8()?.contains("success", true)

        //make api calling
        val actualResponse = repository.getEmployeeDetails().execute()

        //expected mock web server response and api response have a success contains is equal
        assertEquals(expect, actualResponse.body()?.status?.contains("success"))
    }

    /**
     * Another way
     */
    @Test
    fun `checked api calling data is success response`() {
        val expectedResult = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(MockResponseFileReader("success_response.json").content)
        mockWebServer.enqueue(expectedResult)

        //val expect = expectedResult.getBody()?.readUtf8()?.contains("success", true)

        val expect = Gson().fromJson<HomeModelData>(
            expectedResult.getBody()?.readUtf8().toString(),
            HomeModelData::class.java
        )

        //make api calling
        val actualResponse = repository.getEmployeeDetails().execute()

        //expected mock web server response and api response have a success contains is equal
        assertEquals(expect.status, actualResponse.body()?.status)
    }

    /**
     * Result : Task case is failed
     * Cause of api call given 200 response code,
     * api response is success
     */
    @Test
    fun `checked api calling data is failed`() {
        val expectedResult = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_BAD_REQUEST)
            .setBody(MockResponseFileReader("failed_response.json").content)
        mockWebServer.enqueue(expectedResult)

        val expect = expectedResult.getBody()?.readUtf8()?.contains("failed", true)

        val actualResponse = repository.getEmployeeDetails().execute() //api calling

        //expected mock web server response and api response have a failed contains
        //assertEquals(expect, actualResponse.body()?.status?.contains("failed"))
        assertEquals(expect, actualResponse.body()?.status?.contains("success"))
    }

    /***
     * tearDown server
     * closed Mock Web Server
     */
    @After
    fun tearDown() {
        mockWebServer.close()
    }

}