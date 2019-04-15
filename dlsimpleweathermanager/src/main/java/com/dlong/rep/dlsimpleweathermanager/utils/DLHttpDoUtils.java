package com.dlong.rep.dlsimpleweathermanager.utils;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.dlong.rep.dlsimpleweathermanager.model.DLErrorCode;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

import static com.dlong.rep.dlsimpleweathermanager.utils.DLStringUtils.streamToString;

/**
 * 网络工具
 * ----------------------------
 * 当使用的地址开头是http而不是https时记得在
 * AndroidManifest.xml
 * 的 <application 节点里添加
 * android:usesCleartextTraffic="true"
 * 详情看 https://blog.csdn.net/sinat_38184748/article/details/88795699
 * ----------------------------
 * 1、get
 * 2、post
 * ----------------------------
 * @author  dlong
 * created at 2019/4/13 3:03 PM
 */
public class DLHttpDoUtils {

    private static final String TAG = "DLHttpDoUtils";

    /**
     * get请求
     *
     * @param mHandler 回调
     * @param what tag
     * @param baseUrl 基础地址
     * @param paramsMap 携带参数
     */
    public static void requestGet(Handler mHandler, int what, String baseUrl,
                                  HashMap<String, String> paramsMap, String charsetName) {
        Message message = new Message();
        message.what = what;
        try {
            StringBuilder tempParams = new StringBuilder();
            int pos = 0;
            for (String key : paramsMap.keySet()) {
                if (pos > 0) {
                    tempParams.append("&");
                }
                tempParams.append(String.format("%s=%s", key, URLEncoder.encode(paramsMap.get(key),"utf-8")));
                pos++;
            }
            String requestUrl = baseUrl + tempParams.toString();
            // 新建一个URL对象
            URL url = new URL(requestUrl);
            // 打开一个HttpURLConnection连接
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            // 设置连接主机超时时间
            urlConn.setConnectTimeout(5 * 1000);
            // 设置从主机读取数据超时
            urlConn.setReadTimeout(5 * 1000);
            // 设置是否使用缓存  默认是true
            urlConn.setUseCaches(true);
            // 设置为Post请求
            urlConn.setRequestMethod("GET");
            // urlConn设置请求头信息
            // 设置请求中的媒体类型信息。
            urlConn.setRequestProperty("Content-Type", "application/json");
            // 设置客户端与服务连接类型
            urlConn.addRequestProperty("Connection", "Keep-Alive");
            // 开始连接
            urlConn.connect();
            // 判断请求是否成功
            if (urlConn.getResponseCode() == DLErrorCode.HTTP_OK) {
                // 获取返回的数据
                String result = streamToString(urlConn.getInputStream(), charsetName);
                if (null == result){
                    message.arg1 = DLErrorCode.HTTP_DATA_CONVERSION_ERROR;
                } else {
                    message.arg1 = DLErrorCode.HTTP_OK;
                    message.obj = result;
                    Log.e(TAG, "Get方式请求成功，result--->" + result);
                }
            } else {
                message.arg1 = urlConn.getResponseCode();
                Log.e(TAG, "Get方式请求失败" + urlConn.getResponseCode());
            }
            // 关闭连接
            urlConn.disconnect();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            message.arg1 = DLErrorCode.HTTP_REQ_ERROR;
        }
        mHandler.sendMessage(message);
    }

    /**
     * post请求
     *
     * @param mHandler 回调
     * @param what tag
     * @param baseUrl 基础地址
     * @param paramsMap 参数
     */
    public static void requestPost(Handler mHandler, int what, String baseUrl,
                                   HashMap<String, String> paramsMap, String charsetName) {
        Message message = new Message();
        message.what = what;
        try {
            //合成参数
            StringBuilder tempParams = new StringBuilder();
            int pos = 0;
            for (String key : paramsMap.keySet()) {
                if (pos > 0) {
                    tempParams.append("&");
                }
                tempParams.append(String.format("%s=%s", key,  URLEncoder.encode(paramsMap.get(key),"utf-8")));
                pos++;
            }
            String params = tempParams.toString();
            // 请求的参数转换为byte数组
            byte[] postData = params.getBytes();
            // 新建一个URL对象
            URL url = new URL(baseUrl);
            // 打开一个HttpURLConnection连接
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            // 设置连接超时时间
            urlConn.setConnectTimeout(5 * 1000);
            //设置从主机读取数据超时
            urlConn.setReadTimeout(5 * 1000);
            // Post请求必须设置允许输出 默认false
            urlConn.setDoOutput(true);
            //设置请求允许输入 默认是true
            urlConn.setDoInput(true);
            // Post请求不能使用缓存
            urlConn.setUseCaches(false);
            // 设置为Post请求
            urlConn.setRequestMethod("POST");
            //设置本次连接是否自动处理重定向
            urlConn.setInstanceFollowRedirects(true);
            // 配置请求Content-Type
            urlConn.setRequestProperty("Content-Type", "application/json");
            // 开始连接
            urlConn.connect();
            // 发送请求参数
            DataOutputStream dos = new DataOutputStream(urlConn.getOutputStream());
            dos.write(postData);
            dos.flush();
            dos.close();
            // 判断请求是否成功
            if (urlConn.getResponseCode() == DLErrorCode.HTTP_OK) {
                // 获取返回的数据
                String result = streamToString(urlConn.getInputStream(), charsetName);
                if (null == result){
                    message.arg1 = DLErrorCode.HTTP_DATA_CONVERSION_ERROR;
                } else {
                    message.arg1 = DLErrorCode.HTTP_OK;
                    message.obj = result;
                    Log.e(TAG, "Post方式请求成功，result--->" + result);
                }
            } else {
                message.arg1 = urlConn.getResponseCode();
                Log.e(TAG, "Post方式请求失败" + urlConn.getResponseCode());
            }
            // 关闭连接
            urlConn.disconnect();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            message.arg1 = DLErrorCode.HTTP_REQ_ERROR;
        }
        mHandler.sendMessage(message);
    }

}
