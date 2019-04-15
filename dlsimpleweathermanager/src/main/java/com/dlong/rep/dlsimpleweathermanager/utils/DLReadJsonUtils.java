package com.dlong.rep.dlsimpleweathermanager.utils;

import android.content.Context;
import android.content.res.AssetManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 资源文件读取
 * @author  dlong
 * created at 2019/4/13 5:04 PM
 */
public class DLReadJsonUtils {

    private static final String TAG = "DLReadJsonUtils";

    private static final String FILE_CITI_CODE_JSON = "duba_city_code_190115.json";

    /**
     * 获得JSON资源
     * @param context 上下文
     * @return JSONObject
     */
    public static JSONObject getCityCodeJSON(Context context){
        try {
            return new JSONObject(getJson(FILE_CITI_CODE_JSON, context));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getJson(String fileName,Context context){
        StringBuilder stringBuilder = new StringBuilder();
        try {
            //获取assets资源管理器
            AssetManager assetManager = context.getAssets();
            //通过管理器打开文件并读取
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
}
