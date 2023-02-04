package stream.playhuddle.huddle.ui.home

import android.app.NotificationManager
import android.os.Build
import android.os.CountDownTimer
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.core.content.ContextCompat
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.popUpTo
import kotlinx.coroutines.launch
import stream.playhuddle.huddle.R
import stream.playhuddle.huddle.data.User
import stream.playhuddle.huddle.ui.destinations.HomeRouteDestination
import stream.playhuddle.huddle.ui.destinations.InboxRouteDestination
import stream.playhuddle.huddle.ui.theme.Bebas
import stream.playhuddle.huddle.ui.theme.Glacial
import stream.playhuddle.huddle.ui.theme.HuddleTheme
import stream.playhuddle.huddle.ui.theme.md_theme_dark_secondary
import stream.playhuddle.huddle.utils.HomeNavGraph
import stream.playhuddle.huddle.utils.cancelNotifications
import stream.playhuddle.huddle.utils.sendNotification

@HomeNavGraph(start = true)
@Destination
@Composable
fun HomeRoute(navigator: DestinationsNavigator) {
    HomeScreen(navigateToInbox = {
        navigator.navigate(InboxRouteDestination) {
            popUpTo(HomeRouteDestination) {
                inclusive = true
            }
        }
    })
}

const val MAX_PAGE = 7

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun HomeScreen(navigateToInbox: () -> Unit = {}) {

    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(initialPage = 0)
    HorizontalPager(
        count = MAX_PAGE,
        modifier = Modifier.fillMaxSize(),
        state = pagerState
    ) { page ->
        Box(modifier = Modifier) {
            IconButton(
                onClick = {
                    scope.launch { pagerState.scrollToPage(currentPage - 1) }
                },
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .zIndex(99f),
                enabled = currentPage > 0
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.black_swipe),
                    contentDescription = "Left swipe",
                    tint = if (currentPage > 0) Color(0xffd93542) else Color.Black,
                    modifier = Modifier.size(100.dp)
                )
            }
            IconButton(
                onClick = {
                    scope.launch { pagerState.scrollToPage(currentPage + 1) }
                },
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .zIndex(99f),
                enabled = page + 1 < MAX_PAGE
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.right_swipe),
                    contentDescription = "Right swipe",
                    tint = if (currentPage + 1 == MAX_PAGE) Color.Black else Color(0xffd93542),
                    modifier = Modifier.size(100.dp)
                )
            }
            when (page) {
                0 -> PageOne()
                1 -> PageTwo()
                2 -> PageThree()
                3 -> PageFour()
                4 -> PageFive()
                5 -> PageSix()
                6 -> PageSeven(username = "MusicLover555")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PageSeven(username: String) {
    var open by remember { mutableStateOf(false) }
    Page(
        user = User(
            username = "Its_me_Paulo_97",
            interests = "Arts, Bands, Entrepreneur, Swimming",
            bio = "Trader \n  \n \"I work for success.\"",
            onboarded = false,
            age = 25,
            location = "Manila"
        ), imageRes = R.drawable.paulo,
        modifier = Modifier.clickable { open = true }
    )
    if (open) {
        val context = LocalContext.current
        val notificationManager = ContextCompat.getSystemService(
            context,
            NotificationManager::class.java
        ) as NotificationManager
        LaunchedEffect(key1 = Unit, block = {
            object : CountDownTimer(3000, 1000) {
                override fun onTick(millisUntilFinished: Long) = Unit

                @RequiresApi(Build.VERSION_CODES.M)
                override fun onFinish() {
                    notificationManager.cancelNotifications()
                    open = false
                    notificationManager.sendNotification(
                        "Paulo, 22\n2.2 kilometers away",
                        "Hey. I'm Paulo, You can call me Pau :)",
                        context
                    )
                }

            }.start()
        })
        AlertDialog(onDismissRequest = { open = false }) {
            MatchBanner(username = username)
        }
    }
}

@Preview
@Composable
fun MatchBannerPreview() {
    HuddleTheme {
        MatchBanner(username = "MusicLover555")
    }
}

@Composable
fun MatchBanner(username: String) {
    Box(
        modifier = Modifier
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        md_theme_dark_secondary,
                        Color(0xffF07BA0),
                    )
                ),
                shape = MaterialTheme.shapes.large
            )
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(R.string.its_a_match).uppercase(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontFamily = Bebas,
                    fontWeight = FontWeight.W600,
                    color = Color(0xff3D3A3A),
                    fontSize = 48.sp
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.paulo_chat_pfp),
                    contentDescription = null,
                    modifier = Modifier.size(100.dp),
                )
                Text(
                    text = "Its_me_Paulo_97",
                    fontFamily = Glacial,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Image(
                painter = painterResource(id = R.drawable.user_icon),
                contentDescription = null,
                modifier = Modifier.size(100.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.user_icon),
                    contentDescription = null,
                    modifier = Modifier.size(100.dp),
                )
                Text(
                    text = username,
                    fontFamily = Glacial,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )
            }
        }

    }
}

