package com.dn.todo.ui.screen

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dn.todo.ui.R
import com.dn.todo.ui.theme.AppTheme
import com.dn.todo.ui.theme.AppTypography
import com.dn.todo.ui.viewmodel.TaskEditViewModel

@Preview(showBackground = true)
@Composable
fun TaskEditUiPreview() {
    AppTheme {
        TaskEditUi(title = "Задача", description = "Описание:\n- строка 2", isCompleted = true)
    }
}

@Composable
fun TaskEditScreen(
    viewModel: TaskEditViewModel,
    onBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.event.collect {
            when (it) {
                is TaskEditViewModel.DataDrivenEvent.NavigateBack -> onBack()
            }
        }
    }

    TaskEditUi(
        title = state.title,
        onTitleChange = viewModel::setTitle,
        description = state.description,
        onDescriptionChange = viewModel::setDescription,
        isCompleted = state.isCompleted,
        onCompletionChange = viewModel::setCompleted,
        isTitleValid = state.isTitleValid,
        isDescriptionValid = state.isDescriptionValid,
        isSaveButtonEnabled = state.areInputsValid,
        onBack = onBack,
        onSave = viewModel::saveTask,
        onDelete = viewModel::deleteTask
    )
}

@Composable
fun TaskEditUi(
    title: String,
    onTitleChange: (String) -> Unit = {},
    description: String,
    onDescriptionChange: (String) -> Unit = {},
    isCompleted: Boolean,
    onCompletionChange: (Boolean) -> Unit = {},
    isTitleValid: Boolean = true,
    isDescriptionValid: Boolean = true,
    isSaveButtonEnabled: Boolean = true,
    onBack: () -> Unit = {},
    onSave: () -> Unit = {},
    onDelete: () -> Unit = {}
) {
    Column {
        /* Top bar */
        Row(
            modifier = Modifier.padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ImageButton(drawableId = R.drawable.ic_back, onClick = onBack)
            Row(
                modifier = Modifier
                    .background(
                        if (isTitleValid)
                            MaterialTheme.colorScheme.secondaryContainer
                        else
                            MaterialTheme.colorScheme.errorContainer,
                        RoundedCornerShape(8.dp)
                    )
                    .padding(16.dp)
                    .weight(1f)
            ) {
                BasicTextField(
                    modifier = Modifier.weight(1f),
                    value = title,
                    onValueChange = onTitleChange
                )
            }
            Checkbox(checked = isCompleted, onCheckedChange = onCompletionChange)
        }

        /* Body */

        Spacer(Modifier.height(8.dp))
        Box(
            Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 16.dp)
                .background(
                    if (isDescriptionValid)
                        MaterialTheme.colorScheme.secondaryContainer
                    else
                        MaterialTheme.colorScheme.errorContainer,
                    RoundedCornerShape(8.dp)
                )
                .padding(16.dp)
        ) {
            BasicTextField(
                modifier = Modifier.fillMaxSize(),
                value = description,
                onValueChange = onDescriptionChange
            )
        }

        /* Bottom bar */

        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            ImageButton(
                modifier = Modifier.size(64.dp),
                drawableId = R.drawable.ic_delete,
                labelId = R.string.delete,
                onClick = onDelete
            )
            ImageButton(
                modifier = Modifier.size(64.dp),
                drawableId = R.drawable.ic_check,
                labelId = R.string.save,
                isEnabled = isSaveButtonEnabled,
                onClick = onSave
            )
        }
    }
}

@Composable
fun ImageButton(
    modifier: Modifier = Modifier,
    @DrawableRes drawableId: Int,
    isEnabled: Boolean = true,
    @StringRes labelId: Int? = null,
    onClick: () -> Unit = {}
) {
    IconButton(
        modifier = modifier,
        enabled = isEnabled,
        onClick = onClick
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                painter = painterResource(id = drawableId),
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                contentDescription = if (labelId == null) null else stringResource(labelId)
            )
            if (labelId != null)
                Text(text = stringResource(labelId), style = AppTypography.labelSmall)
        }
    }
}