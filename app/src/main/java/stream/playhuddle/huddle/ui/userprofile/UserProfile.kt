package stream.playhuddle.huddle.ui.userprofile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import stream.playhuddle.huddle.R
import stream.playhuddle.huddle.ui.theme.Glacial
import stream.playhuddle.huddle.ui.theme.HuddleTheme
import stream.playhuddle.huddle.utils.HomeNavGraph

@HomeNavGraph
@Destination
@Composable
fun UserProfileScreen(navigator: DestinationsNavigator) {
    UserProfileScreen(Modifier, onNavigateBack = navigator::navigateUp)
}

val list = listOf(
    R.drawable.gal_1,
    R.drawable.gal_2,
    R.drawable.gal_3,
    R.drawable.gal_4,
    R.drawable.gal_5,
    R.drawable.gal_6,
    R.drawable.gal_7,
    R.drawable.gal_8,
    R.drawable.gal_9,
    R.drawable.gal_10,
    R.drawable.gal_11,
    R.drawable.gal_12,
    R.drawable.gal_13
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UserProfileScreen(modifier: Modifier = Modifier, onNavigateBack: () -> Unit = {}) {
    Column(modifier = modifier) {
        TopAppBar(
            title = { Text(text = "Paulo") },
            navigationIcon = {
                IconButton(onClick = onNavigateBack) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                }
            }
        )

        Row(modifier = Modifier.padding(16.dp)) {
            Image(
                painter = painterResource(id = R.drawable.paulo_pfp),
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = "Paulo", fontWeight = FontWeight.Bold, fontFamily = Glacial)
        }

        var openGallery by remember { mutableStateOf(false) }
        var page by remember { mutableStateOf(0)}

        LazyVerticalGrid(
            modifier = Modifier.fillMaxSize(),
            columns = GridCells.Fixed(3)
        ) {
            itemsIndexed(list) { index, drawableId ->
                Image(
                    painter = painterResource(id = drawableId),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(height = 150.dp, width = 100.dp)
                        .clickable {
                            openGallery = true
                            page = index
                        }
                )
            }
        }
        if (openGallery) {
            Dialog(
                onDismissRequest = { openGallery = false },
                properties = DialogProperties(usePlatformDefaultWidth = false)
            ) {
                GalleryPreview(initialPage = page, onDismiss = { openGallery = false })

            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPagerApi::class)
@Composable
fun GalleryPreview(initialPage: Int, onDismiss: () -> Unit = {}) {
    Box(modifier = Modifier.background(color = Color.Black)) {
        TopAppBar(
            title = { Text(text = "Paulo") },
            navigationIcon = {
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                }
            },
            modifier = Modifier
                .align(Alignment.TopCenter)
                .zIndex(2f),
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent,
                navigationIconContentColor = Color.White,
                titleContentColor = Color.White
            )
        )


        val pagerState = rememberPagerState(initialPage=initialPage)
        HorizontalPager(
            count = list.size,
            state = pagerState,
            modifier = Modifier.zIndex(1f)
        ) { page ->
            val drawableId = list[page]
            val painter = painterResource(id = drawableId)

            Box {
                Image(
                    painter = painter,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
            }

        }
    }

}

@Preview
@Composable
fun UserProfileScreenPreview() {
    HuddleTheme {
        UserProfileScreen(Modifier)
    }
}