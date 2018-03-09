package com.biyesheji.android.robot.fragment;


import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.biyesheji.android.robot.MyApp;
import com.biyesheji.android.robot.R;
import com.biyesheji.android.robot.config.AppInfo;
import com.biyesheji.android.robot.entity.AddButton;
import com.biyesheji.android.robot.socket.GsonList;
import com.biyesheji.android.robot.ui.ActionActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/4 0004.
 */

public class GexinFragment extends Fragment implements View.OnClickListener, View.OnLongClickListener, AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener {
    private Button btn_append,btn_del,btn_mode_gexin;
    private View view;
    private GridView gridView;

    public static List<AddButton> listGridview = new ArrayList();
    private SimpleAdapter simpleAdapter;
    private int parseIntid;
    public static int itemlongclick_currentPosition = 10000;
    private Button btntext;
    private Button btntextSelected;
    private String mingcheng;
    private String bianhao;
    private EditText et_mingcheng;
    private EditText et_bianhao;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.gexin_fragment, null);

        initView();
        setListener();
        if(MyApp.gxdzList!=null){

//            Log.d("wxwx","-------gexinfragment --MyApplication.gxdzList.Size---------------"+MyApplication.gxdzList.toString());
                simpleAdapter = new SimpleAdapter(getContext(), MyApp.gxdzList,R.layout.gridview_add,
                        new String[]{"databaseid", "mingcheng", "bianhao"}, new int[]{R.id.databaseid, R.id.gridViewBtn, R.id.bianhao});
                gridView.setAdapter(simpleAdapter);
        }
        gridView.setOnItemClickListener(this);
        gridView.setOnItemLongClickListener(this);

        IntentFilter intentFilter = new IntentFilter(AppInfo.ACTION_GRIDVIEW);
        getActivity().registerReceiver(broadcastReceiver,intentFilter);
        IntentFilter intentFiltergexin = new IntentFilter(AppInfo.ACTION_ROBOT_OPERATEMODE_CHANGE);
        getActivity().registerReceiver(broadcastReceivergexin,intentFiltergexin);
        if(ActionActivity.robotCurrentOprtionMode ==  ActionActivity.constRobotOptCustomizedMode){
//            btn_mode_gexin.setBackgroundResource(R.drawable.wifiaploginpress);
        }else {
//            btn_mode_gexin.setBackgroundResource(R.drawable.wifiaploginpressfalse);
        }

        return view;
    }
    BroadcastReceiver broadcastReceivergexin = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int robotoptmode = intent.getIntExtra("robotoptmode", 0);
            Log.d("wxwx","-----------SettingFragment  robotoptmode-------------"+robotoptmode);
            if(robotoptmode == ActionActivity.constRobotOptCustomizedMode){
//                btn_mode_gexin.setBackgroundResource(R.drawable.wifiaploginpress);
            }else {
//                btn_mode_gexin.setBackgroundResource(R.drawable.wifiaploginpressfalse);
            }
        }
    };

    private void setListener() {
        btn_append.setOnClickListener(this);
        btn_mode_gexin.setOnClickListener(this);
    }

    private void initView() {
        btn_append = (Button) view.findViewById(R.id.btn_append);
        btn_del = (Button) view.findViewById(R.id.btn_del);
        gridView = (GridView) view.findViewById(R.id.gridView);
        btn_mode_gexin = (Button) view.findViewById(R.id.btn_mode_gexin);


    }
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String gridview = intent.getExtras().getString("gridview");
            Log.d("wxwx","----------GexinFragment broadcastReceiver gridview----------------------"+gridview);
            switch (gridview){
                case "refresh":
                    Log.d("wxwx","----------GexinFragment broadcastReceiver----------------------");
                    gridviewRefresh();
                    break;
            }

        }
    };
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.btn_append:
                if(btntextSelected!=null){
                    btn_del.setEnabled(false);
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                View inflate = LayoutInflater.from(getContext()).inflate(R.layout.append_dialog, null);
                et_mingcheng = (EditText) inflate.findViewById(R.id.et_mingcheng);
                et_bianhao = (EditText) inflate.findViewById(R.id.et_bianhao);

                builder.setTitle("添加一条指令");
                builder.setView(inflate);
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mingcheng = et_mingcheng.getText().toString();
                        bianhao = et_bianhao.getText().toString();
                        if(TextUtils.isEmpty(mingcheng) || TextUtils.isEmpty(bianhao)){
                            Toast.makeText(getContext(), "指令或编号不能为空", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(Integer.parseInt(bianhao) >= 255){
                            Toast.makeText(getContext(), "编号不能大于或等于255", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        GsonList.gsonList("gexindongzuoanniupeizhi",255, mingcheng, bianhao,"add");
                       // dialog.dismiss();
                    }
                });
                builder.create().show();

                break;
            case R.id.btn_del:
                new AlertDialog.Builder(getContext()).setTitle("确定删除这条指令吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                GsonList.gsonList("gexindongzuoanniupeizhi",parseIntid,"default","default","delete");
                                btn_del.setEnabled(false);
                                btn_del.setBackgroundResource(R.drawable.delshape);
                                btn_append.setEnabled(true);
                                btn_append.setBackgroundResource(R.drawable.btnpresshuang);
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                btntextSelected.setBackgroundResource(R.drawable.btnpress);
                                Log.d("wxwx","----------btntext-----------------"+btntext.toString());
                                btn_del.setEnabled(false);
                                btn_del.setBackgroundResource(R.drawable.delshape);
                                btn_append.setEnabled(true);
                                btn_append.setBackgroundResource(R.drawable.btnpresshuang);
                                int childCount = gridView.getChildCount();
                                Log.d("wxwx","--------gridview getChildCount-----------"+childCount);
                                for (int j = 0; j < childCount; j++) {
                                    btntext = (Button) gridView.getChildAt(j).findViewById(R.id.gridViewBtn);
                                    btntext.setEnabled(true);
                                    btntext.setBackgroundResource(R.drawable.btnpress);
                                }

                            }
                        })
                        .create().show();
                break;
            case R.id.btn_mode_gexin:
                GsonList.gsonList("actioncontrol",255,"default","setting","mode_gexin");
              //  ActionActivity.robotCurrentOprtionMode = ActionActivity.constRobotOptCustomizedMode;
                if(ActionActivity.robotCurrentOprtionMode == 1){

                }else {
//                    btn_mode_gexin.setBackgroundResource(R.drawable.wifiaploginpressfalse);
                }

                break;

        }
    }

    public void gridviewRefresh(){
        Log.d("wxwx", "---------gridviewRefresh:listGridview.size() ------------------"+listGridview.size());
       // if(listGridview.size()!=0) {
        MyApp.gxdzList.clear();
            Object[] objects = listGridview.toArray();

            Log.d("wxwx", "---------Object[]length ------------------" + objects.length);
            for (int i = 0; i < objects.length; i++) {
                MyApp.gxdzMap = new HashMap<String, Object>();
                MyApp.gxdzMap.put("databaseid", ((AddButton) objects[i]).getId());
                MyApp.gxdzMap.put("mingcheng", ((AddButton) objects[i]).getMingcheng());
                MyApp.gxdzMap.put("bianhao", ((AddButton) objects[i]).getBianhao());
                Log.d("wxwx", "==========listGridview.objects mingcheng========" + ((AddButton) objects[i]).getMingcheng());

                Log.d("wxwx", "==========listGridview.toArray()========" + listGridview.toArray());

                Log.d("wxwx", "==========listGridview.objects ========" + objects[i].toString());

                Log.d("wxwx", "==========listGridview.ActionActivity.gxdzMap ========" + MyApp.gxdzMap.toString());

                MyApp.gxdzList.add(MyApp.gxdzMap);
            }
           // Log.d("wxwx","--gridviewRefresh-----MyApplication.gxdzList-------"+MyApplication.gxdzList.get(0));
            simpleAdapter = new SimpleAdapter(MyApp.getApp().getApplicationContext(), MyApp.gxdzList, R.layout.gridview_add,
                    new String[]{"databaseid", "mingcheng", "bianhao"}, new int[]{R.id.databaseid, R.id.gridViewBtn, R.id.bianhao});
            gridView.setAdapter(simpleAdapter);
       // }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    //gridview的点击事件
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Log.d("wxwx","---------position----------"+position);
        Log.d("wxwx","---------id----------"+id);
        Map<String,Object> map = (Map<String, Object>) adapterView.getItemAtPosition(position);
        int  databaseid = (int) map.get("databaseid");
        String mingcheng = (String) map.get("mingcheng");
        String bianhao = (String) map.get("bianhao");
        Log.d("wxwx","-----gridview点击----databaseid----------"+databaseid);
        Log.d("wxwx","-----gridview点击----mingcheng----------"+mingcheng);
        Log.d("wxwx","-----gridview点击----bianhao----------"+bianhao);

        btntext = (Button) gridView.getChildAt(position).findViewById(R.id.gridViewBtn);
        if(itemlongclick_currentPosition == position){
            btntext.setBackgroundResource(R.drawable.btnpress);
            btn_del.setEnabled(false);
            btn_del.setBackgroundResource(R.drawable.delshape);
            btn_append.setEnabled(true);
            btn_append.setBackgroundResource(R.drawable.btnpresshuang);
            itemlongclick_currentPosition = 1000000;

            int childCount = gridView.getChildCount();
            Log.d("wxwx","--------gridview getChildCount-----------"+childCount);

            for (int i = 0; i < childCount; i++) {
                btntext = (Button) gridView.getChildAt(i).findViewById(R.id.gridViewBtn);
                btntext.setEnabled(true);
                btntext.setBackgroundResource(R.drawable.btnpress);
            }
        }else {
            if(btntext.isEnabled()){
                Log.d("wxwx","------------个性按钮发送---------------------");
                GsonList.gsonList("actioncontrol", Integer.parseInt(bianhao),"default","gexin","default");
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(broadcastReceiver);
        getActivity().unregisterReceiver(broadcastReceivergexin);
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
        Log.d("wxwx","--------gexinFragment position----------------"+position);
        Log.d("wxwx","--------gexinFragment id----------------"+id);

        itemlongclick_currentPosition = position;
        TextView textdatabaseid = (TextView) gridView.getChildAt(itemlongclick_currentPosition).findViewById(R.id.databaseid);
        btntextSelected = (Button) gridView.getChildAt(itemlongclick_currentPosition).findViewById(R.id.gridViewBtn);
       // btntextSelected.setBackgroundResource(R.drawable.gridviewpress);
        btntextSelected.setBackgroundResource(R.drawable.btnpress1);
        String strdatabaseid = textdatabaseid.getText().toString();
        parseIntid = Integer.parseInt(strdatabaseid);
        Log.d("wxwx","------parseIntid--------------"+strdatabaseid);

        int childCount = gridView.getChildCount();
        Log.d("wxwx","--------gridview getChildCount-----------"+childCount);

        for (int i = 0; i < childCount; i++) {
            btntext = (Button) gridView.getChildAt(i).findViewById(R.id.gridViewBtn);
            if(i == itemlongclick_currentPosition){
               // btntext.setBackgroundResource(R.drawable.gridviewpress);
                btntext.setBackgroundResource(R.drawable.btnpress1);
            }else {
              //  btntext.setBackgroundResource(R.drawable.btndisable);
//                btntext.setBackgroundResource(R.drawable.wifiaplogin1);
                btntext.setEnabled(false);
            }
        }
        btn_append.setEnabled(false);
        btn_append.setBackgroundResource(R.drawable.delshape);
        btn_del.setEnabled(true);
        btn_del.setTextColor(Color.WHITE);
        btn_del.setBackgroundResource(R.drawable.btnpresshuang);
        btn_del.setOnClickListener(this);  //添加事件
        return true;
    }
}
