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
        implementation 'com.github.ruixiaozi:Rxzmvvm:0.0.19'
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

    //JDK版本
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }

    ...
```

4.初始化：在Application的OnCreate()中调用：

```
Rxzmvvm.init(getApplicationContext());
```



### 功能说明

1. 视图基类：
    1. BaseActivityView(沉浸式，切换动画)
        + init(int layout_id) 初始化视图的方法，在子视图的OnCreate方法里调用
        + turnTo(Activity activity, Class<K> kClass, Request request,boolean isNeedReturn) 跳转页面方法
        + returnBy(Result result) 页面返回方法
    2. BaseFragmentView 
        + View init(@NonNull LayoutInflater inflater,@Nullable ViewGroup container,int layout_id) 初始化视图
        + turnTo(Class<K> kClass, Request request, boolean isNeedReturn) 跳转页面方法
2. ViewModel基类：LoginViewModel
    + bind(T binding, V view)  绑定databinding与视图的方法，一般在创建后调用
    + getData(String name,Class<T> type) 获取视图数据对象，name为对象变量名称
    + setData(String name,K k) 设置视图数据，name为对象变量名，k为数据对象
    + void onInit(Request request) 当视图初始化数据时回调
    + onShow() 当视图显示时回调
    + boolean onBack() 当点击返回按钮时触发，返回false表示还要执行系统默认动作，返回true表示拦截系统默认动作
    + back( View v)  触发返回事件的方法，可以调用来打到与返回键一样的效果
    + onResult(Result result) 当视图接收到返回时回调
3. 集成RecycleView
    + 增加布局属性；`myAdapter` 和 `list`，分别指向一个MyRecycleAdapter对象，和一个List对象，实现自动适配，自动更新
    + 提供万能Adapter：MyRecycleAdapter(ViewModel vm, int layout_id, int br_id,int br_vm_id)
    + 其中参数：（vm:该list所在的父视图绑定的ViewModel，layout_id：该list的每一项对应的layout，br_id:每一项视图中与List中的对象对应的变量，br_vm_id：每一项视图中指向父ViewModel的变量）
4. 优化TextView属性
    + android:text 属性支持int数值
    + 增加 MyBold属性，true表示粗体，false表示正常
4. 屏幕适配：提供dp_xxx和sp_xxx的dimen
5. 动画：提供基础的进入退出动画
6. Drawable：按钮
7. 全局（Rxzmvvm类）静态方法：
    + toastShow(String msg)  提示字符串
    + getContext() 获取应用上下文