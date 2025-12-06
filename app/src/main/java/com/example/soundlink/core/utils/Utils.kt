package com.example.soundlink.core.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

fun timestampToLegible(timestamp: Long): String {

    val now = System.currentTimeMillis()
    val diff = now - timestamp   // diferencia en ms

    // Si aún no ha pasado 1 hora
    if (diff < TimeUnit.HOURS.toMillis(1)) {
        val minutes = TimeUnit.MILLISECONDS.toMinutes(diff)
        return "Hace ${minutes.coerceAtLeast(1)} min"
    }

    // Si aun no han pasado 24 horas → "Hace X horas"
    if (diff < TimeUnit.DAYS.toMillis(1)) {
        val hours = TimeUnit.MILLISECONDS.toHours(diff)
        return "Hace $hours horas"
    }

    // Más de 24 horas → fecha legible
    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    return sdf.format(Date(timestamp))
}