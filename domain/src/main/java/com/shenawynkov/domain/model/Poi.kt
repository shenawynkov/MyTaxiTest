package com.shenawynkov.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
 data class Poi(
    val coordinate: Coordinate,
    val fleetType: String,
    val heading: Double,
    val id: Int
):Parcelable