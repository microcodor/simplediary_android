package com.wxdroid.simplediary.network;

import android.widget.RelativeLayout;

import com.wxdroid.simplediary.model.ResultModel;
import com.wxdroid.simplediary.model.bean.SimpleClassifyBean;
import com.wxdroid.simplediary.model.bean.SimpleDiscoverBean;
import com.wxdroid.simplediary.model.bean.TnGouGirlBean;
import com.wxdroid.simplediary.model.bean.ToutiaoBean;
import com.wxdroid.simplediary.model.bean.WeiboArticleBean;
import com.wxdroid.simplediary.model.bean.WeiboInviteBean;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 类名称：NetWorks
 * 创建人：wangliteng
 * 创建时间：2016-5-18 14:57:11
 * 类描述：网络请求的操作类
 */
public class NetWorksUtils extends RetrofitUtils {

    protected static final NetService service = getRetrofit(URLDefine.API_SERVER).create(NetService.class);
    protected static final NetTngouService serviceTngou = getCommonRetrofit(URLDefine.TNGOU_SERVER).create(NetTngouService.class);
    protected static final JuHeService serviceJuhe = getCommonRetrofit(URLDefine.JUHE_TOUTIAO_SERVER).create(JuHeService.class);

    //设缓存有效期为1天
    protected static final long CACHE_STALE_SEC = 60 * 60 * 24 * 1;
    //查询缓存的Cache-Control设置，使用缓存
    protected static final String CACHE_CONTROL_CACHE = "only-if-cached, max-stale=" + CACHE_STALE_SEC;
    //查询网络的Cache-Control设置。不使用缓存
    protected static final String CACHE_CONTROL_NETWORK = "max-age=0";

    private interface NetService {


//
//        //POST请求
//        @FormUrlEncoded
//        @POST("bjws/app.user/login")
//        Observable<Verification> getVerfcationCodePostMap(@FieldMap Map<String, String> map);
//
//        //GET请求
//        @GET("bjws/app.user/login")
//        Observable<Verification> getVerfcationGet(@Query("tel") String tel, @Query("password") String pass);
//
//
//        //GET请求，设置缓存
//        @Headers("Cache-Control: public," + CACHE_CONTROL_CACHE)
//        @GET("bjws/app.user/login")
//        Observable<Verification> getVerfcationGetCache(@Query("tel") String tel, @Query("password") String pass);
//
//
//        @Headers("Cache-Control: public," + CACHE_CONTROL_NETWORK)
//        @GET("bjws/app.menu/getMenu")
//        Observable<MenuBean> getMainMenu();

        //GET请求
//        @GET("getwpterms")
//        Observable<WptermsBean> getWpterms();
//
//        //GET请求
//        @GET("getsimpleposts/{termId}/{postId}/{num}")
//        Observable<WpPostsModelListBean> getSimplePosts(@Path("termId") long termId, @Path("postId") long postId, @Path("num") int num);
//
//        @GET("getwppost/{postId}")
//        Observable<WpPostsModelBean> getWpPost(@Path("postId") long postId);
//
//        //GET请求
//        @GET("searchsimpleposts/{keyword}/{index}/{num}")
//        Observable<WpPostsModelListBean> searchSimplePosts(@Path("keyword") String keyword, @Path("index") int index, @Path("num") int num);

//        @FormUrlEncoded
//        @POST("user/login")
//        Observable<ResultModel> login(@FieldMap Map<String, String> map);

        @GET("common/verifyinvite")
        Observable<WeiboInviteBean> verifyinvite(@Query("uid") String uid);

        @FormUrlEncoded
        @POST("common/setupinvite")
        Observable<ResultModel> setupinvite(@FieldMap Map<String, String> map);

        @Headers({"Content-Type: application/json","Accept: application/json"})//需要添加头
        @POST("user/updateuser")
        Observable<ResultModel> updateuser(@Body RequestBody body);

        @FormUrlEncoded
        @POST("user/updateoauth")
        Observable<ResultModel> updateoauth(@FieldMap Map<String, String> map);

        @FormUrlEncoded
        @POST("status/timersend")
        Observable<ResultModel> timersend(@FieldMap Map<String, String> map);

        @GET("common/getqiniutoken")
        Observable<ResultModel> getqiniutoken(@Query("uid") String uid);

        @GET("classify/getclassifies")
        Observable<SimpleClassifyBean> getclassifies();

        @GET("discover/pagelist")
        Observable<SimpleDiscoverBean> pagelist(@Query("classifytype") int classifytype, @Query("page") int page, @Query("size") int size);

        @GET("status/timerlist")
        Observable<WeiboArticleBean> timerlist(@Query("uid") String uid, @Query("sendstatus") int sendstatus, @Query("page") int page, @Query("size") int size);

        @FormUrlEncoded
        @POST("status/updatearticle")
        Observable<ResultModel> updatearticle(@FieldMap Map<String, String> map);

    }
    private interface NetTngouService {
        @GET("tnfs/api/list")
        Observable<TnGouGirlBean> tngougirllist(@Query("page") int page, @Query("rows") int rows);
    }
    private interface JuHeService {
        @GET("toutiao/index")
        Observable<ToutiaoBean> toutiaolist(@Query("type") String type, @Query("key") String key);
    }

