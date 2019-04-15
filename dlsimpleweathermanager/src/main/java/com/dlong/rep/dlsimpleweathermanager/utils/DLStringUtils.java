package com.dlong.rep.dlsimpleweathermanager.utils;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * 字符串工具
 * @author  dlong
 * created at 2019/4/13 4:06 PM
 */
class DLStringUtils {

    private static final String TAG = "DLStringUtils";

    /**
     * 将输入流转换成字符串
     *
     * @param is 从网络获取的输入流
     * @param charsetName The name of a supported {@linkplain java.nio.charset.Charset
     *                    charset}
     * @return String
     */
    public static String streamToString(InputStream is, String charsetName) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = is.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            baos.close();
            is.close();
            byte[] byteArray = baos.toByteArray();
            return new String(byteArray, charsetName);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return null;
        }
    }
}
