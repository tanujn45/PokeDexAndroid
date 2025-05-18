package com.tanujn45.pokedex.models

data class NamedApiResource(
    val name: String
)

data class NamedUrlApiResource(
    val name: String,
    val url: String
)

fun NamedUrlApiResource.toId(): Int =
    url.trimEnd('/').substringAfterLast('/').toIntOrNull() ?: error("Invalid URL: $url")

data class UrlApiResource(
    val url: String
)

fun UrlApiResource.toId(): Int =
    url.trimEnd('/').substringAfterLast('/').toIntOrNull() ?: error("Invalid URL: $url")
