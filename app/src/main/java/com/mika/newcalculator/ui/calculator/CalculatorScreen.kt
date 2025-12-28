package com.mika.newcalculator.ui.calculator

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mika.newcalculator.ui.theme.NewCalculatorTheme

@Composable
fun CalculatorScreen() {
    val context = LocalContext.current
    // Create ViewModel using the Factory pattern to inject dependencies
    val viewModel: CalculatorViewModel = viewModel(
        factory = CalculatorViewModel.provideFactory(context)
    )

    val displayValue by viewModel.display.collectAsState()
    // 0: OFF, 1: EUR to JPY, 2: JPY to EUR
    var currencyConversionMode by remember { mutableIntStateOf(0) }
    val conversionRate by viewModel.conversionRate.collectAsState()
    val conversionDate by viewModel.conversionDate.collectAsState()

    // Calculate day of the week (e.g., "2023-10-01" -> "Sun")
    val dayOfWeek = remember(conversionDate) {
        if (conversionDate.isNotEmpty()) {
            try {
                val dateFormat = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US)
                val date = dateFormat.parse(conversionDate)
                val dayFormat = java.text.SimpleDateFormat("EEE", java.util.Locale.US)
                dayFormat.format(date!!)
            } catch (e: Exception) {
                ""
            }
        } else {
            ""
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Display Exchange Rate and Date
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Date (Line 1)
            Text(
                text = if (conversionDate.isNotEmpty()) "$conversionDate ($dayOfWeek)" else "Loading...",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            // Exchange Rate (Line 2)
            Text(
                text = "1 EUR = ${conversionRate.toInt()} JPY",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }

        // Currency conversion options (horizontal row of RadioButtons)
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier.clickable { currencyConversionMode = 0 },
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = currencyConversionMode == 0,
                    onClick = { currencyConversionMode = 0 }
                )
                Text(
                    "OFF",
                    modifier = Modifier.padding(start = 4.dp),
                    fontSize = 14.sp
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Row(
                modifier = Modifier.clickable { currencyConversionMode = 1 },
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = currencyConversionMode == 1,
                    onClick = { currencyConversionMode = 1 }
                )
                Text(
                    "EUR/JPY",
                    modifier = Modifier.padding(start = 4.dp),
                    fontSize = 14.sp
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Row(
                modifier = Modifier.clickable { currencyConversionMode = 2 },
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = currencyConversionMode == 2,
                    onClick = { currencyConversionMode = 2 }
                )
                Text(
                    "JPY/EUR",
                    modifier = Modifier.padding(start = 4.dp),
                    fontSize = 14.sp
                )
            }
        }

        // Converted value display (only shown when conversion is enabled)
        if (currencyConversionMode != 0) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f),
                        shape = MaterialTheme.shapes.medium
                    )
                    .padding(16.dp)
            ) {
                val inputValue = displayValue.toDoubleOrNull() ?: 0.0
                val convertedValue = when (currencyConversionMode) {
                    1 -> (inputValue * conversionRate).toInt() // EUR to JPY
                    2 -> inputValue / conversionRate // JPY to EUR
                    else -> 0.0
                }
                val symbol = when (currencyConversionMode) {
                    1 -> "¥ "
                    2 -> "€ "
                    else -> ""
                }
                val formattedValue = when (currencyConversionMode) {
                    1 -> "$symbol$convertedValue" // JPY is integer
                    2 -> "$symbol${"%.2f".format(convertedValue)}" // EUR has 2 decimal places
                    else -> ""
                }
                Text(
                    text = formattedValue,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Normal
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.End
                )
            }
        }

        // Original value display (Input area)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = MaterialTheme.shapes.medium
                )
                .padding(16.dp)
        ) {
            // Determine the currency symbol for the input value based on the current mode
            val inputSymbol = when (currencyConversionMode) {
                1 -> "€ " // If EUR -> JPY mode, input is Euro
                2 -> "¥ " // If JPY -> EUR mode, input is Yen
                else -> ""
            }
            Text(
                text = "$inputSymbol$displayValue",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.End
            )
        }

        val buttons = listOf(
            listOf("C", "⌫", "%", "÷"),
            listOf("7", "8", "9", "×"),
            listOf("4", "5", "6", "-"),
            listOf("1", "2", "3", "+"),
            listOf("00", "0", ".", "=")
        )

        buttons.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                row.forEach { label ->
                    // Logic to determine button colors
                    val buttonColor = when (label) {
                        "C", "⌫", "%" -> MaterialTheme.colorScheme.secondary // Function keys
                        "÷", "×", "-", "+", "=" -> MaterialTheme.colorScheme.tertiary // Operation keys
                        else -> MaterialTheme.colorScheme.primary // Number keys
                    }

                    CalculatorButton(label, containerColor = buttonColor) {
                        viewModel.onButtonClick(label)
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun CalculatorButton(label: String, containerColor: Color, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .size(72.dp)
            .background(MaterialTheme.colorScheme.secondaryContainer, CircleShape),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = Color.White
        )
    ) {
        Text(
            text = label,
            color = MaterialTheme.colorScheme.onPrimary,
            fontSize = when (label) {
                "⌫" -> 28.sp  // Adjust size for the backspace symbol
                else -> when (label.length) {
                    1 -> 26.sp
                    2 -> 20.sp
                    else -> 18.sp
                }
            },
            maxLines = 1,
            overflow = TextOverflow.Clip,
            modifier = when (label) {
                "⌫" -> Modifier.offset(x = (-3).dp, y = (-2).dp)
                else -> Modifier
            }
        )
    }
}