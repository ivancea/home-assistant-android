package io.homeassistant.companion.android.home.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Picker
import androidx.wear.compose.material.rememberPickerState
import androidx.wear.compose.material3.FilledIconButton
import androidx.wear.compose.material3.Icon
import androidx.wear.compose.material3.IconButtonDefaults
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.Text
import androidx.wear.compose.material3.touchTargetAwareSize
import androidx.wear.tooling.preview.devices.WearDevices
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import com.google.android.horologist.composables.picker.toRotaryScrollAdapter
import com.google.android.horologist.compose.rotaryinput.rotaryWithSnap
import io.homeassistant.companion.android.theme.wearColorScheme
import io.homeassistant.companion.android.util.intervalToString
import io.homeassistant.companion.android.views.ListHeader
import io.homeassistant.companion.android.common.R as R

@OptIn(ExperimentalHorologistApi::class)
@Composable
fun RefreshIntervalPickerView(
    currentInterval: Int,
    onSelectInterval: (Int) -> Unit
) {
    val options = listOf(0, 60, 2 * 60, 5 * 60, 10 * 60, 15 * 60, 30 * 60, 60 * 60, 2 * 60 * 60, 5 * 60 * 60, 10 * 60 * 60, 24 * 60 * 60)
    val initialIndex = options.indexOf(currentInterval)
    val state = rememberPickerState(
        initialNumberOfOptions = options.size,
        initiallySelectedOption = if (initialIndex != -1) initialIndex else 0
    )

    Column(
        modifier = Modifier.padding(12.dp).fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ListHeader(R.string.refresh_interval)
        Picker(
            state = state,
            contentDescription = stringResource(R.string.refresh_interval),
            modifier = Modifier
                .weight(1f)
                .rotaryWithSnap(state.toRotaryScrollAdapter())
        ) {
            Text(
                text = intervalToString(LocalContext.current, options[it]),
                style = with(LocalDensity.current) {
                    MaterialTheme.typography.displayMedium.copy(
                        fontWeight = FontWeight.Medium,
                        fontSize = MaterialTheme.typography.displayMedium.fontSize.value.dp.toSp() // Ignore text scaling
                    )
                },
                color = wearColorScheme.primary
            )
        }
        FilledIconButton(
            onClick = { onSelectInterval(options[state.selectedOption]) },
            modifier = Modifier.touchTargetAwareSize(IconButtonDefaults.SmallButtonSize)
        ) {
            Icon(
                Icons.Filled.Check,
                contentDescription = stringResource(id = R.string.save),
                modifier = Modifier.size(IconButtonDefaults.iconSizeFor(IconButtonDefaults.SmallButtonSize))
            )
        }
    }
}

@Preview(device = WearDevices.LARGE_ROUND)
@Composable
private fun PreviewRefreshIntervalPickerView() {
    CompositionLocalProvider {
        RefreshIntervalPickerView(currentInterval = 10) {}
    }
}
