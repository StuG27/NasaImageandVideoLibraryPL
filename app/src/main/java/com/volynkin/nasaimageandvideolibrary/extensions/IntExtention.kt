package com.volynkin.nasaimageandvideolibrary.extensions

import android.content.Context
import android.util.DisplayMetrics


fun Int.fromDPToPixels(context: Context): Int {
    val density = context.resources.displayMetrics.densityDpi
    val pixelsInDp = density / DisplayMetrics.DENSITY_DEFAULT
    return this * pixelsInDp
}