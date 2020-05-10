package com.gzf01.rxzmvvm.vm;

import android.content.Intent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModel;

import com.gzf01.rxzmvvm.entity.Request;
import com.gzf01.rxzmvvm.entity.Result;
import com.gzf01.rxzmvvm.view.IView;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Title: BaseViewModel 抽象类 <br/>
 * Description: 基础VM <br/>
 *
 * @version 0.0.1
 * @since 0.0.1
 */
public abstract class BaseViewModel<T extends ViewDataBinding,V extends IView> extends ViewModel {

    protected T binding;
    protected V view;

    @Override
    protected void onCleared() {
        binding = null;
        view = null;
    }

    /**
     * Title: bind 方法 <br />
     * Description: 绑定视图,初始化数据
     *
     * @return void
     */
    public void bind(T binding, V view) {
        this.binding = binding;
        this.view = view;
        if (binding == null)
            return;
        try {
            for(Method m:this.binding.getClass().getMethods()){
                Class[] paramTypes = m.getParameterTypes();
                if(Modifier.isPublic(m.getModifiers()) && paramTypes.length == 1
                        && m.getName().matches("^set.+Init$") ){

                        m.invoke(binding, paramTypes[0].newInstance());//初始化
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }

    }


    /**
     * Title: back 方法 <br />
     * Description: 返回功能
     *
     * @return void
     */
    public void back( View v){
        if(view instanceof AppCompatActivity){
            AppCompatActivity appCompatActivity = (AppCompatActivity)view;
            appCompatActivity.onBackPressed();
        }
    }


    /**
     * Title: onBack 方法 <br />
     * Description: 按下返回,false表示执行系统默认的，true拦截系统默认
     *
     * @return boolean
     */
    public boolean onBack(){
        return false;
    }


    public <K> K getData(String name,Class<K> type){
        if(binding==null || name==null || name.length()<1)
            return null;
        name = Character.toUpperCase(name.charAt(0))+name.substring(1);
        try {
            for(Method m:binding.getClass().getMethods()){
                if (m.getReturnType().equals(type) && m.getName().matches("^get"+name)){
                    return (K)m.invoke(binding);
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public<K> void setData(String name,K k){
        if (k == null || name==null || name.length()<1)
            return;
        name = Character.toUpperCase(name.charAt(0))+name.substring(1);
        try {
            for(Method m:binding.getClass().getMethods()){
                Class[] paramTypes = m.getParameterTypes();
                if (paramTypes.length==1 && paramTypes[0].equals(k.getClass()) && m.getName().matches("^set"+name)){

                    m.invoke(binding,k);
                    return;
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Title: onInit 方法 <br />
     * Description: 初始化
     *
     * @return void
     */
    public void onInit(Request request){

    }


    /**
     * Title: onShow 方法 <br />
     * Description: 当界面显示的时候
     *
     * @return void
     */
    public void onShow(){}


    /**
     * Title: onResult 方法 <br />
     * Description: 当收到返回的时候
     *
     * @return void
     */
    public void onResult(Result result){

    }


}
