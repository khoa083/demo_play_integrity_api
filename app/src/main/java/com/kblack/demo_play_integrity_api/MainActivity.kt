package com.kblack.demo_play_integrity_api

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import androidx.activity.viewModels
import com.kblack.base.BaseActivity
import com.kblack.base.extensions.clickWithTrigger
import com.kblack.base.utils.DataResult
import com.kblack.demo_play_integrity_api.databinding.ActivityMainBinding
import com.kblack.demo_play_integrity_api.utils.Utils.Companion.observeNonNull
import com.kblack.base.extensions.toast
import java.util.regex.Pattern
import androidx.core.graphics.toColorInt
import com.google.gson.Gson

class MainActivity() : BaseActivity<ActivityMainBinding, MainActivityViewModel>() {

    override val viewModel: MainActivityViewModel by viewModels()
    override val layoutId: Int = R.layout.activity_main
    override val idContainerView: Int = R.id.main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.prepareIntegrityTokenProvider(applicationContext)
    }

    override fun setupView(activityBinding: ActivityMainBinding) {
        activityBinding.apply {
            btnVerify.clickWithTrigger {
                viewModel.playIntegrityRequest(applicationContext)
            }
            observeData(this@apply)
        }
    }

    private fun observeData(activityBinding: ActivityMainBinding) {
        viewModel.resultRAW.observeNonNull(this@MainActivity) { dataResult ->
            when (dataResult?.status) {
                DataResult.Status.LOADING -> {
                    activityBinding.txtResult.text = "Loading..."
                }

                DataResult.Status.SUCCESS -> {
//                    activityBinding.txtResult.text = dataResult.data?.toString() ?: "No data"
                    val jsonString = Gson().toJson(dataResult.data)
                    activityBinding.txtResult.text = formatJsonWithColors(jsonString)
                    // if use toString() method, it will not format the json string
                }

                DataResult.Status.ERROR -> {
                    activityBinding.txtResult.text = dataResult.message ?: "Unknown error"
                }

                null -> this@MainActivity.toast("DataResult is null")
            }
            viewModel.clearResult()
        }
    }

    override fun showView(isShow: Boolean) {
        TODO("Not yet implemented")
    }

    // One Dark Pro color scheme
    companion object {
        const val KEY_COLOR = "#e06c75"        // Red
        const val STRING_COLOR = "#98c379"     // Green
        const val NUMBER_COLOR = "#d19a66"     // Orange
        const val BOOLEAN_COLOR = "#56b6c2"    // Cyan
        const val NULL_COLOR = "#c678dd"       // Purple
        const val BRACKET_COLOR = "#61afef"    // Blue
    }

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
