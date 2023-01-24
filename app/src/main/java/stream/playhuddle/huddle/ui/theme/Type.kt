package stream.playhuddle.huddle.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import stream.playhuddle.huddle.R

val Glacial = FontFamily(
    Font(R.font.glacial_bold, FontWeight.Bold),
    Font(R.font.glacial_italic, style = FontStyle.Italic),
    Font(R.font.glacial_regular)
)

val Bebas = FontFamily(
    Font(R.font.bebas_regular)
)

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = Glacial,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    labelLarge = TextStyle(
        fontFamily = Glacial,
        lineHeight = 20.0.sp,
        fontSize = 14.sp,
        letterSpacing = 0.1.sp,
        fontWeight = FontWeight.Medium
//        val LabelLargeFont = TypefaceTokens.Plain
//            val LabelLargeLineHeight = 20.0.sp
//    val LabelLargeSize = 14.sp
//val LabelLargeTracking = 0.1.sp
//val LabelLargeWeight = TypefaceTokens.WeightMedium
    )

    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)