# DL10SimpleWeatherManager
简单天气信息获取工具

# 说明链接
CSDN：https://blog.csdn.net/sinat_38184748/article/details/89330588

# 前言
由于项目需要读取用户当前位置，然后在首页显示一些简单的天气信息，整理了一个工具，方便其他项目的统一使用；
![在这里插入图片描述](/img/1.png)
# 使用方法
## 添加依赖
导入**dlsimpleweathermanager-release-1.0.0.aar**文件到**libs**
在**build.gradle**中添加

```java
dependencies {
    implementation fileTree(dir: 'libs', include: ['*.jar'])
    ......
    //简单天气管理工具
    implementation(name: 'dlsimpleweathermanager-release-1.0.0', ext: 'aar')
}
```
如果想配合定位使用，可以参考我的另一个定位工具
[Android 简单易用的原生定位API封装管理工具](https://blog.csdn.net/sinat_38184748/article/details/89280289)
## 初始化

```java
DLSimpleWeatherUtils.init(getApplicationContext());
```
## 获取天气
获取天气的方法有两个，一个是带经纬度信息去查询，一个是不带；
带经纬度信息查询天气，
里面有4个参数

```java
     * @param latitude 纬度
     * @param longitude 经度
     * @param coordinateCode 坐标代码，代表输入的经纬度的标准来源
     * @param getWeatherListener 监听器
```

```java
DLSimpleWeatherUtils.checkWeather(location.getLatitude(), location.getLongitude(),
                    DLCoordinateCode.CODE_WGS84,onGetWeatherListener);
```
经纬度的标准码说明可以查看这个链接 
[安卓定位及坐标转换](http://jp1017.top/2016/05/23/%E5%AE%89%E5%8D%93%E5%AE%9A%E4%BD%8D%E5%8F%8A%E5%9D%90%E6%A0%87%E8%BD%AC%E6%8D%A2/)

![在这里插入图片描述](/img/2.png)不带经纬度信息查询天气，这样子会自动根据IP地址去查询经纬度，但是这样的结果并不会十分准确，偏差比较大，只适合实在无法获取定位的时候再使用。

```java
DLSimpleWeatherUtils.checkWeather(onGetWeatherListener);
```
## 监听器

```java
    /**
     * 天气获取监听器
     */
    private OnGetWeatherListener onGetWeatherListener = new OnGetWeatherListener() {
        @Override
        public void OnNetworkDisable() {
            Log.e(TAG, "没有打开网络，或没有网络权限");
        }

        @Override
        public void OnError(int step, int code) {
            // 这里会返回错误出现的步骤，和错误码
            Log.e(TAG, "step = " + step + "; code = " + code);
        }

        @Override
        public void OnGetWeather(DLWeatherInfo weatherInfo) {
            // 返回获得的天气信息
            Log.e(TAG, weatherInfo.toString());
            txt.setText(weatherInfo.toString());
        }

        @Override
        public void OnGetLatAndLon(double latitude, double longitude) {
            // 这里返回获得的经纬度信息
            Log.e(TAG, "latitude = " + latitude + "; longitude = " + longitude);
        }

        @Override
        public void OnGetRealAddress(DLPlaceInfo placeInfo) {
            // 这里返回获取的地址信息
            Log.e(TAG, placeInfo.getInfo());
        }
    };
```
## 结果
![在这里插入图片描述](/img/3.png)
天气代码对应天气图标，去中国天气网站找就可以了，当然我的demo里面也会有项目里使用到的。

