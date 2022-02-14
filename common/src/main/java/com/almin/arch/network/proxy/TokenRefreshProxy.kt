//package com.almin.arch.network.proxy
//
//import com.almin.arch.scheduler.SchedulersProvider
//import java.lang.reflect.InvocationHandler
//import java.lang.reflect.InvocationTargetException
//import java.lang.reflect.Method
//import java.util.*
//
///**
// * Created by Almin on 2019-07-07.  以前自己研究一直在用的rxjava 自动刷新token重新请求的代理类
// */
//class TokenRefreshProxy(private val proxyObject: Any,
//                        private val tokenOperator: TokenOperator,
//                        private val schedulersProvider: SchedulersProvider
//) : InvocationHandler{
//
//
//    override fun invoke(proxy: Any?, method: Method?, args: Array<out Any>?): Any? {
//        if(method==null) return null
//
//        return when(method.returnType){
//            Observable::class.java -> {
//                return Observable.just("").flatMap {
//                    try {
//                        try {
//                            return@flatMap (method.invoke(proxyObject,args) as Observable<*>)
//                                    .subscribeOn(schedulersProvider.networkIO())
//                                    .observeOn(schedulersProvider.mainThread())
//                        }catch (e: InvocationTargetException){
//                            e.printStackTrace()
//                        }
//                    }catch (e: IllegalAccessException){
//                        e.printStackTrace()
//                    }
//                    return@flatMap null
//                }.retryWhen(object : Function<Observable<out Throwable>,Observable<*>> {
//                    private var retryTimes = 1
//                    override fun apply(throwable: Observable<out Throwable>): Observable<*> {
//                        return throwable.flatMap {
//                            if(retryTimes > 0 && (it is ApiException)){
//                                if(it.isTokenKind()){
//                                    retryTimes--
//                                    return@flatMap if(refreshTokenWhenTokenInvalid()) Observable.just(true) else Observable.error(it)
//                                }
//                            }
//                            return@flatMap throwable
//                        }
//                    }
//                })
//            }
//            Single::class.java -> {
//                return Single.just("").flatMap {
//                    try {
//                        try {
//                            return@flatMap (method.invoke(proxyObject,args) as Single<*>)
//                                    .subscribeOn(schedulersProvider.networkIO())
//                                    .observeOn(schedulersProvider.mainThread())
//                        }catch (e: InvocationTargetException){
//                            e.printStackTrace()
//                        }
//                    }catch (e: IllegalAccessException){
//                        e.printStackTrace()
//                    }
//                    return@flatMap null
//                }.retryWhen(object : Function<Flowable<out Throwable>,Publisher<*>>{
//                    private var retryTimes = 1
//                    override fun apply(throwable: Flowable<out Throwable>): Publisher<*> {
//                        return throwable.flatMap {
//                            if(retryTimes > 0 && (it is ApiException)){
//                                if(it.isTokenKind()){
//                                    retryTimes--
//                                    return@flatMap if(refreshTokenWhenTokenInvalid()) Flowable.just(true) else Flowable.error(it)
//                                }
//                            }
//                            return@flatMap throwable
//                        }
//                    }
//                })
//
//            }
//            Flowable::class.java -> {
//                return Flowable.just("").flatMap {
//                    try {
//                        try {
//                            return@flatMap (method.invoke(proxyObject,args) as Flowable<*>)
//                                    .subscribeOn(schedulersProvider.networkIO())
//                                    .observeOn(schedulersProvider.mainThread())
//                        }catch (e: InvocationTargetException){
//                            e.printStackTrace()
//                        }
//                    }catch (e: IllegalAccessException){
//                        e.printStackTrace()
//                    }
//                    return@flatMap null
//                }.retryWhen(object : Function<Flowable<out Throwable>,Publisher<*>>{
//                    private var retryTimes = 1
//                    override fun apply(throwable: Flowable<out Throwable>): Publisher<*> {
//                        return throwable.flatMap {
//                            if(retryTimes > 0 && (it is ApiException)){
//                                if(it.isTokenKind()){
//                                    retryTimes--
//                                    return@flatMap if(refreshTokenWhenTokenInvalid()) Flowable.just(true) else Flowable.error(it)
//                                }
//                            }
//                            return@flatMap throwable
//                        }
//                    }
//                })
//            }
//            else -> method.invoke(proxyObject,args)
//        }
//    }
//
//    private fun refreshTokenWhenTokenInvalid(): Boolean {
//        synchronized(TokenRefreshProxy::class.java) {
//            return if (!isEnableRefreshToken()) {
//                true
//            } else {
//                val token = tokenOperator.callRefreshUserTokenApi()
//                if (token != null) {
//                    tokenOperator.updateUpdateTokenLastTime(Date().time)
//                    tokenOperator.updateToken(token)
//                    true
//                } else {
//                    false
//                }
//            }
//        }
//    }
//
//
//    // more than 5 minutes
//    private fun isEnableRefreshToken(): Boolean {
//        return (Date().time - tokenOperator.lastUpdateTokenTime) / (1000 * 60) > 5
//    }
//
//    companion object{
//        private const val TAG = "Token_Proxy"
//        private const val TOKEN_JSON_KEY = "token"
//    }
//
//}