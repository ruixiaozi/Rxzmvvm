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
        implementation 'com.github.ruixiaozi:Rxzmvvm:0.0.3'
}
```

3.初始化：在Application的OnCreate()中调用：

```
Rxzmvvm.init(getApplicationContext());
```

### 功能说明

1. 视图基类：BaseActivityView
2. ViewModel基类：LoginViewModel
3. 集成RecycleView
    + 增加布局属性；`myAdapter` 和 `list`，分别指向一个MyRecycleAdapter对象，和一个List对象，实现自动适配，自动更新
    + 提供万能Adapter：MyRecycleAdapter
4. 屏幕适配：提供dp_xxx和sp_xxx的dimen
5. 动画：提供基础的进入退出动画
6. Drawable：按钮