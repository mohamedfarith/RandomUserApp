package com.app.randomuser.ui.randomUserFragments.viewmodels

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.randomuser.models.Results
import com.app.randomuser.models.UserInfo
import com.app.randomuser.services.Resource
import com.app.randomuser.utils.LocalUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActViewModel() : ViewModel() {
    private val mainRepository: MainRepository by lazy {
        MainRepository()
    }
    private val livedata: MutableLiveData<Resource<ArrayList<Results>>> by lazy {
        MutableLiveData<Resource<ArrayList<Results>>>()
    }

    private var profileInfo: UserInfo? = null;

    fun getUserDetails(
        context: Context,
        number: Int
    ): MutableLiveData<Resource<ArrayList<Results>>> {
        if (LocalUtils.isOnline(context)) {
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

                    } else {
                        livedata.postValue(Resource.error("Server error", null))
                        loadDataFromLocal()
                    }
                }
            }
        } else {
            loadDataFromLocal()
        }



        return livedata
    }

    private fun loadDataFromLocal(): MutableLiveData<Resource<ArrayList<Results>>> {
        viewModelScope.launch(Dispatchers.IO) {

            val userInfo = mainRepository.getAllResults()
            if(userInfo!=null) {
                val resultList = userInfo.resultsList
                withContext(Dispatchers.Main) {
                    livedata.postValue(Resource.success(resultList))
                }
            }else{
                livedata.postValue(Resource.error("local source is empty",null))
            }


        }
        return livedata
    }
}
