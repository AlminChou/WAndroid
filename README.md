# WAndroid - 开源玩Android小项目

* MVI架构 single Activity multi Fragment
* 协程
* flow
* koin
* retrofit
* moshi
* room
* paging3
* coil
* viewbinding扩展函数
* compose（部分页面）
* navigation
* xCrash
* FlowBus

#### mvi基建参考 ： https://proandroiddev.com/mvi-architecture-with-kotlin-flows-and-channels-d36820b2028d

#### Activity 
借用了Activity 共享viewModel 来存储全局常用的数据和状态，提供订阅 例如： user信息

#### ui基类
* Androidx Fragment ，Activity， 全新权限申请api
* viewbingdingKtx    参考：https://github.com/DylanCaiCoding/ViewBindingKTX

#### Navigator:
路径：com.ui.navigator
* AppNavigator : 定义了统一的常规跳转api，注释了一些Navigation api使用的行为记录，方便开发
* 自定义CustomFragmentNavigator，修改原生的FragmentNavigator 加入 Add Replace添加方式
* TabFragmentNavigator ： 主要解决了切换tab时候fragment重建的问题，保留了该类作为记录； 项目后来还是换回了直接简单的show hide实现了main tab主页

#### dsl语法封装请求 - 封装在基类ViewModel 以及 基类Repository
默认flow形式请求
```kotlin
 viewModelScope.launch {
    flow {
        emit(userRepository.login(account, password))
    }.onStart {
        setState { LoginContract.State.Logining }
    }.apiCatch {
        setState { LoginContract.State.LoginFailed }
    }.collect{
        setState { LoginContract.State.LoginSuccess(it) }
    }
 }

```
1.简约请求 
```kotlin
api<UserInfo> {
    call { userRepository.register(account, password, repeat) }
    prepare { setState { RegisterContract.State.SignIning} }
    success {
        setState { RegisterContract.State.SignInSuccess}
    }
    failed {
        setState { RegisterContract.State.SignInFailed }
    }
}
```

2.缓存请求（多模式策略）  Repository$networkBoundResource  
 英文不好，是在难起名
```kotlin
sealed class Category {
    object CacheFirstFetchCover : Category() // 优先展示缓存 ，请求覆盖
    object FetchFailedCacheCover : Category() // 请求失败后，读取缓存覆盖
    object QueryAfterFetchSave : Category()  //  请求成功后写入缓存，读取缓存进行覆盖
    object CacheFirstQueryCoverAfterFetch : Category()  //  优先展示缓存， 请求成功后写入缓存，读取缓存进行覆盖
}
```
```kotlin
// LoginViewModel
api(userRepository.login(account, password)){
    prepare {
        setState { LoginContract.State.Logining }
    }
    cache {
    }
    success {
        setState { LoginContract.State.LoginSuccess(it) }
    }
    failed {
        setState { LoginContract.State.LoginFailed } 
    }
}
// UserRepository
fun login(account: String, password: String) : Flow<Resource<UserInfo>>{
    return networkBoundResource(
        cacheQuery = {
            // 读取缓存
            flow <UserInfo> {
                val users = userDao.allUserInfo()
                if(!users.isNullOrEmpty()){
                    emit(users.first())
                }
            }
        },
        fetch = {
            //api 网络请求 
            userApiService.login(account, password)
        },
        onFetchFailed = {
         
        },
        saveFetchResult = {
           // 保存缓存操作
            userDao.insert(it)
        },
        shouldFetch = {
            true
        }, category = Category.QueryAfterFetchSave
    )
}

```


#### 自定义Json-MoshiConverter 避免重复定义 接口包裹类  Response<T>,  只需直接定义最终class类型返回 <T>

解决：
```kotlin
@FormUrlEncoded
@POST("user/login")
suspend fun login(
@Field("username") username: String,
@Field("password") pwd: String
): ApiResponse<UserInfo>
```

最终：
```kotlin 
@FormUrlEncoded
@POST("user/login")
suspend fun login(
@Field("username") username: String,
@Field("password") pwd: String
): UserInfo
```

#### Fragment 混合 Compose示例
HomeFragment 特意采用了 Fragment 混合 Compose作为示例  -- （一边学习compose， compose开发真的快啊，一个列表ui一下子就完事 真爽）
* 封装自定义 上拉刷新、下拉刷新、loadMore等状态 RefreshList控件

#### 内置ext扩展函数
* ViewModel.pager() （分页请求快速api） 
* Repository.remoteMediatorPager () (使用自建key表 以及 RemoteMediator 、 room实现的分页缓存) //参考：https://developer.android.com/topic/libraries/architecture/paging/v3-network-db#remote-keys
目前remoteMediator参考资料比较少，谷歌百度了很久，参考回来的还是会有问题（谷歌有的是把所有数据都存key，有的只存最后一个，2个都试过还是有点问题） 在不断对比与尝试修改下只能暂时做成这样，我当前发现的是 偶然间上拉会一瞬间瞬移到某个位置，上拉加载状态动画也会不自然，希望大佬们帮我看一眼指出修改


#### 缓存：
项目中有些位置其实是用datastore/mmkv 等key value比较适合和方便，但是为了一些尝试，大部分直接用了room做

#### FlowBus
参考：https://mp.weixin.qq.com/s/U4SxMffMVIUC7X2LkyulTQ
 
#### 图片icon:
部分icon来源于 https://www.iconfont.cn/
部分来源于网络
