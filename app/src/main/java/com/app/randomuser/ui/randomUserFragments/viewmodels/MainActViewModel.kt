package com.app.randomuser.ui.randomUserFragments.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.randomuser.models.Results
import com.app.randomuser.models.UserInfo
import com.app.randomuser.services.Resource
import com.app.randomuser.services.Retrofit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActViewModel() : ViewModel() {
    private var mainRepository: MainRepository = MainRepository(Retrofit.getInstance())

    private var livedata: MutableLiveData<Resource<ArrayList<Results>>> = MutableLiveData()

    private var profileInfo: UserInfo? = null;

    fun getUserDetails(number: Int): MutableLiveData<Resource<ArrayList<Results>>> {

        viewModelScope.launch(Dispatchers.IO) {
            livedata.postValue(Resource.loading(null))
            val response = mainRepository.getUserList(number)

            withContext(Dispatchers.Main) {
                if (response.code() == 200 && response.isSuccessful) {
                    //response modified case
                    if (profileInfo == null) {
                        response.body()?.resultsList?.let {
                            profileInfo =
                                UserInfo(response.body()?.resultsList, response.body()?.info, 1)
                        }
                    } else {
                        response.body()?.resultsList?.let {
                            profileInfo?.resultsList?.addAll(it)
                        }
                    }
                    livedata.postValue(Resource.success(profileInfo?.resultsList))
                    mainRepository.insertAll(profileInfo)

                } else if (response.code() == 304) {
                    //response not modified case
                    //fetch from local
                } else {
                    livedata.postValue(Resource.error("Server error", null))
                }
            }
        }



        return livedata
    }

    fun loadDataFromLocal(): MutableLiveData<Resource<ArrayList<Results>>> {
        viewModelScope.launch(Dispatchers.IO) {

            val resultList = mainRepository.getAllResults().resultsList

            withContext(Dispatchers.Main) {
                livedata.postValue(Resource.success(resultList))

            }

        }
        return livedata
    }
}
