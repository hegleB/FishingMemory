package com.qure.camera.utils

fun widthConvert(x: Int, imageWidth: Int, screenWidth: Int): Int {
    val ratio = screenWidth.toFloat() / imageWidth
    return (x * ratio).toInt()
}

fun heightConvert(y: Int, imageHeight: Int, screenHeight: Int): Int {
    val height = screenHeight.toFloat()
    val ratio = height / imageHeight
    return (y * ratio).toInt()
}