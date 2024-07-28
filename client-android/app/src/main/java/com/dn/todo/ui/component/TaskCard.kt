package com.dn.todo.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dn.todo.ui.theme.AppTheme
import com.dn.todo.ui.theme.AppTypography

@Preview
@Composable
fun TaskCardPreview() {
    AppTheme {
        TaskCard(
            "Custom task",
            "Some very long text that can't be compacted to single string will end with ellipsize",
            true
        )
    }
}

@Composable
fun TaskCard(
    title: String,
    description: String?,
    isCompleted: Boolean,
    onCheckedChange: (Boolean) -> Unit = {},
    onClick: () -> Unit = {}
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(checked = isCompleted, onCheckedChange = onCheckedChange)
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = title,
                    style = AppTypography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )

                if (description != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = description,
                        style = AppTypography.bodyLarge,
                        color = MaterialTheme.colorScheme.secondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}