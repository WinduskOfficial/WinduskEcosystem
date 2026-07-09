package com.windusk.ecosystemBiteUi.element.part.lib

import android.os.Bundle
import com.windusk.ecosystemBiteUi.element.part.Part
import com.windusk.ecosystemBiteUi.view.PartView
import com.windusk.lazybundle.StringProperty
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
class Text private constructor(
    override val bundle: Bundle
): Part(bundle) {
    companion object: CreationTools<Text>(::Text) {
        const val TYPE = "text"

        const val BIG_TITLE_STYLE = "big_title"
        const val MEDIUM_TITLE_STYLE = "medium_title"
        const val SMALL_TITLE_STYLE = "small_title"

        const val BIG_TEXT_STYLE = "big_text"
        const val MEDIUM_TEXT_STYLE = "medium_text"
        const val SMALL_TEXT_STYLE = "small_text"

        fun PartView.text(
            key: String,
            text: String,
            style: String = MEDIUM_TEXT_STYLE
        ) = addPart {
            new().apply {
                this.key.set(key)
                this.text.set(text)
                this.style.set(style)
            }
        }
    }

    init {
        type.set(TYPE)
    }

    @IgnoredOnParcel
    val text = StringProperty(this, "text")

    @IgnoredOnParcel
    val style = StringProperty(this, "style")
}