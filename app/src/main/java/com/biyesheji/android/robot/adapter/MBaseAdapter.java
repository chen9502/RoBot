package com.biyesheji.android.robot.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2018/2/27.
 */

public abstract class MBaseAdapter<T> extends BaseAdapter {
    protected LayoutInflater inflater ;
    private List<T> datas;

    public MBaseAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
        datas = new ArrayList<T>();
    }

    protected LayoutInflater getInflater() {
        return inflater;
    }

    public void addAll(List<T> dd){
        datas.addAll(dd);
        notifyDataSetChanged();

    }
    public void clear(){
        datas.clear();
        notifyDataSetChanged();
    }
    public void add(T t){
        datas.add(t);
        notifyDataSetChanged();
    }
    public void add(int index,T t){
        datas.add(index,t);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public T getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}
