package com.smallworldfs.moneytransferapp.compose.widgets

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY

@Composable
fun SWImageFlag(
    modifier: Modifier = Modifier,
    imageUrl: String = STRING_EMPTY,
    onSuccess: (AsyncImagePainter.State.Success) -> Unit = {},
    onLoading: (AsyncImagePainter.State.Loading) -> Unit = {},
    onError: (AsyncImagePainter.State.Error) -> Unit = {},
    errorImage: Painter = painterResource(id = R.drawable.account_icn_selectcountry),
    fallbackImage: Painter = painterResource(id = R.drawable.account_icn_selectcountry),
    size: Dp = 56.dp
) {

    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .diskCachePolicy(CachePolicy.DISABLED)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .error(R.drawable.placeholder_country_adapter)
            .fallback(R.drawable.placeholder_country_adapter)
            .build(),
        contentDescription = null,
        onSuccess = onSuccess,
        onLoading = onLoading,
        onError = onError,
        error = errorImage,
        modifier = modifier
            .size(size),
        fallback = fallbackImage,
    )
}

@Preview(showBackground = true, widthDp = 420)
@Composable
fun SWImageFlagPreview() {
    SWImageFlag(
        errorImage = painterResource(id = R.drawable.linear_global_search),
        fallbackImage = painterResource(id = R.drawable.linear_global_search),
    )
}
