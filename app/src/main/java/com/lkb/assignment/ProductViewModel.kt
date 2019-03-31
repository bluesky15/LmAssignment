package com.lkb.assignment

import androidx.lifecycle.ViewModel
import com.lkb.assignment.model.ResponseModel
import io.reactivex.Observable

class ProductViewModel : ViewModel() {
    private val lmService by lazy {
        LmService.create()
    }
    var responseModel: Observable<ResponseModel>?=null

    fun callProductApi(): Observable<ResponseModel> {
        return if (responseModel==null) {
            lmService.retrieveData()
        } else responseModel!!

    }

    fun refreshProductData(): Observable<ResponseModel> {
            return lmService.retrieveData()
    }

}