package com.cs.coronavirustrackingappmvvm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cs.coronavirustrackingappmvvm.model.TrackingResponseModel
import com.cs.coronavirustrackingappmvvm.repository.TrackingRepository

class TrackingViewModel: ViewModel() {
    private var mutableLiveData: MutableLiveData<TrackingResponseModel?>? = null
    private var trackingRepository: TrackingRepository? = null
    suspend fun init(){
        if (mutableLiveData != null){
            return
        }

        trackingRepository = TrackingRepository.instance
        mutableLiveData = trackingRepository!!.getTrackingList(null, null)
    }

    fun getTrackingRepository(): LiveData<TrackingResponseModel?>?{
        return mutableLiveData
    }
}