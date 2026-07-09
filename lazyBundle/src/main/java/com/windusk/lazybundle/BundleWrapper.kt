package com.windusk.lazybundle

import android.os.Bundle
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlin.reflect.KClass

@Parcelize
open class BundleWrapper protected constructor(
    protected open val bundle: Bundle
): Parcelable {
    class NotSetException(key: String): IllegalStateException("Bundle key \"$key\" not set.")
    class AlreadySetException(key: String): IllegalStateException("Bundle key \"$key\" already set.")

    abstract class CreationTools<T : BundleWrapper>(private val constructor: (Bundle) -> T) {
        fun new(): T = import(Bundle())
        fun import(bundle: Bundle): T = constructor(bundle)
    }

    companion object : CreationTools<BundleWrapper>(::BundleWrapper)

    abstract class Property<T>(
        private val wrapper: BundleWrapper
    ) {
        abstract val canReplace: Boolean

        protected abstract val key: String
        protected abstract val default: T?

        protected abstract fun getter(bundle: Bundle): T
        protected abstract fun setter(bundle: Bundle, newValue: T)

        fun get() = when {
            exist() -> getter(wrapper.bundle)
            else -> default ?: throw NotSetException(key)
        }

        fun getOrNull(): T? = try {
            get()
        } catch (_: NotSetException) { null }

        fun set(newValue: T) {
            if (!canReplace && exist()) return
            setter(wrapper.bundle, newValue)
        }

        fun exist(): Boolean = wrapper.bundle.containsKey(key)
    }

    fun export() = bundle

    fun <T : BundleWrapper> exportAs(clazz: KClass<T>): T {
        val companionField = clazz.java.declaredFields.find { it.name == "Companion" }
            ?.apply { isAccessible = true }
            ?: throw IllegalArgumentException("No Companion found for ${clazz.simpleName}")

        val companion = companionField.get(null)
                as? CreationTools<*>
            ?: throw IllegalArgumentException("No CreationTools found for ${clazz.simpleName}")

        return companion.import(this.bundle) as T
    }
}