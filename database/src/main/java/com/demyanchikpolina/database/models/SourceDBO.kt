package com.demyanchikpolina.database.models

import androidx.room.ColumnInfo

data class SourceDBO(

    @ColumnInfo("id") val sourceId: String?,

    @ColumnInfo("name") val name: String?,
)
