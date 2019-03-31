package com.lkb.assignment

import com.lkb.assignment.model.Conversion
import java.math.RoundingMode
import java.text.DecimalFormat

class CurrencyConverter {
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
        df.roundingMode = RoundingMode.CEILING
        return df
    }

    fun updateCurrRate(rates: List<Conversion>) {
        for (index in 0..(rates.size - 1)) {
            when {
                rates[index].from.contains("INR") && rates[index].to.contains("AED") -> {
                    rateINRtoAED = rates[index].rate.toDouble()
                }
                rates[index].from.contains("AED") && rates[index].to.contains("INR") -> {
                    rateAEDtoINR = rates[index].rate.toDouble()
                }
                rates[index].from.contains("SAR") && rates[index].to.contains("INR") -> {
                    rateSARtoINR = rates[index].rate.toDouble()
                }
                rates[index].from.contains("INR") && rates[index].to.contains("SAR") -> {
                    rateINRtoSAR = rates[index].rate.toDouble()
                }
                rates[index].from.contains("AED") && rates[index].to.contains("SAR") -> {
                    rateAEDtoSAR = rates[index].rate.toDouble()
                }
                rates[index].from.contains("SAR") && rates[index].to.contains("AED") -> {
                    rateSARtoAED = rates[index].rate.toDouble()
                }
            }
        }
    }
}