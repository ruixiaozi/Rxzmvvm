package com.gzf01.rxzmvvm.vm;

import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModel;

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
                System.out.println(2);
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




    public <K> K get(Class<K> clazz){
        if(binding==null)
            return null;
        try {
            for(Method m:binding.getClass().getMethods()){
                if (m.getReturnType().equals(clazz)){
                    return (K)m.invoke(binding);
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public<K> void set(K k){
        if (k == null)
            return;
        try {
            for(Method m:binding.getClass().getMethods()){
                Class[] paramTypes = m.getParameterTypes();
                if (paramTypes.length==1 && paramTypes[0].equals(k.getClass())){

                    m.invoke(binding,k);
                    return;
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Title: onShow 方法 <br />
     * Description: 当界面显示的时候
     *
     * @return void
     */
    public abstract void onShow();


}
