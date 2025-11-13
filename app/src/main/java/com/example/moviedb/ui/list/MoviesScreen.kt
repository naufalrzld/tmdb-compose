package com.example.moviedb.ui.list

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil3.compose.AsyncImage
import com.example.moviedb.MovieDetailScreen
import com.example.moviedb.core.BuildConfig
import com.example.moviedb.core.domain.model.MovieData
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun MoviesScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    viewModel: MoviesViewModel = koinViewModel()
) {

    var showGenresChooser by remember { mutableStateOf(false) }
    val selectedGenres by viewModel.selectedGenresState

    if (showGenresChooser) {
        GenresChooserBottomSheet(
            viewModel = viewModel,
            onApply = {
                val genres = if (selectedGenres.isEmpty()) null else selectedGenres.joinToString { it.id.toString() }
                viewModel.getMovies(genres)
                showGenresChooser = false
            },
            onDismiss = {
                showGenresChooser = false
            }
        )
    }

    LaunchedEffect(Unit) {
        val genres = if (selectedGenres.isEmpty()) null else selectedGenres.joinToString { it.id.toString() }
        viewModel.getMovies(genres)
    }

    LaunchedEffect(Unit) {
        viewModel.onError.collect {
            snackbarHostState.showSnackbar(it)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        GenreSelector(
            text = if (selectedGenres.isEmpty()) "Genres" else selectedGenres.joinToString { it.name },
            onClick = {
                showGenresChooser = true
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
        MovieList(
            viewModel = viewModel,
            onMovieClick = { movieId ->
                navController.navigate(MovieDetailScreen(movieId))
            }
        )
    }
}

@Composable
fun GenreSelector(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .border(
                border = BorderStroke(width = 1.dp, color = Color(0xFF757575)),
                shape = CircleShape
            )
            .clickable {
                onClick()
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyMedium
            )
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "Arrow Drop Down",
                tint = Color(0xFF757575)
            )
        }
    }
}

@Composable
fun MovieList(
    viewModel: MoviesViewModel,
    onMovieClick: (Int) -> Unit
) {
    val movies = viewModel.movies.collectAsLazyPagingItems()

    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (movies.loadState.refresh is LoadState.Loading) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        } else {
            if (movies.itemCount != 0) {
                items(movies.itemCount) { index ->
                    movies[index]?.let { movie ->
                        MovieItem(
                            movie = movie,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onMovieClick(movie.id)
                                }
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
                            text = "No movies available",
                            style = MaterialTheme.typography.bodyMedium.copy(fontStyle = FontStyle.Italic)
                        )
                    }
                }
            }

            if (movies.loadState.append is LoadState.Loading) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}

@Composable
fun MovieItem(
    movie: MovieData,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(intrinsicSize = IntrinsicSize.Min),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            movie.posterPath?.let { posterPath ->
                val posterUrl = BuildConfig.TMDB_BASE_URL_IMAGE + posterPath
                AsyncImage(
                    model = posterUrl,
                    contentDescription = "Poster",
                    modifier = Modifier
                        .width(120.dp)
                        .height(180.dp)
                        .clip(RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp))
                )
            } ?: Box(
                modifier = Modifier
                    .width(120.dp)
                    .height(180.dp)
                    .background(
                        color = Color(0x33000000),
                        shape = RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp)
                    )
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(8.dp)
            ) {
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = movie.releaseDate,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color(0xFF757575)
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = movie.overview,
                    maxLines = 6,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyMedium.copy()
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenresChooserBottomSheet(
    viewModel: MoviesViewModel,
    onApply: () -> Unit,
    onDismiss: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    val isGenreLoading by viewModel.isGenreLoading
    val genres by viewModel.genresState

    LaunchedEffect(Unit) {
        viewModel.getGenres()
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "Select Genre",
                style = MaterialTheme.typography.titleMedium
            )
            if (isGenreLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                if (genres.isNotEmpty()) {
                    FlowRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        genres.forEach {
                            GenreItem(
                                name = it.name,
                                onClick = {
                                    viewModel.setSelectedGenres(it)
                                },
                                isSelected = it.isSelected,
                            )
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No genres available",
                            style = MaterialTheme.typography.bodyMedium.copy(fontStyle = FontStyle.Italic)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    scope.launch {
                        sheetState.hide()
                        onApply()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Apply",
                    style = MaterialTheme.typography.titleMedium
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun GenreItem(
    name: String,
    onClick: () -> Unit,
    isSelected: Boolean = false
) {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .border(
                border = BorderStroke(
                    width = 1.dp,
                    color = if (isSelected) MaterialTheme.colorScheme.primary else Color(0xFF757575)
                ),
                shape = CircleShape
            )
            .clickable {
                onClick()
            }
            .padding(horizontal = 8.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = if (isSelected) MaterialTheme.colorScheme.primary else Color(0xFF757575),
                fontWeight = if (isSelected) FontWeight.Medium else MaterialTheme.typography.bodyMedium.fontWeight
            )
        )
    }
}
