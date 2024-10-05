package com.example.countriesapp.data

import com.example.countriesapp.R
import com.example.countriesapp.model.Country

class DataSource {
    fun getCountries()= listOf(
        Country(R.string.egypt, R.drawable.egyptian_flag, 29.9774614,31.1329645),
        Country(R.string.palestine, R.drawable.palestinian_flag, 31.9485, 35.9301),
        Country(R.string.madagascar, R.drawable.madagascar_flag, -18.9769, 47.5884),
        Country(R.string.russia, R.drawable.russian_flag, 55.7558,37.6173)
    )

}