package com.gzf01.rxzmvvm.global;

import android.app.Activity;
import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.gzf01.rxzmvvm.view.adapter.MyRecycleAdapter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Title: Rxzmvvm 类 <br/>
 * Description: 全局类 <br/>
 * CreateTime: 2020/5/7 <br/>
 *
 * @author ruixiaozi
 * @version 0.0.1
 * @since 0.0.1
 */
public class Rxzmvvm {
    private static Context context;
    public static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").serializeNulls().create();

    //消息窗口对象
    private static Toast toast;
    /**
     * Title: init 方法 <br />
     * Description: 初始化方法
     *
     * @return void
     */
    public static void init(Context context){
        Rxzmvvm.context = context;
    }




    public static Context getContext(){
        return context;
    }

    /**
     * Title: toastShow 方法 <br />
     * Description: 优化Toast显示
     *
     * @return   void
     */
    public static void toastShow(String msg){
        if(toast == null){
            toast = Toast.makeText(context,msg,Toast.LENGTH_SHORT);
        }
        else{
            toast.setText(msg);
        }

        toast.show();
    }

    /**
     * Title: setTextByInt 方法 <br />
     * Description:
     *
     * @return void
     */
    @BindingAdapter(value = {"android:text"})
    public static void setTextByInt(TextView tv, int data){
        tv.setText(Integer.toString(data));
    }

    /**
     * Title: setRecycleAdapter 方法 <br />
     * Description: 自动刷新RecycleAdapter
     *
     * @return
     */
    @BindingAdapter(value = {"myAdapter","list"},requireAll = true)
    public static void setRecycleAdapter(RecyclerView rv, MyRecycleAdapter adapter, List list){
        if(adapter==null)
            return;
        //绑定主视图
        List<?> list1 = adapter.getData();
        if(list1==null){
            adapter.bindList(list);
            LinearLayoutManager manager = new LinearLayoutManager(getContext());
            rv.setLayoutManager(manager);
            rv.setAdapter(adapter);
        }
        else if(list1 != list){
            list1.clear();
            list1.addAll(list);
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * Title: setBold 方法 <br />
     * Description: 设置粗体
     *
     * @return void
     */
    @BindingAdapter(value = {"MyBold"})
    public static void setBold(TextView tv, boolean isBold){
        tv.getPaint().setFakeBoldText(isBold);
    }
}
