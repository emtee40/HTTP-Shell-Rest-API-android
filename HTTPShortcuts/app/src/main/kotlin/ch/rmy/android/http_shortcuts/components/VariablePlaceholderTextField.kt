package ch.rmy.android.http_shortcuts.components

import android.app.Application
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DataObject
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.TransformedText
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import ch.rmy.android.http_shortcuts.R
import ch.rmy.android.http_shortcuts.dagger.getApplicationComponent
import ch.rmy.android.http_shortcuts.data.domains.variables.VariableRepository
import ch.rmy.android.http_shortcuts.data.dtos.VariablePlaceholder
import ch.rmy.android.http_shortcuts.extensions.insertAtCursor
import ch.rmy.android.http_shortcuts.variables.VariablePlaceholderProvider
import ch.rmy.android.http_shortcuts.variables.Variables.BROKEN_RAW_PLACEHOLDER_REGEX
import ch.rmy.android.http_shortcuts.variables.Variables.RAW_PLACEHOLDER_REGEX
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class VariablePlaceholderViewModel(application: Application) : AndroidViewModel(application) {
    @Inject
    lateinit var variableRepository: VariableRepository

    @Inject
    lateinit var variablePlaceholderProvider: VariablePlaceholderProvider

    private val _variablePlaceholders by lazy { MutableStateFlow(variablePlaceholderProvider.placeholders) }
    val variablePlaceholders by lazy { _variablePlaceholders.asStateFlow() }

    init {
        getApplicationComponent().inject(this)
        viewModelScope.launch {
            variableRepository.getObservableVariables().collect { variables ->
                variablePlaceholderProvider.applyVariables(variables)
                _variablePlaceholders.value = variablePlaceholderProvider.placeholders
            }
        }
    }
}

private val VARIABLE_PLACEHOLDER_REGEX = RAW_PLACEHOLDER_REGEX.toRegex()
private val BROKEN_VARIABLE_PLACEHOLDER_REGEX = BROKEN_RAW_PLACEHOLDER_REGEX.toRegex()

@Stable
private fun transformVariablePlaceholders(text: String, placeholders: List<VariablePlaceholder>, style: SpanStyle): TransformedText {
    val rangeMappings = mutableListOf<Pair<IntRange, IntRange>>()
    var offsetSum = 0
    val transformedText = VARIABLE_PLACEHOLDER_REGEX.replace(text) { result ->
        val (variableId) = result.destructured
        val placeholder = placeholders.find { it.variableId == variableId } ?: return@replace result.value
        val variableKey = placeholder.variableKey
        val replacement = "{$variableKey}"
        val lengthDiff = replacement.length - result.value.length
        rangeMappings.add(result.range to IntRange(result.range.first + offsetSum, result.range.last + lengthDiff + offsetSum))
        offsetSum += lengthDiff
        replacement
    }
    return TransformedText(
        buildAnnotatedString {
            append(transformedText)
            rangeMappings.forEach { (_, range) ->
                addStyle(style, range.first, range.last + 1)
            }
        },
        object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                var shift = 0
                for ((originalRange, transformedRange) in rangeMappings) {
                    if (offset < originalRange.first) {
                        break
                    }
                    if (offset == originalRange.first) {
                        return transformedRange.first
                    }
                    if (offset in originalRange) {
                        return transformedRange.last + 1
                    }
                    shift = transformedRange.last - originalRange.last
                }
                return offset + shift
            }

            override fun transformedToOriginal(offset: Int): Int {
                var shift = 0
                for ((originalRange, transformedRange) in rangeMappings) {
                    if (offset < transformedRange.first) {
                        break
                    }
                    if (offset == transformedRange.first) {
                        return originalRange.first
                    }
                    if (offset in transformedRange) {
                        return originalRange.last + 1
                    }
                    shift = originalRange.last - transformedRange.last
                }
                return offset + shift
            }
        }
    )
}

@Composable
fun VariablePlaceholderTextField(
    key: String,
    value: String,
    onValueChange: (String) -> Unit,
    label: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    maxLines: Int = Int.MAX_VALUE,
) {
    val viewModel = viewModel<VariablePlaceholderViewModel>()
    val placeholders by viewModel.variablePlaceholders.collectAsStateWithLifecycle()
    val focusRequester = remember {
        FocusRequester()
    }
    val placeholderColor = colorResource(R.color.variable)
    val placeholderStyle = remember {
        SpanStyle(
            color = placeholderColor,
            fontFamily = FontFamily.Monospace,
        )
    }

    var dialogVisible by rememberSaveable(key = key) {
        mutableStateOf(false)
    }

    var textFieldValue by remember {
        mutableStateOf(
            TextFieldValue(
                text = value,
            )
        )
    }
    LaunchedEffect(value) {
        textFieldValue = textFieldValue.copy(text = value)
    }

    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester)
            .then(modifier),
        label = label,
        value = textFieldValue,
        onValueChange = { newValue ->
            var selection = newValue.selection
            val newText = newValue.text
                .replace(BROKEN_VARIABLE_PLACEHOLDER_REGEX) {
                    if (selection.start == it.range.last + 1) {
                        selection = TextRange(it.range.first)
                    }
                    ""
                }
            textFieldValue = newValue.copy(text = newText, selection = selection)
            onValueChange(newText)
        },
        maxLines = maxLines,
        trailingIcon = {
            IconButton(
                onClick = {
                    dialogVisible = true
                },
            ) {
                Icon(Icons.Filled.DataObject, null)
            }
        },
        visualTransformation = {
            transformVariablePlaceholders(it.text, placeholders, placeholderStyle)
        },
    )

    if (dialogVisible) {
        VariablePickerDialog(
            title = stringResource(R.string.dialog_title_variable_selection),
            variables = placeholders,
            onVariableSelected = {
                textFieldValue = textFieldValue.insertAtCursor("{{$it}}", "")
                onValueChange(textFieldValue.text)
                dialogVisible = false
                focusRequester.requestFocus()
            },
            onDismissRequested = {
                dialogVisible = false
            },
        )
    }
}
