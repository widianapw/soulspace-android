package com.soulspace.app.presentation.psychologist.psychologist

import com.soulspace.app.domain.model.PsychologistItem

data class PsychologistState(
    val isLoading: Boolean = false,
    val error: String = "",
    val list: List<PsychologistItem> = emptyList()
)