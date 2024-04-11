package com.demyanchikpolina.newsapi.utils

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

internal object DateTimeUTCSerializer : KSerializer<Date> {
    override val descriptor = PrimitiveSerialDescriptor("Date", PrimitiveKind.STRING)

    private val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.UK)

    override fun deserialize(decoder: Decoder): Date =
        formatter.parse(decoder.decodeString())

    override fun serialize(encoder: Encoder, value: Date) =
        encoder.encodeString(value.toString())
}