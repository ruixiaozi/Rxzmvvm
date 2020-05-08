package com.gzf01.rxzmvvm.view.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * Title: MyRecycleAdapter 类 <br/>
 * Description: 基础RecycAdapter <br/>
 * CreateTime: 2020/5/7 <br/>
 *
 * @author ruixiaozi
 * @version 0.0.1
 * @since 0.0.1
 */
public class MyRecycleAdapter extends RecyclerView.Adapter<MyRecycleAdapter.MyHolder> {
    private List<?> data;
    private int layout_id;
    private int br_id,br_vm_id;
    private ViewModel vm;

    public MyRecycleAdapter(ViewModel vm, int layout_id, int br_id,int br_vm_id) {
        this.layout_id = layout_id;
        this.br_id = br_id;
        this.vm = vm;
        this.br_vm_id = br_vm_id;
    }

    public List<?> getData() {
        return data;
    }

    public void bindList(List<?> data){
       this.data = data;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int layout_id) {
        LayoutInflater from = LayoutInflater.from(parent.getContext());
        //设置当前item的布局，并返回它的binding对象
        ViewDataBinding binding = DataBindingUtil.inflate(from, layout_id, parent, false);
        //返回一个Holder
        return new MyHolder(binding);
    }


    /**
     * 可以通过这个实现每个项目不同的布局，目前这个使用相同的
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        return layout_id;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.binding.setVariable(br_id,data.get(position));
        holder.binding.setVariable(br_vm_id,vm);
    }

    @Override
    public int getItemCount() {
        if (data == null)
            return 0;
        return data.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder{
        ViewDataBinding binding;

        public MyHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}