package com.windusk.ecosystemBiteUi.element.part

import android.os.Bundle
import com.windusk.ecosystemBiteUi.element.Element
import kotlinx.parcelize.Parcelize

@Parcelize
open class Part protected constructor(
    override val bundle: Bundle
): Element(bundle) {
    companion object: CreationTools<Part>(::Part)

    override fun setEnabled(newEnabled: Boolean) = this.apply {
        this.enabled.set(newEnabled)
    }

}