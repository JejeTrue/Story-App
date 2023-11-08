package com.jejetrue.storyofj.data.repository
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.storysubmissionapp.data.local.database.StoryDatabase
import com.example.storysubmissionapp.data.model.Story
import com.example.storysubmissionapp.data.model.UserModel
import com.example.storysubmissionapp.data.response.LoginResponse
import com.example.storysubmissionapp.data.response.StoriesResponse
import com.example.storysubmissionapp.data.response.StoryResponse
import com.jejetrue.storyofj.data.StoryRemoteMediator
import com.jejetrue.storyofj.data.api.response.RegisterResponse
import com.jejetrue.storyofj.data.api.retrofit.ApiService
import com.jejetrue.storyofj.data.api.Result
import com.jejetrue.storyofj.data.pref.UserPreferences
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UserRepository private constructor(private val preference: UserPreferences, private val apiService: ApiService, private val database: StoryDatabase){

    @OptIn(ExperimentalPagingApi::class)
    fun getStories(token: String): LiveData<PagingData<Story>>{
        return Pager(config = PagingConfig(pageSize = 5), remoteMediator = StoryRemoteMediator(database,apiService,token), pagingSourceFactory = {
            database.storyDao().getAllStory()
        }).liveData
    }

    fun getStoriesLocation(
        token: String
    ): LiveData<Result<StoriesResponse>> =
        liveData {
            emit(Result.Loading)
            try {
                val response = apiService.getStories("Bearer $token", 1, 75, 1)
                if (response.error) {
                    emit(Result.Error("Stories Error: ${response.message}"))
                    Log.d("Stories Error", response.message)
                } else {
                    emit(Result.Success(response))
                    Log.d("Stories Success", response.message)
                }
            } catch (e: Exception) {
                emit(Result.Error("Error : ${e.message.toString()}"))
                Log.d("Stories Exception", e.message.toString())
            }
        }

    fun uploadImage(
        token: String,
        imageUri: MultipartBody.Part,
        description: RequestBody
    ): LiveData<Result<RegisterResponse>> =
        liveData {
            emit(Result.Loading)
            try {
                val response = apiService.uploadStory("bearer $token", imageUri, description)
                if (response.error) {
                    emit(Result.Error("Upload Error: ${response.message}"))
                    Log.d("Upload Error", response.message)
                } else {
                    emit(Result.Success(response))
                    Log.d("Upload Success", response.message)
                }
            } catch (e: Exception) {
                emit(Result.Error("Error : ${e.message.toString()}"))
                Log.d("Upload Exception", e.message.toString())
            }
        }

    fun getDetailStory(
        token: String,
        id: String
    ): LiveData<Result<StoryResponse>> =
        liveData {
            emit(Result.Loading)
            try {
                val response = apiService.getDetailStory("Bearer $token", id)
                if (response.error) {
                    emit(Result.Error("Detail Error: ${response.message}"))
                    Log.d("Detail Error", response.message)
                } else {
                    emit(Result.Success(response))
                    Log.d("Detail Success", response.message)
                }
            } catch (e: Exception) {
                emit(Result.Error("Error : ${e.message.toString()}"))
                Log.d("Detail Exception", e.message.toString())
            }
        }


    fun getSession() = preference.getSession()

    suspend fun logout() {
        preference.logout()
    }

    fun registerUser(
        name: String,
        email: String,
        password: String
    ): LiveData<Result<String>> =
        liveData {
            emit(Result.Loading)
            try {
                val response = apiService.register(name, email, password)
                if (response.error) {
                    emit(Result.Error("Register Error: ${response.message}"))
                    Log.d("Register Error", response.message)
                } else {
                    emit(Result.Success("User Created"))
                    Log.d("Register Success", response.message)
                }
            } catch (e: Exception) {
                emit(Result.Error("Error : ${e.message.toString()}"))
                Log.d("Register Exception", e.message.toString())
            }
        }

    fun loginUser(
        email: String,
        password: String
    ): LiveData<Result<LoginResponse>> =
        liveData {
            emit(Result.Loading)
            try {
                val response = apiService.login(email, password)
                if (response.error) {
                    Log.d("Login Error", response.message)
                    emit(Result.Error("Login Error: ${response.message}"))
                } else {
                    Log.d("Login Success", response.message)
                    emit(Result.Success(response))

                    preference.saveSession(
                        UserModel(
                            response.loginResult.userId,
                            response.loginResult.name,
                            response.loginResult.token,
                            true
                        )
                    )
                }
            } catch (e: Exception) {
                Log.d("Login Exception", e.message.toString())
                emit(Result.Error("Error : ${e.message.toString()}"))
            }
        }


    companion object {
        private var instance: UserRepository? = null
        fun getInstance(preference: UserPreferences,apiService: ApiService, database: StoryDatabase): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(preference, apiService, database)
            }.also { instance = it }
    }
}