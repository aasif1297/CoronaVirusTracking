package com.cs.coronavirustrackingappmvvm.repository

import androidx.lifecycle.MutableLiveData
import com.cs.coronavirustrackingappmvvm.model.TrackingResponseModel
import com.cs.coronavirustrackingappmvvm.retrofit.RetrofitService
import com.cs.coronavirustrackingappmvvm.retrofit.RetrofitService.createService
import com.cs.coronavirustrackingappmvvm.retrofit.TrackingApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class TrackingRepository {
    private val trackingApi: TrackingApi = createService(TrackingApi::class.java)
    fun getTrackingList(source: String?, key: String?): MutableLiveData<TrackingResponseModel?> {
        val trackingData: MutableLiveData<TrackingResponseModel?> =
            MutableLiveData<TrackingResponseModel?>()
        trackingApi.getTrackingList(

        )!!.enqueue(object : Callback<TrackingResponseModel?> {
            override fun onFailure(call: Call<TrackingResponseModel?>, t: Throwable) {
                trackingData.value = null
            }

            override fun onResponse(
                call: Call<TrackingResponseModel?>,
                response: Response<TrackingResponseModel?>
            ) {
                if (response.isSuccessful) {
                    trackingData.value = response.body()
                }
            }

        })
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