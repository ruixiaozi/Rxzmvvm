### 使用方法

1. 将其添加到存储库末尾的root build.gradle中：

```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

2. 添加依赖

```
dependencies {
        implementation 'com.github.ruixiaozi:Rxzmvvm:0.0.5'
}
```

3. 在Module的build.gradle中，android下添加dataBinding：

```
android {
    ...

    //开启数据绑定
    dataBinding {
        enabled = true
    }

    ...
```

4.初始化：在Application的OnCreate()中调用：

```
Rxzmvvm.init(getApplicationContext());
```



### 功能说明

1. 视图基类：BaseActivityView(沉浸式，切换动画)
    + turnTo(Activity activity, Class<K> kClass, Request request,boolean isNeedReturn) 跳转页面方法
    + returnBy(Result result) 页面返回方法
2. ViewModel基类：LoginViewModel
    + bind(T binding, V view)  绑定databinding与视图的方法，一般在创建后调用
    + getData(String name,Class<T> type) 获取视图数据对象，name为对象变量名称
    + setData(String name,K k) 设置视图数据，name为对象变量名，k为数据对象
    + void onInit(Request request) 当视图初始化数据时回调
    + onShow() 当视图显示时回调
    + onResult(Result result) 当视图接收到返回时回调
3. 集成RecycleView
    + 增加布局属性；`myAdapter` 和 `list`，分别指向一个MyRecycleAdapter对象，和一个List对象，实现自动适配，自动更新
    + 提供万能Adapter：MyRecycleAdapter
4. 屏幕适配：提供dp_xxx和sp_xxx的dimen
5. 动画：提供基础的进入退出动画
6. Drawable：按钮
7. 全局（Rxzmvvm类）静态方法：
    + toastShow(String msg)  提示字符串
    + getContext() 获取应用上下文