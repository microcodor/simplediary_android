package com.sina.weibo.sdk.codestyle;

import java.util.HashMap;

/**
 * 微博错误码
 * Created by jinchun on 2017/1/21.
 */

public class ErrorCode {
    public static HashMap<Integer,String> ERRORMESSAGE=null;

    public static HashMap<Integer,String> getErrorMap(){
        if (ERRORMESSAGE==null){
            ERRORMESSAGE = new HashMap<>();
            init();
        }
        return ERRORMESSAGE;
    }

    public static String getErrorMsg(int sendStatus){
        String error = getErrorMap().get(sendStatus);
        if (error==null){
            error = "";
        }
        return error;
    }

    public static void init(){
        //系统级错误代码
        ERRORMESSAGE.put(10001, "系统错误");
        ERRORMESSAGE.put(10002, "服务暂停");
        ERRORMESSAGE.put(10004, "IP限制不能请求该资源");
        ERRORMESSAGE.put(10006, "缺少source (appkey) 参数");
        ERRORMESSAGE.put(10007, "不支持的MediaType (%s)");
        ERRORMESSAGE.put(10008, "参数错误，请参考API文档");
        ERRORMESSAGE.put(10009, "任务过多，系统繁忙");
        ERRORMESSAGE.put(10010, "任务超时");
        ERRORMESSAGE.put(10011, "RPC错误");
        ERRORMESSAGE.put(10012, "非法请求");
        ERRORMESSAGE.put(10013, "不合法的微博用户");
        ERRORMESSAGE.put(10014, "应用的接口访问权限受限");
        ERRORMESSAGE.put(10016, "缺失必选参数 (%s)，请参考API文档");
        ERRORMESSAGE.put(10017, "参数值非法");
        ERRORMESSAGE.put(10018, "请求长度超过限制");
        ERRORMESSAGE.put(10020, "接口不存在");
        ERRORMESSAGE.put(10021, "请求的HTTP METHOD不支持，请检查是否选择了正确的POST/GET方式");
        ERRORMESSAGE.put(10022, "IP请求频次超过上限");
        ERRORMESSAGE.put(10023, "用户请求频次超过上限");
        ERRORMESSAGE.put(10024, "用户请求特殊接口 (%s) 频次超过上限");

        //服务级错误代码

        ERRORMESSAGE.put(20001, "IDs参数为空");
        ERRORMESSAGE.put(20002, "Uid参数为空");
        ERRORMESSAGE.put(20003, "用户不存在");
        ERRORMESSAGE.put(20005, "不支持的图片类型，仅仅支持JPG、GIF、PNG");
        ERRORMESSAGE.put(20006, "图片太大");
        ERRORMESSAGE.put(20007, "请确保使用multpart上传图片");
        ERRORMESSAGE.put(20008, "内容为空");
        ERRORMESSAGE.put(20009, "IDs参数太长了");
        ERRORMESSAGE.put(20012, "输入文字太长，请确认不超过140个字符");
        ERRORMESSAGE.put(20013, "输入文字太长，请确认不超过300个字符");
        ERRORMESSAGE.put(20014, "安全检查参数有误，请再调用一次");
        ERRORMESSAGE.put(20015, "账号、IP或应用非法，暂时无法完成此操作");
        ERRORMESSAGE.put(20016, "发布内容过于频繁");

        ERRORMESSAGE.put(20017, "提交相似的信息");
        ERRORMESSAGE.put(20018, "包含非法网址");
        ERRORMESSAGE.put(20019, "提交相同的信息");
        ERRORMESSAGE.put(20020, "包含广告信息");
        ERRORMESSAGE.put(20021, "包含非法内容");
        ERRORMESSAGE.put(20022, "此IP地址上的行为异常");
        ERRORMESSAGE.put(20031, "需要验证码");
        ERRORMESSAGE.put(20032, "发布成功，目前服务器可能会有延迟，请耐心等待1-2分钟");

        ERRORMESSAGE.put(20101, "不存在的微博");
        ERRORMESSAGE.put(20102, "不是你发布的微博");
        ERRORMESSAGE.put(20103, "不能转发自己的微博");
        ERRORMESSAGE.put(20104, "不合法的微博");
        ERRORMESSAGE.put(20109, "微博ID为空");
        ERRORMESSAGE.put(20111, "不能发布相同的微博");

        ERRORMESSAGE.put(20201, "不存在的微博评论");
        ERRORMESSAGE.put(20202, "不合法的评论");
        ERRORMESSAGE.put(20203, "不是你发布的评论");
        ERRORMESSAGE.put(20204, "评论ID为空");

        ERRORMESSAGE.put(20301, "不能给不是你粉丝的人发私信");
        ERRORMESSAGE.put(20302, "不合法的私信");
        ERRORMESSAGE.put(20303, "不是属于你的私信");
        ERRORMESSAGE.put(20305, "不存在的私信");
        ERRORMESSAGE.put(20306, "不能发布相同的私信");
        ERRORMESSAGE.put(20307, "非法的私信ID");

        ERRORMESSAGE.put(21332, "access_token 无效");
//        ERRORMESSAGE.put(, "");
//        ERRORMESSAGE.put(, "");
//        ERRORMESSAGE.put(, "");
//        ERRORMESSAGE.put(, "");
//        ERRORMESSAGE.put(, "");
//        ERRORMESSAGE.put(, "");
//        ERRORMESSAGE.put(, "");
//        ERRORMESSAGE.put(, "");
//        ERRORMESSAGE.put(, "");
//        ERRORMESSAGE.put(, "");
//        ERRORMESSAGE.put(, "");
//        ERRORMESSAGE.put(, "");
//        ERRORMESSAGE.put(, "");
//        ERRORMESSAGE.put(, "");
//        ERRORMESSAGE.put(, "");
//        ERRORMESSAGE.put(, "");
//        ERRORMESSAGE.put(, "");
//        ERRORMESSAGE.put(, "");
//        ERRORMESSAGE.put(, "");
//        ERRORMESSAGE.put(, "");
//        ERRORMESSAGE.put(, "");
//        ERRORMESSAGE.put(, "");
//        ERRORMESSAGE.put(, "");
//        ERRORMESSAGE.put(, "");
//        ERRORMESSAGE.put(, "");
//        ERRORMESSAGE.put(, "");
//        ERRORMESSAGE.put(, "");
//        ERRORMESSAGE.put(, "");
//        ERRORMESSAGE.put(, "");
//        ERRORMESSAGE.put(, "");
//        ERRORMESSAGE.put(, "");
//        ERRORMESSAGE.put(, "");
//        ERRORMESSAGE.put(, "");
//        ERRORMESSAGE.put(, "");
//        ERRORMESSAGE.put(, "");



    }
}
