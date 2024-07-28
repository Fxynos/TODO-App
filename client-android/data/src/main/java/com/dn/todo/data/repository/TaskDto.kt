package com.dn.todo.data.repository

import com.google.gson.annotations.SerializedName

internal class TaskDto {
    var id: Long? = null
    var title: String = ""
    var description: String? = null
    @SerializedName("is_completed")
    var isCompleted: Boolean? = null
}