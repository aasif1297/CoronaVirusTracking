package com.cs.coronavirustrackingappmvvm.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.cs.coronavirustrackingappmvvm.model.TrackingResponseModel
import com.cs.coronavirustrackingappmvvm.retrofit.RetrofitService
import com.cs.coronavirustrackingappmvvm.retrofit.RetrofitService.createService
import com.cs.coronavirustrackingappmvvm.retrofit.TrackingApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response


class TrackingRepository {
    private val trackingApi: TrackingApi = createService(TrackingApi::class.java)
    suspend fun getTrackingList(source: String?, key: String?): MutableLiveData<TrackingResponseModel?> {
        val trackingData: MutableLiveData<TrackingResponseModel?> =
            MutableLiveData<TrackingResponseModel?>()
        val response = trackingApi.getTrackingList()
        try {
            if (response?.isSuccessful!!) {
                trackingData.value = response.body()
            } else {
                trackingData.value = null
                Log.e("TrackingRepository", "Error: ${response.code()}")
            }
        } catch (e: HttpException) {
            Log.e("TrackingRepository", "Exception ${e.message}")
        } catch (e: Throwable) {
            Log.e("TrackingRepository", "Ooops: Something else went wrong")
        }

//        trackingApi.getTrackingList()!!.enqueue(object : Callback<TrackingResponseModel?> {
//            override fun onFailure(call: Call<TrackingResponseModel?>, t: Throwable) {
//                trackingData.value = null
//            }
//
//            override fun onResponse(call: Call<TrackingResponseModel?>, response: Response<TrackingResponseModel?>) {
//                if (response.isSuccessful) {
//                    trackingData.value = response.body()
//                }
//            }
//        })
        return trackingData
    }

    companion object {
        private var trackingRepository: TrackingRepository? = null
        val instance: TrackingRepository?
            get() {
                if (trackingRepository == null) {
                    trackingRepository = TrackingRepository()
                }
                return trackingRepository
            }
    }
}