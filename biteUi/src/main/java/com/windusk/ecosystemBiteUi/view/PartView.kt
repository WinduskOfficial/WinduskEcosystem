package com.windusk.ecosystemBiteUi.view

import android.os.Parcelable
import com.windusk.ecosystemBiteUi.element.part.Part
import kotlinx.parcelize.Parcelize

@Parcelize
class PartView(
    val info: ViewInfo,
    var parts: List<Part>
) : Parcelable {
    companion object {
        fun build(
            constructor: PartView.() -> Unit
        ) = PartView(
            ViewInfo.new(),
            emptyList()
        ).apply(constructor)
    }

    fun addPart(constructor: () -> Part): Part {
        val part = constructor()

        parts = parts + part

        return part
    }
}