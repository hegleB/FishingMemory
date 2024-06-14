package com.qure.model.camera

data class ObjectRect<T : Number>(
    val top: T,
    val bottom: T,
    val left: T,
    val right: T,
)

fun ObjectRect<Int>.toFloatRect(): ObjectRect<Float> {
    return ObjectRect(
        top = this.top.toFloat(),
        bottom = this.bottom.toFloat(),
        left = this.left.toFloat(),
        right = this.right.toFloat(),
    )
}

fun ObjectRect<Float>.toIntRect(): ObjectRect<Int> {
    return ObjectRect(
        top = this.top.toInt(),
        bottom = this.bottom.toInt(),
        left = this.left.toInt(),
        right = this.right.toInt(),
    )
}
