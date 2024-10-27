package com.eitanliu.binding.model

import android.graphics.Typeface
import androidx.annotation.AnyRes
import com.eitanliu.utils.hashCodeArray
import java.io.Serializable

data class FontFamilyTag(
    @AnyRes val id: Int?,
    val name: String?,
    val typeface: Typeface?,
) : Serializable {
    companion object {
        @JvmStatic
        val NORMAL = FontFamilyTag(null, null, null)
    }

    override fun equals(other: Any?): Boolean {
        if (other is FontFamilyTag) {
            return id == other.id && name == other.name
        }
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return hashCodeArray(id, name)
    }
}