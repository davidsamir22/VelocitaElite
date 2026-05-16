package com.velocita.elite.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.velocita.elite.ui.theme.VelocitaColors

@Composable
fun GoldDivider(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth(0.5f)
            .height(1.dp)
            .background(
                Brush.horizontalGradient(
                    listOf(
                        Color.Transparent,
                        VelocitaColors.GoldPrimary,
                        Color.Transparent
                    )
                )
            )
    )
}

@Composable
fun VelocitaTextField(
    value                : String,
    onValueChange        : (String) -> Unit,
    label                : String,
    placeholder          : String,
    errorText            : String?            = null,
    visualTransformation : VisualTransformation = VisualTransformation.None,
    leadingIcon          : @Composable (() -> Unit)? = null,
    trailingIcon         : @Composable (() -> Unit)? = null,
    keyboardOptions      : KeyboardOptions    = KeyboardOptions.Default,
    keyboardActions      : KeyboardActions    = KeyboardActions.Default
) {
    val isError = !errorText.isNullOrBlank()

    Column(modifier = Modifier.fillMaxWidth()) {

        OutlinedTextField(
            value                = value,
            onValueChange        = onValueChange,
            label                = {
                Text(
                    text  = label,
                    style = MaterialTheme.typography.labelMedium.copy(
                        letterSpacing = 1.2.sp
                    )
                )
            },
            placeholder          = {
                Text(
                    text  = placeholder,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = VelocitaColors.SilverDim
                    )
                )
            },
            isError              = isError,
            visualTransformation = visualTransformation,
            leadingIcon          = leadingIcon,
            trailingIcon         = trailingIcon,
            keyboardOptions      = keyboardOptions,
            keyboardActions      = keyboardActions,
            singleLine           = true,
            shape                = RoundedCornerShape(4.dp),
            colors               = OutlinedTextFieldDefaults.colors(
                focusedTextColor    = VelocitaColors.SilverLight,
                unfocusedTextColor  = VelocitaColors.SilverLight,
                errorTextColor      = VelocitaColors.SilverLight,
                focusedLabelColor   = VelocitaColors.GoldPrimary,
                unfocusedLabelColor = VelocitaColors.SilverMid,
                errorLabelColor     = VelocitaColors.ErrorRed,
                focusedBorderColor  = VelocitaColors.GoldPrimary,
                unfocusedBorderColor = VelocitaColors.CharcoalLight,
                errorBorderColor    = VelocitaColors.ErrorRed,
                focusedContainerColor   = VelocitaColors.CharcoalMid.copy(alpha = 0.5f),
                unfocusedContainerColor = VelocitaColors.CharcoalMid.copy(alpha = 0.3f),
                errorContainerColor     = VelocitaColors.ErrorRed.copy(alpha = 0.05f),
                cursorColor = VelocitaColors.GoldPrimary,
                errorCursorColor = VelocitaColors.ErrorRed
            ),
            modifier = Modifier.fillMaxWidth()
        )

        if (isError) {
            Row(
                modifier           = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp, top = 4.dp),
                verticalAlignment  = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(
                    imageVector        = Icons.Default.ErrorOutline,
                    contentDescription = null,
                    tint               = VelocitaColors.ErrorRed,
                    modifier           = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text  = errorText!!,
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = VelocitaColors.ErrorRed
                    )
                )
            }
        }
    }
}