@Composable
private fun PageSix() {
    Page(
        user = User(
            username = "ArtsanddesignAfficionado",
            interests = "Arts, Designs, Creativity",
            bio = "Prod. design/Make-up artist/costume - Aira Annegeline Yanes & Mary Rose Malabayabas" +
                    "\nCreatives/Design - Dylan James Paulino" +
                    "\nMarketing and Promotions - Thea Marie Templanza & Rosebell Canlas",
            onboarded = false,
            age = -1,
            location = ""
        ), imageRes = R.drawable.art
    )
}

@Composable
private fun PageFive() {
    Page(
        user = User(
            username = "PicturesqueCutie999",
            interests = "Photography, Graphic Design",
            bio = "Direction of Photography - Danica Montralbo" +
                    "\nCamera Operator - Lance Latoja" +
                    "\nEditor - Jony Pagkaliwangan" +
                    "\nEditor/Creatives - Shaina Rogel" +
                    "\nWeb & App Developer - Warren Layson",
            onboarded = false,
            age = -1,
            location = ""
        ), imageRes = R.drawable.prod
    )
}

@Composable
private fun PageFour() {
    Page(
        user = User(
            username = "SerialWriter",
            interests = "Scriptwriting, Watching Movies",
            bio = "Asst. Producer/Scriptwriter - Monica Antonio" +
                    "\nHead Scriptwriter - Francheska Sastre" +
                    "\nWriter - Lenny Marie Garcia",
            age = -1,
            onboarded = true,
            location = ""
        ),
        imageRes = R.drawable.serial_writer
    )
}

@Composable
private fun PageThree() {
    Page(
        user = User(
            username = "NextGen_Leaders",
            interests = "Film, Arts, Movies and Chill",
            bio = "Director - Rayven Daligcon" +
                    "\nAssistant Director - Harold Lemon Tubiano" +
                    "\nExecutive Producer - Nikka Avegail Sabio",
            location = "",
            onboarded = true,
            age = -1
        ),
        imageRes = R.drawable.camera,
    )
}

@Composable
private fun PageTwo() {
    Page(
        user = User(
            "Sinulid Productions",
            age = -1,
            location = "",
            interests = "Short Film Production" +
                    "\n\nStudent-led Production House" +
                    "\nSocial Media Awareness",
            bio = "",
            onboarded = true
        ),
        imageRes = R.drawable.sinulid,
    )
}

@Composable
private fun PageOne() {
    Page(
        user = User(
            "MusicLover555",
            age = 21,
            location = "Metro Manila",
            interests = "Watching TV & Films, Foodie, Bands",
            bio = "Just someone to talk with :)",
            onboarded = true
        ),
        imageRes = R.drawable.user,
    )
}

@Composable
private fun Page(user: User, @DrawableRes imageRes: Int, modifier: Modifier = Modifier) {
    CompositionLocalProvider(
        LocalTextStyle provides MaterialTheme.typography.bodyLarge.copy(
            fontFamily = Glacial
        )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = modifier
        ) {
            Image(
                painter = painterResource(imageRes),
                contentDescription = null,
                modifier = Modifier
                    .size(500.dp)
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append(user.username)
                                if (user.age != -1) append(", ${user.age}")
                            }
                        },
                        fontSize = 28.sp
                    )
                    if (user.location.isNotEmpty()) {
                        Text(
                            text = user.location,
                            fontSize = 24.sp,
                            fontStyle = FontStyle.Italic
                        )
                    }
                }
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("Interests: ")
                        }
                        withStyle(
                            style = SpanStyle(fontStyle = FontStyle.Italic)
                        ) {
                            append(user.interests)
                        }
                    },
                    fontSize = 24.sp, textAlign = TextAlign.Center,
                )
                if (user.bio.isNotEmpty()) {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append("Bio:\n")
                            }
                            append(user.bio)
                        },
                        textAlign = TextAlign.Center,
                        fontSize = 24.sp,
                    )
                }
            }

        }
    }
}


@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HuddleTheme {
        HomeScreen()
    }
}