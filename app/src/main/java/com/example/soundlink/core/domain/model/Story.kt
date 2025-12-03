package com.example.soundlink.core.domain.model

data class Story(
    val id: Long,
    val user: User,
    val isSeen: Boolean,     // Para cambiar el estilo visual del anillo
    val timestamp: Long,      // Cuándo fue subido
    val active: Boolean,
    val duration: Long,
)

//Content like image or video URL are not stored here for brevity so we used a placeholder
