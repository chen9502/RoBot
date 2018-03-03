package com.biyesheji.android.robot.utils;

import android.util.SparseArray;
import android.view.View;



public class ViewHolderUtils {

    @SuppressWarnings("unchecked")
    public static View getView(View conterView , int viewId){
        SparseArray<View> array  = (SparseArray) conterView.getTag();
         if(array ==null){
             array = new SparseArray<View>();
         }
        View view = array.get(viewId);
        if(view ==null){
            view  = conterView.findViewById(viewId);
        }
        return view;
    }
}
