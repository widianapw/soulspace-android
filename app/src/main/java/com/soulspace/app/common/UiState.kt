package com.soulspace.app.common


sealed class UiEvents {
    data class SnackbarEvent(val message: String) : UiEvents()
    data class SuccessEvent(val data: Any? = null) : UiEvents()
}