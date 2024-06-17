package com.qure.ui.model

enum class SnackBarType {
    NETWORK_ERROR,
    SUCCESS,
    ERROR,
}

enum class SnackBarMessageType {
    SAVE_MEMO,
    DELETE_MEMO,
    UPDATE_MEMO,
    DELETE_ALL_BOOKMARK,
    CAMERA_CAPTURE_SUCCESS,
    CAMERA_CAPTURE_FAILURE,
    CAMERA_CAPTURE_DETECT_FAILURE,
    PERMISSION_FAILURE;

    companion object {
         private const val FAILURE_MESSAGE = "FAILURE"
        fun SnackBarMessageType.isFailureMessageType(): Boolean {
            return this.name.contains(FAILURE_MESSAGE)
        }
    }
}

