package com.lkb.assignment

import java.math.RoundingMode
import java.text.DecimalFormat

class CurrencyConverter{
    //TODO use the database to store and update the rate
    //TODO with timestamp
    var rateINRtoAED = 0.057
    var rateAEDtoINR = 17.1
    var rateINRtoSAR = 0.058
    var rateSARtoINR = 17.2
    var rateSARtoAED = 0.98
    var rateAEDtoSAR = 1.02

    fun convertINRtoAED(fromCurr: Double): String? {
        val df = getDecimalFormatter()
        return df.format(fromCurr * rateINRtoAED)
    }
    fun convertAEDtoINR(fromCurr: Double): String? {
        val df = getDecimalFormatter()
        return df.format(fromCurr * rateAEDtoINR)
    }
    fun convertINRtoSAR(fromCurr: Double): String? {
        val df = getDecimalFormatter()
        return df.format(fromCurr * rateINRtoSAR)
    }
    fun convertSARtoINR(fromCurr: Double): String? {
        val df = getDecimalFormatter()
        return df.format(fromCurr * rateSARtoINR)
    }
    fun convertSARtoAED(fromCurr: Double): String? {
        val df = getDecimalFormatter()
        return df.format(fromCurr * rateSARtoAED)
    }
    fun convertAEDtoSAR(fromCurr: Double): String? {
        val df = getDecimalFormatter()
        return df.format(fromCurr * rateAEDtoSAR)
    }


    private fun getDecimalFormatter(): DecimalFormat {
        val df = DecimalFormat("#.##")
        df.roundingMode =RoundingMode.CEILING
        return df
    }


}