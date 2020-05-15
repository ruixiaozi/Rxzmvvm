package com.gzf01.rxzmvvm.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.reflect.TypeToken;
import com.gzf01.rxzmvvm.R;
import com.gzf01.rxzmvvm.entity.Request;
import com.gzf01.rxzmvvm.entity.Result;
import com.gzf01.rxzmvvm.global.Rxzmvvm;
import com.gzf01.rxzmvvm.utils.statusBar.StatusBarUtil;
import com.gzf01.rxzmvvm.vm.BaseViewModel;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Title: BaseActivityView 类 <br/>
 * Description: ActivityView的基础视图类 <br/>
 * CreateTime: 2020/5/7 <br/>
 *
 * @author ruixiaozi
 * @version 0.0.1
 * @since 0.0.1
 */
public abstract class BaseActivityView<T extends BaseViewModel,V extends ViewDataBinding> extends AppCompatActivity implements IView {

    //ViewModelProvider的工厂类
    protected ViewModelProvider.Factory factory;
    protected T viewModel;
    protected V binding;


    protected void init(int layout_id) {

        // 设置右入动画
        overridePendingTransition(R.anim.right_in, R.anim.view_stay);
        //设置状态栏透明
        StatusBarUtil.setTranslucentStatus(this);
        //一般的手机的状态栏文字和图标都是白色的, 可如果你的应用也是纯白色的, 或导致状态栏文字看不清
        //所以如果你是这种情况,请使用以下代码, 设置状态使用深色文字图标风格, 否则你可以选择性注释掉这个if内容
        if (!StatusBarUtil.setStatusBarDarkTheme(this, true)) {
            //如果不支持设置深色风格 为了兼容总不能让状态栏白白的看不清, 于是设置一个状态栏颜色为半透明,
            //这样半透明+白=灰, 状态栏的文字能看得清
            StatusBarUtil.setStatusBarColor(this,0x55000000);
        }
        //获取工厂类
        factory = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication());

        //获取ViewModel
        Class <T> entityClass = (Class <T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        viewModel = new ViewModelProvider(this,factory).get(entityClass);  //(T) factory.create(entityClass);

        //获取数据绑定对象
        binding = DataBindingUtil.setContentView(this,layout_id);

        //View与ViewModel绑定
        for(Method m:this.binding.getClass().getMethods()){
            if(m.getName().matches("^set.+ViewModel$")){
                try {
                    m.invoke(binding,viewModel);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        viewModel.bind(binding, this);

        //初始化
        Intent data = getIntent();
        if(viewModel!=null ){
            if(data!=null && data.hasExtra("code") && data.hasExtra("data"))
            {
                Request request = new Request();
                request.setCode(data.getIntExtra("code",0));
                Map<String,String> map = Rxzmvvm.gson.fromJson(data.getStringExtra("data"),
                        TypeToken.getParameterized(Map.class, String.class, String.class).getType());
                request.setData(map);
                viewModel.onInit(request);
            }
            else
                viewModel.onInit(null);
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        if(viewModel!=null)
            viewModel.onShow();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(viewModel!=null && data!=null && data.hasExtra("code") && data.hasExtra("data")){
            Result result = new Result();
            result.setCode(data.getIntExtra("code",0));
            result.setMessage(data.getStringExtra("msg"));
            Map<String,String> map = Rxzmvvm.gson.fromJson(data.getStringExtra("data"),
                    TypeToken.getParameterized(LinkedHashMap.class, String.class, String.class).getType());
            result.setData(map);
            viewModel.onResult(result);
        }

    }

    @Override
    public void finish() {
        super.finish();
        //设置右出动画
        overridePendingTransition(R.anim.view_stay, R.anim.right_out);
    }


    /**
     * Title: turnTo 方法 <br />
     * Description: 跳转页面方法
     *
     * @return void
     */
    public <K> void turnTo(Activity activity, Class<K> kClass, Request request,boolean isNeedReturn){
        Intent intent = new Intent(activity,kClass);
        if(request!=null) {
            intent.putExtra("code",request.getCode());
            String str = Rxzmvvm.gson.toJson(request.getData());
            intent.putExtra("data",str);
        }

        if(isNeedReturn)
            activity.startActivityForResult(intent,0);
        else{
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(intent);
            finish();
        }
    }

    /**
     * Title: returnBy 方法 <br />
     * Description: 返回方法
     *
     * @return void
     */
    public void returnBy(Result result){
        if(result != null){
            Intent intent = new Intent();
            intent.putExtra("code",result.getCode());
            intent.putExtra("msg",result.getMessage());
            intent.putExtra("data",Rxzmvvm.gson.toJson(result.getData()));
            setResult(RESULT_OK,intent);
        }
        finish();
    }

    @Override
    public void onBackPressed() {
        if(viewModel==null || !viewModel.onBack())
            super.onBackPressed();
    }
}
