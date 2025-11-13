package com.example.moviedb.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.moviedb.core.domain.model.MovieReviewData
import com.example.moviedb.core.utils.formatToMovieDuration
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import org.koin.androidx.compose.koinViewModel

@Composable
fun MovieDetailScreen(
    movieId: Int,
    viewModel: MovieDetailViewModel = koinViewModel(),
    snackbarHostState: SnackbarHostState
) {

    val isVideoLoading by viewModel.isVideoLoading
    val videoId by viewModel.videoId
    val movie by viewModel.movieState
    val reviews = viewModel.reviews.collectAsLazyPagingItems()

    LaunchedEffect(Unit) {
        viewModel.getMovieDetail(movieId)
        viewModel.getMovieVideos(movieId)
        viewModel.getMovieReviews(movieId)
    }

    LaunchedEffect(Unit) {
        viewModel.onError.collect {
            snackbarHostState.showSnackbar(it)
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        if (isVideoLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
                    .background(color = Color.Black),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            videoId?.let {
                VideoPlayer(
                    videoId = it,
                    lifecycleOwner = LocalLifecycleOwner.current,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 9f)
                )
            } ?: Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
                    .background(color = Color.Black),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No video available",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color.White,
                        fontStyle = FontStyle.Italic
                    )
                )
            }
        }
        LazyColumn(modifier = Modifier.weight(1f)) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = movie?.title ?: "",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = movie?.releaseDate ?: "",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = Color(0xFF757575)
                                    )
                                )
                                Text(
                                    text = movie?.runtime?.formatToMovieDuration() ?: "",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = Color(0xFF757575)
                                    )
                                )
                            }
                        }
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp),
                                    tint = Color(0xFFFDCC0D)
                                )
                                Text(
                                    text = movie?.voteAverage?.toString() ?: "0.0",
                                    style = MaterialTheme.typography.labelLarge.copy()
                                )
                            }
                            Text(
                                text = "${movie?.voteCount ?: "-"} Votes",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color(0xFF757575)
                                )
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(color = Color(0xFFDADADA))
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "Genre:",
                            style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF757575))
                        )
                        Text(
                            text = movie?.genres?.joinToString { it.name } ?: "-",
                            style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF757575))
                        )
                    }
                    Text(
                        text = movie?.overview ?: "",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Reviews",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }

            if (reviews.itemCount != 0) {
                items(reviews.itemCount) { index ->
                    reviews[index]?.let { reviewData ->
                        ReviewItem(
                            review = reviewData,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            } else {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No reviews available",
                            style = MaterialTheme.typography.bodyMedium.copy(fontStyle = FontStyle.Italic)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun VideoPlayer(
    videoId: String,
    lifecycleOwner: LifecycleOwner,
    modifier: Modifier = Modifier
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            YouTubePlayerView(context).apply {
                lifecycleOwner.lifecycle.addObserver(this)

                addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                    override fun onReady(youTubePlayer: YouTubePlayer) {
                        super.onReady(youTubePlayer)
                        youTubePlayer.loadVideo(videoId, 0f)
                    }
                })
            }
        }
    )
}

@Composable
fun ReviewItem(
    review: MovieReviewData,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .size(40.dp)
                    .background(color = Color(0xFF757575))
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = review.author,
                    style = MaterialTheme.typography.titleSmall
                )
                Text(
                    text = review.authorDetails.username,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color(0xFF757575)
                    )
                )
            }
            Text(
                text = review.createdAt,
                modifier = Modifier.align(Alignment.Top),
                style = MaterialTheme.typography.bodySmall.copy(
                    color = Color(0xFF757575)
                )
            )
        }
        Text(
            text = review.content,
            style = MaterialTheme.typography.bodyMedium
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(color = Color(0xFFDADADA))
        )
    }
}
