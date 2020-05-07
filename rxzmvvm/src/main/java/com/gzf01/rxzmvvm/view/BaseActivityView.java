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
    //消息窗口对象
    protected Toast toast;
    //全局上下文
    protected Context context;

    protected T viewModel;

    protected V binding;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置右入动画
        overridePendingTransition(R.anim.right_in, R.anim.view_stay);
        //获取工厂类
        factory = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication());
        //获取全局上下文
        context = Rxzmvvm.getContext();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent data = getIntent();
        if(viewModel!=null && data!=null && data.hasExtra("request")){
            viewModel.onInit((Request) data.getSerializableExtra("request"));
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
    public <K> void turnTo(Activity activity, Class<K> kClass, Request request){
        Intent intent = new Intent(activity,kClass);
        if(request!=null){
            intent.putExtra("request",request);
        }
        activity.startActivityForResult(intent,0);
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

    /**
     * Title: toastShow 方法 <br />
     * Description: 优化Toast显示
     *
     * @return   void
     */
    @Override
    public void toastShow(String msg){
        if(toast == null){
            toast = Toast.makeText(context,msg,Toast.LENGTH_SHORT);
        }
        else{
            toast.setText(msg);
        }
        toast.show();
    }
}
