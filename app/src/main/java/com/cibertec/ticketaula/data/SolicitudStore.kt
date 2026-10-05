package com.cibertec.ticketaula.data

import android.content.ContentValues
import android.content.Context
import com.cibertec.ticketaula.model.Solicitud

// SolicitudStore funciona como repositorio del modelo: oculta los detalles de SQLite
// y entrega objetos Solicitud a los controladores de la interfaz.
class SolicitudStore(context: Context) {

    private val dbHelper = TicketAulaDbHelper(context.applicationContext)

    fun agregar(
        nombre: String,
        descripcion: String,
        categoria: String,
        prioridad: String,
        rutaImagen: String?
    ): Long {
        // PRÁCTICA TEMA 6 - BLOQUE 3:
        // 1. Crear ContentValues y relacionar las cinco columnas con sus valores.
        // 2. Abrir writableDatabase.
        // 3. Retornar el resultado de insert().
        // -1 mantiene la plantilla ejecutable hasta completar la inserción.

        val valores = ContentValues().apply {
            put(SolicitudContrato.COLUMNA_NOMBRE, nombre)
            put(SolicitudContrato.COLUMNA_DESCRIPCION, descripcion)
            put(SolicitudContrato.COLUMNA_CATEGORIA, categoria)
            put(SolicitudContrato.COLUMNA_PRIORIDAD, prioridad)
            put(SolicitudContrato.COLUMNA_RUTA_IMAGEN, rutaImagen)
        }

        return dbHelper.writableDatabase.insert(
            SolicitudContrato.TABLA_SOLICITUDES,
            null,
            valores
        )
    }

    fun obtenerTodas(): List<Solicitud> {
        // PRÁCTICA TEMA 6 - BLOQUE 4A (READ):
        // 1. Crear una MutableList<Solicitud> y el arreglo de columnas.
        // 2. Ejecutar query() sobre readableDatabase.
        // 3. Recorrer el Cursor con while (cursor.moveToNext()).
        // 4. Construir cada Solicitud, agregarla a la lista y retornar el resultado.
        val solicitudes = mutableListOf<Solicitud>()
        val columnas = arrayOf(
            SolicitudContrato.COLUMNA_ID,
            SolicitudContrato.COLUMNA_NOMBRE,
            SolicitudContrato.COLUMNA_DESCRIPCION,
            SolicitudContrato.COLUMNA_CATEGORIA,
            SolicitudContrato.COLUMNA_PRIORIDAD,
            SolicitudContrato.COLUMNA_RUTA_IMAGEN,

        )

        dbHelper.readableDatabase.query(
            SolicitudContrato.TABLA_SOLICITUDES,
            columnas,
            null,
            null,
            null,
            null,
            "${SolicitudContrato.COLUMNA_ID} DESC"

        ).use { cursor ->
            val indiceId = cursor.getColumnIndexOrThrow(SolicitudContrato.COLUMNA_ID)
            val indiceNombre = cursor.getColumnIndexOrThrow(SolicitudContrato.COLUMNA_NOMBRE)
            val indiceDescripcion = cursor.getColumnIndexOrThrow(SolicitudContrato.COLUMNA_DESCRIPCION)
            val indiceCategoria = cursor.getColumnIndexOrThrow(SolicitudContrato.COLUMNA_CATEGORIA)
            val indicePrioridad = cursor.getColumnIndexOrThrow(SolicitudContrato.COLUMNA_PRIORIDAD)
            val indiceImagen = cursor.getColumnIndexOrThrow(SolicitudContrato.COLUMNA_RUTA_IMAGEN)

            while (cursor.moveToNext()) {
                solicitudes.add(
                    Solicitud(
                        id = cursor.getInt(indiceId),
                        nombre = cursor.getString(indiceNombre),
                        descripcion = cursor.getString(indiceDescripcion),
                        categoria = cursor.getString(indiceCategoria),
                        prioridad = cursor.getString(indicePrioridad),
                        rutaImagen = if (cursor.isNull(indiceImagen)) {
                            null
                        } else {
                            cursor.getString(indiceImagen)
                        }
                    )
                )
            }

        }

        return solicitudes
    }

    fun actualizar(solicitud: Solicitud): Int {
        // PRÁCTICA TEMA 6 - BLOQUE 4B (UPDATE):
        // 1. Crear ContentValues con los datos de solicitud.
        // 2. Llamar a writableDatabase.update().
        // 3. Usar "id = ?" y arrayOf(solicitud.id.toString()) como condición segura.
        // 4. Retornar la cantidad de filas actualizadas.
        val valores = ContentValues().apply {
            put(SolicitudContrato.COLUMNA_NOMBRE, solicitud.nombre)
            put(SolicitudContrato.COLUMNA_DESCRIPCION, solicitud.descripcion)
            put(SolicitudContrato.COLUMNA_CATEGORIA, solicitud.categoria)
            put(SolicitudContrato.COLUMNA_PRIORIDAD, solicitud.prioridad)
            put(SolicitudContrato.COLUMNA_RUTA_IMAGEN, solicitud.rutaImagen)
        }
        return  dbHelper.writableDatabase.update(
            SolicitudContrato.TABLA_SOLICITUDES,
            valores,
            "${SolicitudContrato.COLUMNA_ID} = ?",
            arrayOf(solicitud.id.toString())
        )
    }

    fun eliminar(id: Int): Int {
        // PRÁCTICA TEMA 6 - BLOQUE 4C (DELETE):
        // 1. Llamar a writableDatabase.delete().
        // 2. Usar "id = ?" y arrayOf(id.toString()) como condición segura.
        // 3. Retornar la cantidad de filas eliminadas.

        return dbHelper.writableDatabase.delete(
            SolicitudContrato.TABLA_SOLICITUDES,
            "${SolicitudContrato.COLUMNA_ID} = ?",
            arrayOf(id.toString())
        )
    }
}
