package com.dn.todo.data.repository

import retrofit2.Response

class TaskApiException(response: Response<*>): RuntimeException(
    "${response.code()}: ${response.errorBody()}"
)