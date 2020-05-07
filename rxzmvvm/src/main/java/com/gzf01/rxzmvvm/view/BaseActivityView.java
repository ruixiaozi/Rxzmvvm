package com.gzf01.rxzmvvm.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModelProvider;

import com.gzf01.rxzmvvm.R;
import com.gzf01.rxzmvvm.entity.Request;
import com.gzf01.rxzmvvm.entity.Result;
import com.gzf01.rxzmvvm.global.Rxzmvvm;
import com.gzf01.rxzmvvm.utils.statusBar.StatusBarUtil;
import com.gzf01.rxzmvvm.vm.BaseViewModel;

import java.io.Serializable;

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


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置右入动画
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

    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent data = getIntent();
        if(viewModel!=null ){
            if(data!=null && data.hasExtra("request"))
                viewModel.onInit((Request) data.getSerializableExtra("request"));
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
        if(viewModel!=null && data!=null && data.hasExtra("result")){
            viewModel.onResult((Result) data.getSerializableExtra("result"));
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
            intent.putExtra("request", request);
        }

        if(isNeedReturn)
            activity.startActivityForResult(intent,0);
        else{
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
            intent.putExtra("result",result);
            setResult(RESULT_OK,intent);
        }
        finish();
    }


}
