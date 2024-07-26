package com.rubenlr.demo

//import org.springframework.boot.context.properties.ConfigurationProperties

//@ConfigurationProperties("exchange")
data class ExchangeProprieties(var title: String, val banner: Banner) {
    data class Banner(val title: String? = null, val content: String)
}