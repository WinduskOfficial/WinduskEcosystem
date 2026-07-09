package com.windusk.ecosystemBiteUi.element.part.lib

import com.windusk.ecosystemBiteUi.element.part.lib.Text.Companion.text
import com.windusk.ecosystemBiteUi.element.part.lib.ViewPart.Companion.viewPart
import com.windusk.ecosystemBiteUi.view.PartView
import com.windusk.ecosystemBiteUi.view.ViewInfo

fun getTextDescriptionView(
    key: String,
    text: String,
    description: String
) = PartView.build {
    info.type.set(ViewInfo.COLUMN_TYPE)

    text(key = "${key}_text", text = text, style = Text.BIG_TEXT_STYLE)
    text(key = "${key}_description", text = description, style = Text.MEDIUM_TEXT_STYLE)
}

fun PartView.textDescription(
    key: String,
    text: String,
    description: String
) = viewPart(key, getTextDescriptionView(key, text, description))