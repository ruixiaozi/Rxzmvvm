package com.gzf01.rxzmvvm.view;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.reflect.TypeToken;
import com.gzf01.rxzmvvm.entity.Request;
import com.gzf01.rxzmvvm.entity.Result;
import com.gzf01.rxzmvvm.global.Rxzmvvm;
import com.gzf01.rxzmvvm.vm.BaseViewModel;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Title: BaseFragmentView 类 <br/>
 * Description: Fragment基类 <br/>
 * CreateTime: 2020/5/13 <br/>
 *
 * @author ruixiaozi
 * @version 0.0.1
 * @since 0.0.1
 */
public class BaseFragmentView<T extends BaseViewModel,V extends ViewDataBinding> extends Fragment implements IView {
    //ViewModelProvider的工厂类
    protected ViewModelProvider.Factory factory;
    protected T viewModel;
    protected V binding;


    /**
     * Title: init 方法 <br />
     * Description: 初始化fragment的方法
     *
     * @return View
     */
    protected View init(@NonNull LayoutInflater inflater,@Nullable ViewGroup container,int layout_id) {

        //获取工厂类
        factory = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication());

        //获取ViewModel
        Class <T> entityClass = (Class <T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        viewModel = new ViewModelProvider(this,factory).get(entityClass);  // (T) factory.create(entityClass);

        //获取数据绑定对象
        binding = DataBindingUtil.inflate(inflater, layout_id, container, false);;

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

        if(viewModel!=null ) {
            viewModel.onInit(null);
        }

        return binding.getRoot();

    }

    /**
     * Title: turnTo 方法 <br />
     * Description: 跳转页面方法
     *
     * @return void
     */
    public <K> void turnTo(Class<K> kClass, Request request, boolean isNeedReturn){
        Intent intent = new Intent(this.getActivity(),kClass);
        if(request!=null) {
            intent.putExtra("code",request.getCode());
            String str = Rxzmvvm.gson.toJson(request.getData());
            intent.putExtra("data",str);
        }

        if(isNeedReturn)
            this.startActivityForResult(intent,0);
        else{
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            this.startActivity(intent);
            this.getActivity().finish();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
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
    public void onResume() {
        super.onResume();
        if(viewModel!=null)
            viewModel.onShow();
    }
}
