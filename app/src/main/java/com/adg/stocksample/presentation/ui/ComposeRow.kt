package com.adg.stocksample.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Checkbox
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.adg.stocksample.presentation.ui.theme.StockSampleTheme

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun BaseRow(
    modifier: Modifier = Modifier,
    rowTitle: String? = null,
    rowDescription: String? = null,
    action: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit
) {
    val clickableModifier = if (action != null) modifier.clickable(onClick = action) else modifier
    return ConstraintLayout(
        modifier = clickableModifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        val (rowLayer, nestedLayer) = createRefs()
        Column(
            modifier = Modifier
                .constrainAs(rowLayer) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(nestedLayer.start, 8.dp)
                    width = Dimension.fillToConstraints
                }
        ) {
            rowTitle?.let { title ->
                Text(text = title, style = MaterialTheme.typography.h6, maxLines = 1)
            }
            rowDescription?.let { description ->
                Text(text = description, style = MaterialTheme.typography.body1)
            }
        }
        Box(
            modifier = Modifier
                .width(75.dp)
                .constrainAs(nestedLayer) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                    height = Dimension.fillToConstraints
                },
            content = content
        )
    }
}

@Composable
fun ActionRow(
    actionText: String,
    action: () -> Unit,
    modifier: Modifier = Modifier,
    rowTitle: String? = null,
    rowDescription: String? = null,
) = BaseRow(
    modifier = modifier,
    rowTitle = rowTitle,
    action = action,
    rowDescription = rowDescription
) {
    Text(
        text = actionText,
        style = MaterialTheme.typography.button,
        modifier = Modifier
            .padding(4.dp)
            .align(Alignment.Center)
    )
}

@Composable
fun DoubleTextRow(
    nestedTitle: String,
    nestedDescription: String,
    modifier: Modifier = Modifier,
    rowTitle: String? = null,
    rowDescription: String? = null,
) = BaseRow(modifier = modifier, rowTitle = rowTitle, rowDescription = rowDescription) {
    Column(
        horizontalAlignment = Alignment.End,
        modifier = Modifier.align(Alignment.TopEnd),
    ) {
        Text(
            text = nestedTitle,
            style = MaterialTheme.typography.h6,
            maxLines = 1
        )
        Text(
            text = nestedDescription,
            style = MaterialTheme.typography.body1,
            maxLines = 1
        )
    }
}

@Composable
fun CheckboxRow(
    checked: Boolean,
    action: () -> Unit,
    modifier: Modifier = Modifier,
    rowTitle: String? = null,
    rowDescription: String? = null,
) = BaseRow(
    modifier = modifier,
    rowTitle = rowTitle,
    action = action,
    rowDescription = rowDescription
) {
    Checkbox(
        checked = checked,
        onCheckedChange = null,
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
private fun PreviewSpacer() =
    Spacer(
        modifier = Modifier
            .height(8.dp)
            .fillMaxWidth()
            .background(Color.LightGray)
    )

@Preview(showBackground = true)
@Composable
fun BaseRowsPreview() {
    StockSampleTheme {
        Column {
            BaseRow(
                rowTitle = "Title",
                rowDescription = "Description",
                content = {}
            )
            PreviewSpacer()
            BaseRow(
                rowTitle = "Title",
                rowDescription = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incidi",
                content = {}
            )
            PreviewSpacer()
            BaseRow(
                rowTitle = "Title",
                content = {}
            )
            PreviewSpacer()
            BaseRow(
                rowDescription = "Description",
                content = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ActionRowsPreview() {
    StockSampleTheme {
        Column {
            ActionRow(
                rowTitle = "Title",
                rowDescription = "Description",
                actionText = "Action",
                action = {}
            )
            PreviewSpacer()
            ActionRow(
                rowTitle = "Title",
                rowDescription = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incidi",
                actionText = "Action",
                action = {}
            )
            PreviewSpacer()
            ActionRow(
                rowTitle = "Title",
                actionText = "Action",
                action = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DoubleTextRowsPreview() {
    StockSampleTheme {
        Column {
            DoubleTextRow(
                rowTitle = "Title",
                rowDescription = "Description",
                nestedTitle = "$500",
                nestedDescription = "$699"
            )
            PreviewSpacer()
            DoubleTextRow(
                rowTitle = "Title",
                rowDescription = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incidi",
                nestedTitle = "$500",
                nestedDescription = "$699"
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CheckboxRowsPreview() {
    StockSampleTheme {
        Column {
            CheckboxRow(
                rowTitle = "Title",
                rowDescription = "Description",
                checked = true,
                action = {}
            )
            PreviewSpacer()
            CheckboxRow(
                rowTitle = "Title",
                rowDescription = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incidi",
                checked = false,
                action = {}
            )
            PreviewSpacer()
            CheckboxRow(
                rowTitle = "Title",
                checked = true,
                action = {}
            )
        }
    }
}