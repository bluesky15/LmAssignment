package com.lkb.assignment.model

data class ResponseModel(
        val conversion: List<Conversion>,
        val products: List<Product>,
        val title: String
)