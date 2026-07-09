package com.windusk.lazybundle

import android.os.Bundle
import android.os.IBinder
import android.os.IInterface
import android.os.Parcelable
import java.io.Serializable

class BooleanProperty(
    wrapper: BundleWrapper,
    override val key: String,
    override val canReplace: Boolean = false,
    override val default: Boolean? = null
): BundleWrapper.Property<Boolean>(wrapper) {
    override fun getter(bundle: Bundle) = bundle.getBoolean(key)
    override fun setter(bundle: Bundle, newValue: Boolean) = bundle.putBoolean(key, newValue)
}

class BooleanArrayProperty(
    wrapper: BundleWrapper,
    override val key: String,
    override val canReplace: Boolean = false,
    override val default: BooleanArray? = null
): BundleWrapper.Property<BooleanArray>(wrapper) {
    override fun getter(bundle: Bundle) = bundle.getBooleanArray(key) ?: BooleanArray(0)
    override fun setter(bundle: Bundle, newValue: BooleanArray) = bundle.putBooleanArray(key, newValue)
}

class DoubleProperty(
    wrapper: BundleWrapper,
    override val key: String,
    override val canReplace: Boolean = false,
    override val default: Double? = null
): BundleWrapper.Property<Double>(wrapper) {
    override fun getter(bundle: Bundle) = bundle.getDouble(key)
    override fun setter(bundle: Bundle, newValue: Double) = bundle.putDouble(key, newValue)
}

class DoubleArrayProperty(
    wrapper: BundleWrapper,
    override val key: String,
    override val canReplace: Boolean = false,
    override val default: DoubleArray? = null
): BundleWrapper.Property<DoubleArray>(wrapper) {
    override fun getter(bundle: Bundle) = bundle.getDoubleArray(key) ?: DoubleArray(0)
    override fun setter(bundle: Bundle, newValue: DoubleArray) = bundle.putDoubleArray(key, newValue)
}

class FloatProperty(
    wrapper: BundleWrapper,
    override val key: String,
    override val canReplace: Boolean = false,
    override val default: Float? = null
): BundleWrapper.Property<Float>(wrapper) {
    override fun getter(bundle: Bundle) = bundle.getFloat(key)
    override fun setter(bundle: Bundle, newValue: Float) = bundle.putFloat(key, newValue)
}

class FloatArrayProperty(
    wrapper: BundleWrapper,
    override val key: String,
    override val canReplace: Boolean = false,
    override val default: FloatArray? = null
): BundleWrapper.Property<FloatArray>(wrapper) {
    override fun getter(bundle: Bundle) = bundle.getFloatArray(key) ?: FloatArray(0)
    override fun setter(bundle: Bundle, newValue: FloatArray) = bundle.putFloatArray(key, newValue)
}

class IntProperty(
    wrapper: BundleWrapper,
    override val key: String,
    override val canReplace: Boolean = false,
    override val default: Int? = null
): BundleWrapper.Property<Int>(wrapper) {
    override fun getter(bundle: Bundle) = bundle.getInt(key)
    override fun setter(bundle: Bundle, newValue: Int) = bundle.putInt(key, newValue)
}

class IntArrayProperty(
    wrapper: BundleWrapper,
    override val key: String,
    override val canReplace: Boolean = false,
    override val default: IntArray? = null
): BundleWrapper.Property<IntArray>(wrapper) {
    override fun getter(bundle: Bundle) = bundle.getIntArray(key) ?: IntArray(0)
    override fun setter(bundle: Bundle, newValue: IntArray) = bundle.putIntArray(key, newValue)
}

class LongProperty(
    wrapper: BundleWrapper,
    override val key: String,
    override val canReplace: Boolean = false,
    override val default: Long? = null
): BundleWrapper.Property<Long>(wrapper) {
    override fun getter(bundle: Bundle) = bundle.getLong(key)
    override fun setter(bundle: Bundle, newValue: Long) = bundle.putLong(key, newValue)
}

class LongArrayProperty(
    wrapper: BundleWrapper,
    override val key: String,
    override val canReplace: Boolean = false,
    override val default: LongArray? = null
): BundleWrapper.Property<LongArray>(wrapper) {
    override fun getter(bundle: Bundle) = bundle.getLongArray(key) ?: LongArray(0)
    override fun setter(bundle: Bundle, newValue: LongArray) = bundle.putLongArray(key, newValue)
}

