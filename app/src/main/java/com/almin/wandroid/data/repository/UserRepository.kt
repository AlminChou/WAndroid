package com.almin.wandroid.data.repository

import com.almin.arch.repository.Repository
import com.almin.arch.repository.Resource
import com.almin.wandroid.data.db.UserDao
import com.almin.wandroid.data.model.UserInfo
import com.almin.wandroid.data.network.api.UserApiService
import kotlinx.coroutines.flow.*
import java.lang.NullPointerException

/**
 * Created by Almin on 2022/1/4.
 */
class UserRepository(private val userApiService: UserApiService, private val userDao: UserDao) : Repository(){

//    suspend fun login(account: String, password: String) : UserInfo{
//        val user = userApiService.login(account, password)
//        userDao.insert(user)
//        return user
//    }

    fun login(account: String, password: String) : Flow<Resource<UserInfo>>{
        return networkBoundResource(
            cacheQuery = {
                flow <UserInfo> {
//                    throw NullPointerException("123123213213213213123122  empty")
                    emit(userDao.allUserInfo().first())
                }
//                    .catch {
//                    emitAll(emptyFlow<UserInfo>())
//                    println("123123213213213213123122  empty  catch")
//                }
//              callbackFlow {  }
            },
            fetch = {
                userApiService.login(account, password)
            },
            onFetchFailed = {

            },
            saveFetchResult = {
                userDao.insert(it)
            },
            shouldFetch = {
                true
            }, category = Category.QueryAfterFetchSave
        )
    }


    suspend fun register(account: String, password: String, repeat: String) = userApiService.register(account, password, repeat)

    suspend fun updateUserCache(userInfo: UserInfo) = userDao.insert(userInfo)

    suspend fun allUserInfo() = userDao.allUserInfo()

    fun loadUserInfo() = userDao.loadUserInfo()

}