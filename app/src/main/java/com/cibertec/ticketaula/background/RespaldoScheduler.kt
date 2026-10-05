package com.cibertec.ticketaula.background

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context

object RespaldoScheduler {

    fun programar(context: Context): Int {
        // PRÁCTICA TEMA 7 - BLOQUE 6:
        // 1. Crear ComponentName con RespaldoJobService.
        // 2. Construir JobInfo con latencia mínima y plazo máximo.
        // 3. Obtener JobScheduler y retornar schedule(tarea).
        val componente = ComponentName( context, RespaldoJobService::class.java)
        val tarea = JobInfo.Builder(ID_TRABAJO_RESPALDO, componente)
            .setMinimumLatency(DEMORA_MINIMA_MS)
            .setOverrideDeadline(PLAZO_MAXIMO_MS)
            .build()
        val scheduler = context.getSystemService(JobScheduler::class.java)


        return scheduler.schedule(tarea)
    }

    const val PREFERENCIAS_RESPALDO = "estado_respaldo"
    const val CLAVE_ULTIMO_RESPALDO = "ultimo_respaldo"

    private const val ID_TRABAJO_RESPALDO = 6001
    private const val DEMORA_MINIMA_MS = 1_000L
    private const val PLAZO_MAXIMO_MS = 5_000L
}
