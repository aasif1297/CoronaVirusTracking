package com.cs.coronavirustrackingappmvvm.model

data class Field(
    val alias: String,
    val defaultValue: Any,
    val domain: Any,
    val length: Int,
    val name: String,
    val sqlType: String,
    val type: String
)