class StringProperty(
    wrapper: BundleWrapper,
    override val key: String,
    override val canReplace: Boolean = false,
    override val default: String? = null
): BundleWrapper.Property<String>(wrapper) {
    override fun getter(bundle: Bundle) = bundle.getString(key)!!
    override fun setter(bundle: Bundle, newValue: String) = bundle.putString(key, newValue)
}

class StringArrayProperty(
    wrapper: BundleWrapper,
    override val key: String,
    override val canReplace: Boolean = false,
    override val default: Array<String>? = null
): BundleWrapper.Property<Array<String>>(wrapper) {
    override fun getter(bundle: Bundle) = bundle.getStringArray(key) ?: emptyArray()
    override fun setter(bundle: Bundle, newValue: Array<String>) = bundle.putStringArray(key, newValue)
}

class BundleProperty(
    wrapper: BundleWrapper,
    override val key: String,
    override val canReplace: Boolean = false,
    override val default: Bundle? = null
): BundleWrapper.Property<Bundle>(wrapper) {
    override fun getter(bundle: Bundle) = bundle.getBundle(key) ?: Bundle()
    override fun setter(bundle: Bundle, newValue: Bundle) = bundle.putBundle(key, newValue)
}

class BinderProperty<T : IBinder, I : IInterface>(
    wrapper: BundleWrapper,
    override val key: String,
    override val canReplace: Boolean = false,
    override val default: T? = null,
    private val asInterface: (IBinder) -> I
): BundleWrapper.Property<T>(wrapper) {
    @Suppress("DEPRECATION", "UNCHECKED_CAST")
    override fun getter(bundle: Bundle) = (bundle.getBinder(key) as? T)!!
    override fun setter(bundle: Bundle, newValue: T) = bundle.putBinder(key, newValue)

    fun getInterface(): I = asInterface(get())
    fun getInterfaceOrNull() = getOrNull()?.let { asInterface(it) }
}

class SerializableProperty<T : Serializable>(
    wrapper: BundleWrapper,
    override val key: String,
    override val canReplace: Boolean = false,
    override val default: T? = null
): BundleWrapper.Property<T>(wrapper) {
    @Suppress("DEPRECATION", "UNCHECKED_CAST")
    override fun getter(bundle: Bundle) = (bundle.getSerializable(key) as? T)!!
    override fun setter(bundle: Bundle, newValue: T) = bundle.putSerializable(key, newValue)
}

class ParcelableProperty<T : Parcelable>(
    wrapper: BundleWrapper,
    override val key: String,
    override val canReplace: Boolean = false,
    override val default: T? = null
): BundleWrapper.Property<T>(wrapper) {
    @Suppress("DEPRECATION", "UNCHECKED_CAST")
    override fun getter(bundle: Bundle) = (bundle.getParcelable(key) as? T)!!
    override fun setter(bundle: Bundle, newValue: T) = bundle.putParcelable(key, newValue)
}

class ParcelableArrayProperty<T : Parcelable>(
    wrapper: BundleWrapper,
    override val key: String,
    override val canReplace: Boolean = false,
    override val default: Array<T>? = null
): BundleWrapper.Property<Array<T>>(wrapper) {
    @Suppress("DEPRECATION", "UNCHECKED_CAST")
    override fun getter(bundle: Bundle) = bundle.getParcelableArray(key).orEmpty() as Array<T>
    override fun setter(bundle: Bundle, newValue: Array<T>) = bundle.putParcelableArray(key, newValue)
}

class EnumProperty<T : Enum<T>>(
    wrapper: BundleWrapper,
    override val key: String,
    private val enumClass: Class<T>,
    override val canReplace: Boolean = false,
    override val default: T? = null
) : BundleWrapper.Property<T>(wrapper) {
    override fun getter(bundle: Bundle): T = java.lang.Enum.valueOf(enumClass, bundle.getString(key)!!)
    override fun setter(bundle: Bundle, newValue: T) = bundle.putString(key, newValue.name)
}