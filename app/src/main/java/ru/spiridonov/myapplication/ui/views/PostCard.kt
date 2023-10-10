package ru.spiridonov.myapplication.ui.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.spiridonov.myapplication.R
import ru.spiridonov.myapplication.ui.theme.ApplicationTheme

@Composable
fun PostCard() {
    Card {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape),
                painter = painterResource(id = R.drawable.post_comunity_thumbnail),
                contentDescription = null
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "/dev/null",
                    color = MaterialTheme.colorScheme.onPrimary
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "13:49",
                    color = MaterialTheme.colorScheme.onSecondary
                )
            }

            Icon(
                imageVector = Icons.Rounded.MoreVert,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSecondary
            )
        }
    }
}

@Preview
@Composable
private fun PreviewLight() {
    ApplicationTheme(darkTheme = false) {
        PostCard()
    }
}

@Preview
@Composable
private fun PreviewNight() {
    ApplicationTheme(darkTheme = true) {
        PostCard()
    }
}