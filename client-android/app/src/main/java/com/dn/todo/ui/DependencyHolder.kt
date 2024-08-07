package com.dn.todo.ui

import com.dn.todo.data.repository.RemoteTaskRepository
import com.dn.todo.domain.TaskManager
import com.dn.todo.domain.TaskRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
object DependencyHolder {
    @Provides
    @ViewModelScoped
    fun provideTaskManager(repository: TaskRepository) = TaskManager(repository)

    @Provides
    @ViewModelScoped
    fun provideTaskRepository(): TaskRepository = RemoteTaskRepository(timeoutMs = 1000L)
}