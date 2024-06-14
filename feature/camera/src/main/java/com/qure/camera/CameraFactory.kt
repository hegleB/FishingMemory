package com.qure.camera

object CameraFactory {
    fun create(): CameraOperations = CameraManager()
}