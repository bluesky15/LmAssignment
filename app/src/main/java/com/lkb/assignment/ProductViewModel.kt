package com.lkb.assignment

import androidx.lifecycle.ViewModel
import com.lkb.assignment.model.Conversion
import com.lkb.assignment.model.Product
import com.lkb.assignment.model.ResponseModel
import io.reactivex.Observable

class ProductViewModel : ViewModel() {
    private val lmService by lazy {
        LmService.create()
    }
    var responseModel: Observable<ResponseModel>? = null
    /**
     * Method to call the API
     * In case of state change it returns the saved data.
     */
    fun callProductApi(): Observable<ResponseModel> {
        return if (responseModel == null) {
            lmService.retrieveData()
        } else responseModel!!

    }

    /**
     * Method to refresh the api data on demand
     */
    fun refreshProductData(): Observable<ResponseModel> {
        return lmService.retrieveData()
    }

    /**
     * Method to change the currency to INR
     */
    fun toInr(
        pdata: List<Product>,
        currencyConverter: CurrencyConverter,
        rate: List<Conversion>
    ): Observable<List<Product>> {
        currencyConverter.updateCurrRate(rate)
        for (index in 0..(pdata.size - 1)) {
            if (pdata[index].currency.contains("AED")) {
                pdata[index].price = currencyConverter.convertAEDtoINR(pdata[index].price.toDouble())!!
                pdata[index].currency = "INR"
            } else if (pdata[index].currency.contains("SAR")) {
                pdata[index].price = currencyConverter.convertSARtoINR(pdata[index].price.toDouble())!!
                pdata[index].currency = "INR"
            }
        }
        return Observable.just(pdata)
    }
    /**
     * Method to change the currency to AED
     */
    fun toAed(
        pdata: List<Product>,
        currencyConverter: CurrencyConverter,
        rate: List<Conversion>
    ): Observable<List<Product>> {
        currencyConverter.updateCurrRate(rate)
        for (index in 0..(pdata.size - 1)) {
            if (pdata[index].currency.contains("INR")) {
                pdata[index].price = currencyConverter.convertINRtoAED(pdata[index].price.toDouble())!!
                pdata[index].currency = "AED"
            } else if (pdata[index].currency.contains("SAR")) {
                pdata[index].price = currencyConverter.convertSARtoAED(pdata[index].price.toDouble())!!
                pdata[index].currency = "AED"
            }
        }
        return Observable.just(pdata)
    }
    /**
     * Method to change the currency to SAR
     */
    fun toSar(
        pdata: List<Product>,
        currencyConverter: CurrencyConverter,
        rate: List<Conversion>
    ): Observable<List<Product>> {

        currencyConverter.updateCurrRate(rate)
        for (index in 0..(pdata.size - 1)) {
            if (pdata[index].currency.contains("INR")) {
                pdata[index].price = currencyConverter.convertINRtoSAR(pdata[index].price.toDouble())!!
                pdata[index].currency = "SAR"
            } else if (pdata[index].currency.contains("AED")) {
                pdata[index].price = currencyConverter.convertAEDtoSAR(pdata[index].price.toDouble())!!
                pdata[index].currency = "SAR"
            }
        }
        return Observable.just(pdata)
    }

}