    //POST请求
//    public static void verfacationCodePost(String tel, String pass, Observer<Verification> observer){
//        setSubscribe(service.getVerfcationCodePost(tel, pass),observer);
//    }
//
//
//    //POST请求参数以map传入
//    public static void verfacationCodePostMap(Map<String, String> map, Observer<Verification> observer) {
//       setSubscribe(service.getVerfcationCodePostMap(map),observer);
//    }
//
//    //Get请求设置缓存
//    public static void verfacationCodeGetCache(String tel, String pass, Observer<Verification> observer) {
//        setSubscribe(service.getVerfcationGetCache(tel, pass),observer);
//    }
//
//    //Get请求
//    public static void verfacationCodeGet(String tel, String pass, Observer<Verification> observer) {
//        setSubscribe(service.getVerfcationGet(tel, pass),observer);
//    }
//
//    //Get请求
//    public static void verfacationCodeGetsub(String tel, String pass, Observer<Verification> observer) {
//        setSubscribe(service.getVerfcationGet(tel, pass),observer);
//    }
//
//    //Get请求
//    public static void Getcache( Observer<MenuBean> observer) {
//        setSubscribe(service.getMainMenu(),observer);
//    }
    //Get请求
//    public static void GetWpterms(Observer<WptermsBean> observer) {
//        setSubscribe(service.getWpterms(), observer);
//    }
//
//    public static void GetSimplePosts(long termId, long postId, int num, Observer<WpPostsModelListBean> observer) {
//        setSubscribe(service.getSimplePosts(termId, postId, num), observer);
//    }
//
//    //public static void GetSimplePosts(Observer<>)
//    public static void GetWpPost(long postId, Observer<WpPostsModelBean> observer) {
//        setSubscribe(service.getWpPost(postId), observer);
//    }
//
//    public static void SearchSimplePosts(String keyword, int index, int num, Observer<WpPostsModelListBean> observer) {
//        setSubscribe(service.searchSimplePosts(keyword, index, num), observer);
//    }

    /**
     * 验证用户是否已开通权限
     * */
    public static void verifyinvite(String uid, Observer<WeiboInviteBean> observer){
        setSubscribe(service.verifyinvite(uid),observer);
    }

    /**
     * 管理员用户功能
     * */
    public static void setupinvite(Map<String, String> map, Observer<ResultModel> observer){
        setSubscribe(service.setupinvite(map), observer);
    }

    /**
     * 更新服务器授权记录
     * */
    public static void updateoauth(Map<String, String> map, Observer<ResultModel> observer) {
        setSubscribe(service.updateoauth(map), observer);
    }
    /**
     * 登录接口，验证用户是否已激活，更新用户信息
     * */
    public static void updateuser(RequestBody body, Observer<ResultModel> observer) {
        setSubscribe(service.updateuser(body), observer);
    }
    /**
     * 定时发微博接口
     * */
    public static void timersend(Map<String, String> map, Observer<ResultModel> observer) {
        setSubscribe(service.timersend(map), observer);
    }
    /**
     * 获取7牛token
     * */
    public static void getqiniutoken(String uid, Observer<ResultModel> observer){
        setSubscribe(service.getqiniutoken(uid),observer);
    }

    /**
     * 获取所有分类
     * */
    public static void getclassifies(Observer<SimpleClassifyBean> observer){
        setSubscribe(service.getclassifies(),observer);
    }
    /**
     * 获取领域分页内容
     * */
    public static void pagelist(int classifytype, int page, int size, Observer<SimpleDiscoverBean> observer){
        setSubscribe(service.pagelist(classifytype,page,size),observer);
    }

    /**
     * 获取定时记录
     * */
    public static void timerlist(String uid, int sendstatus, int page, int size, Observer<WeiboArticleBean> observer){
        setSubscribe(service.timerlist(uid, sendstatus,page,size),observer);
    }

    /**
     * 修改定时消息
     * */
    public static void updatearticle(Map<String, String> map, Observer<ResultModel> observer){
        setSubscribe(service.updatearticle(map), observer);
    }


    /**
     * 天狗云图片列表
     * */
    public static void tngougirllist(int page, int rows, Observer<TnGouGirlBean> observer){
        setSubscribe(serviceTngou.tngougirllist(page, rows),observer);
    }

    /**
     * 聚合娱乐
     * */
    public static void toutiaolist(String type, String key, Observer<ToutiaoBean> observer){
        setSubscribe(serviceJuhe.toutiaolist(type,key),observer);
    }

    /**
     * 插入观察者
     *
     * @param observable
     * @param observer
     * @param <T>
     */
    public static <T> void setSubscribe(Observable<T> observable, Observer<T> observer) {
        observable.subscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.newThread())//子线程访问网络
                .observeOn(AndroidSchedulers.mainThread())//回调到主线程
                .subscribe(observer);
    }

}
