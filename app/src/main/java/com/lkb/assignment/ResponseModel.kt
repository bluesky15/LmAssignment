package com.lkb.assignment

data class ResponseModel(
        val conversion: List<Conversion>,
        val products: List<Product>,
        val title: String
)