package com.soulspace.app.presentation.psychologist.navigation

import com.soulspace.app.domain.model.NavigationItem

data class NavigationState (
    val isLoading: Boolean = false,
    val error: String = "",
    val list: List<NavigationItem> = emptyList()
)
