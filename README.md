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
        implementation 'com.github.ruixiaozi:Rxzmvvm:0.0.1'
}
```

3.初始化：在Application的OnCreate()中调用：

```
Rxzmvvm.init(getApplicationContext());
```