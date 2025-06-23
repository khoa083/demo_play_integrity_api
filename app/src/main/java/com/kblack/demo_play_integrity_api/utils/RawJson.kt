package com.kblack.demo_play_integrity_api.utils

import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.core.graphics.toColorInt
import java.util.regex.Pattern

object RawJson {
    // One Dark Pro color scheme
    const val KEY_COLOR = "#e06c75"        // Red
    const val STRING_COLOR = "#98c379"     // Green
    const val NUMBER_COLOR = "#d19a66"     // Orange
    const val BOOLEAN_COLOR = "#56b6c2"    // Cyan
    const val NULL_COLOR = "#c678dd"       // Purple
    const val BRACKET_COLOR = "#61afef"    // Blue

    fun formatJsonWithColors(jsonString: String): SpannableString {
        val formattedJson = formatJsonString(jsonString)
        val spannable = SpannableString(formattedJson)

        // Apply colors
        applyKeyColors(spannable, formattedJson)
        applyStringColors(spannable, formattedJson)
        applyNumberColors(spannable, formattedJson)
        applyBooleanColors(spannable, formattedJson)
        applyNullColors(spannable, formattedJson)
        applyBracketColors(spannable, formattedJson)

        return spannable
    }

    private fun formatJsonString(jsonString: String): String {
        return try {
            val jsonObject = org.json.JSONObject(jsonString)
            jsonObject.toString(2)
        } catch (e: Exception) {
            jsonString
        }
    }

    private fun applyKeyColors(spannable: SpannableString, text: String) {
        val keyPattern = Pattern.compile("\"[^\"]*\"(?=\\s*:)")
        val matcher = keyPattern.matcher(text)
        while (matcher.find()) {
            spannable.setSpan(
                ForegroundColorSpan(KEY_COLOR.toColorInt()),
                matcher.start(),
                matcher.end(),
                0
            )
        }
    }

    private fun applyStringColors(spannable: SpannableString, text: String) {
        val stringPattern = Pattern.compile(":\\s*\"[^\"]*\"")
        val matcher = stringPattern.matcher(text)
        while (matcher.find()) {
            val start = matcher.start() + matcher.group().indexOf("\"")
            spannable.setSpan(
                ForegroundColorSpan(STRING_COLOR.toColorInt()),
                start,
                matcher.end(),
                0
            )
        }
    }

    private fun applyNumberColors(spannable: SpannableString, text: String) {
        val numberPattern = Pattern.compile(":\\s*(-?\\d+(?:\\.\\d+)?(?:[eE][+-]?\\d+)?)")
        val matcher = numberPattern.matcher(text)
        while (matcher.find()) {
            val start = matcher.start(1)
            val end = matcher.end(1)
            spannable.setSpan(
                ForegroundColorSpan(NUMBER_COLOR.toColorInt()),
                start,
                end,
                0
            )
        }
    }

    private fun applyBooleanColors(spannable: SpannableString, text: String) {
        val booleanPattern = Pattern.compile("\\b(true|false)\\b")
        val matcher = booleanPattern.matcher(text)
        while (matcher.find()) {
            spannable.setSpan(
                ForegroundColorSpan(BOOLEAN_COLOR.toColorInt()),
                matcher.start(),
                matcher.end(),
                0
            )
        }
    }

    private fun applyNullColors(spannable: SpannableString, text: String) {
        val nullPattern = Pattern.compile("\\bnull\\b")
        val matcher = nullPattern.matcher(text)
        while (matcher.find()) {
            spannable.setSpan(
                ForegroundColorSpan(NULL_COLOR.toColorInt()),
                matcher.start(),
                matcher.end(),
                0
            )
        }
    }

    private fun applyBracketColors(spannable: SpannableString, text: String) {
        val bracketPattern = Pattern.compile("[{}\\[\\]]")
        val matcher = bracketPattern.matcher(text)
        while (matcher.find()) {
            spannable.setSpan(
                ForegroundColorSpan(BRACKET_COLOR.toColorInt()),
                matcher.start(),
                matcher.end(),
                0
            )
        }
    }
}