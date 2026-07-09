package com.windusk.clientBasics.data.sharings.search

import com.windusk.clientBasics.data.sharings.saver.ConflictSaver
import com.windusk.clientBasics.data.sharings.saver.PrioritySaver
import com.windusk.clientBasics.data.sharings.saver.SharingSaver

class SearchContext(
    val sharingSaver: SharingSaver,
    val prioritySaver: PrioritySaver,
    val conflictSaver: ConflictSaver
)