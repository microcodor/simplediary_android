package com.wxdroid.simplediary.api;

import android.os.Environment;
import android.util.Log;

import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.wxdroid.simplediary.ui.common.login.Constants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * 新浪微博自定义封装gif上传
 * Created by jinchun on 2017/1/10.
 */

public class GifUploadAPI {
    public static void uploadMultiFile(String content, String path, String lat, String lon, String token
            , final RequestListener listener) {
        final String url = "https://api.weibo.com/2/statuses/upload.json";
        InetAddress ipaddr = null;
        try {
            ipaddr = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        File file = new File(path);
        RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("pic", path.substring(path.lastIndexOf("/") + 1), fileBody)
                .addFormDataPart("status", content)
                .addFormDataPart("long", lon)
                .addFormDataPart("lat", lat)
                .build();
        Request request = new Request.Builder()
                .addHeader("Authorization", "OAuth2 " + token)
                .addHeader("API-RemoteIP", ipaddr.getHostAddress())
                .url(url)
                .post(requestBody)
                .build();


        final okhttp3.OkHttpClient.Builder httpBuilder = new OkHttpClient.Builder();
        OkHttpClient okHttpClient = httpBuilder
                //设置超时
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("okHttpClient", "uploadMultiFile() e=" + e);
                WeiboException weiboException = new WeiboException(e.getMessage());

                listener.onWeiboException(weiboException);
            }


            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i("okHttpClient-onResponse", "uploadMultiFile() response=" + response.body().string());
                listener.onComplete(response.body().string());
            }
        });
    }


//    public static Request  getFileRequest(String url,File file,Map<String, String> maps){
//        MultipartBody.Builder builder=  new MultipartBody.Builder().setType(MultipartBody.FORM);
//        if(maps==null){
//            builder.addPart( Headers.of("Content-Disposition", "form-data; name=\"file\";filename=\"file.gif\""), RequestBody.create(MediaType.parse("image/gif"),file)
//            ).build();
//
//        }else{
//            for (String key : maps.keySet()) {
//                builder.addFormDataPart(key, maps.get(key));
//            }
//
//            builder.addPart( Headers.of("Content-Disposition", "form-data; name=\"file\";filename=\"file.gif\""), RequestBody.create(MediaType.parse("image/gif"),file)
//            );
//
//        }
//        RequestBody body=builder.build();
//        return   new Request.Builder().url(url).post(body).build();
//
//    }

    /**
     * 异步get,直接调用
     */
    private void asyncGet(String imageurl) {
        OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder().get()
                .url(imageurl)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

//                Message message = handler.obtainMessage();
//                if (response.isSuccessful()) {
//                    message.what = IS_SUCCESS;
//                    message.obj = response.body().bytes();
//                    handler.sendMessage(message);
//                } else {
//                    handler.sendEmptyMessage(IS_FAIL);
//                }
            }
        });
    }

    /**
     * 同步写法，需要放在子线程中使用
     */
    public static String synchronizedGet(String imageurl) {
        final String fileDir = Environment.getExternalStorageDirectory() + Constants.FileDir + Constants.IMAGE_PATH;
        String fileName = "";
        File dirFile = new File(fileDir);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }

        OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder().get()
                .url(imageurl)
                .build();

        try {
            Response response = client.newCall(request).execute();
            InputStream is = response.body().byteStream();
            FileOutputStream fos = null;
            byte[] buffer = new byte[1024];
            int lenght = 0;
            fileName = imageurl.substring(imageurl.lastIndexOf("/") + 1);
            File tempFile = new File(fileDir, fileName);
            fos = new FileOutputStream(tempFile);
            //
            while ((lenght = is.read(buffer)) > 0) {
                fos.write(buffer, 0, lenght);
            }
            fos.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileDir + fileName;

    }


}
