@file:OptIn(
    ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class,
    ExperimentalFoundationApi::class
)

package za.co.retrorabbit.gameofthrones.composables

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedSuggestionChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun IconTile(
    title: String,
    icon: ImageVector,
    click: (() -> Unit)? = null
) {
    ElevatedButton(
        modifier = Modifier
            .height(120.dp)
            .width(200.dp),
        shape = RoundedCornerShape(10.dp),
        onClick = {
            if (click != null) {
                click()
            }
        }) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                icon,
                contentDescription = "Localized description"
            )
            Text(
                text = title,
                textAlign = TextAlign.Center,
                maxLines = 3
            )
        }
    }
}

@Preview()
@Composable
fun IconTitlePreview() {
    IconTile("Title", Icons.Filled.Face)
    IconTile("Title", Icons.Filled.Face)
}

@Composable
fun MultilineLabel(title: String, body: String?) {
    val clipboardManager: ClipboardManager = LocalClipboardManager.current
    Column {
        Text(
            text = title,
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp
        )
        Text(
            modifier = Modifier
                .combinedClickable(
                    onClick = { },
                    onLongClick = {
                        body?.let {
                            clipboardManager.setText(AnnotatedString(body))
                        }
                    },
                    onLongClickLabel = "Copied value"
                ),
            text = body?.ifBlank { "⚔" } ?: "⚔",
            fontSize = 14.sp,
            fontWeight = FontWeight.Light,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MultilineLabelPreview() {
    Column(
        modifier = Modifier
            .width(200.dp)
            .padding(8.dp)
    ) {
        MultilineLabel("Title", "Body")
    }
}

@Composable
fun MultilineLabel(title: String, body: List<String>?) {
    val clipboardManager: ClipboardManager = LocalClipboardManager.current

    Column {
        Text(
            text = title,
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp
        )
        body?.let {
            if (body.all { it.isNotBlank() }) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .combinedClickable(
                            onClick = { },
                            onLongClick = {
                                clipboardManager.setText(
                                    AnnotatedString(
                                        body
                                            .joinToString(separator = ", ")
                                    )
                                )
                            },
                            onLongClickLabel = "Copied value"
                        ),
                ) {
                    items(body.size) {
                        Text(body.joinToString(separator = ", "))
                    }
                }
            } else {
                BlankPlaceholder()
            }
        } ?: BlankPlaceholder()
    }
}

@Preview(showBackground = true)
@Composable
fun MultilineLabelListPreview() {
    Column(
        modifier = Modifier
            .width(200.dp)
            .padding(8.dp)
    ) {
        MultilineLabel("Title", arrayListOf("One", "Two", "Three"))
    }
}

@Composable
fun BlankPlaceholder() {
    Text(
        text = "⚔",
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier
            .padding(horizontal = 16.dp)
    )
}