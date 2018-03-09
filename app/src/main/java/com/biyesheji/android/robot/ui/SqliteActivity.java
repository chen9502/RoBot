package com.biyesheji.android.robot.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.biyesheji.android.robot.R;
import com.biyesheji.android.robot.config.AppInfo;
import com.biyesheji.android.robot.socket.ConnectToServer;
import com.biyesheji.android.robot.socket.GsonList;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SqliteActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private List<Map<String,Object>> listmap = null;
    private ListView listView;
    private ImageView image_back;
    private Button btn_update,btn_add,btn_delete;
    private String json;
    private SimpleAdapter simpleAdapter;
    private String ask;
    private String answer;
    private int _id;
    private Intent intent;

    private final int reqestCodeAdd = 1;
    private final int reqestCodeDelete = 2;
    private final int reqestCodeUpdate = 3;
    private List<Map<String,Object>> listadd;
    private List<Map<String,Object>> listdelete;
    private List<Map<String,Object>> listupdate;
    private int listPosition;
    private Map<String, Object> map1;
    private Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlite);

        initView();
        GsonList.gsonList("database",1,"0x22","AAAA","6");

        image_back.setOnClickListener(this);
        btn_add.setOnClickListener(this);
        btn_delete.setOnClickListener(this);
        listView.setOnItemClickListener(this); //listview点击 事件

        IntentFilter filter = new IntentFilter(AppInfo.ACTION_DATABASE);
        registerReceiver(broadcastReceiver,filter);

        simpleAdapter = new SimpleAdapter(SqliteActivity.this,listmap, R.layout.sqliteadapter,
                new String[]{"para2","para3","para1","para4"},new int[]{R.id.textAsk,R.id.textAnswer,R.id.text_id,R.id.text_type});
        listView.setAdapter(simpleAdapter);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }



    private void initView() {
        listView = (ListView) findViewById(R.id.listviewId);
        image_back = (ImageView) findViewById(R.id.image_back);
        btn_update = (Button) findViewById(R.id.btn_update);
        btn_add = (Button) findViewById(R.id.btn_add);
        btn_delete = (Button) findViewById(R.id.btn_delete);

        listmap = new ArrayList<>();
        listadd = new ArrayList<>();
        listdelete = new ArrayList<>();
        listupdate = new ArrayList<>();
    }


    @Override
    public void onClick(View v) {
       /* int id = v.getId();
        switch (id){
            case R.id.image_back:
                finish();
                break;
            case R.id.btn_update: //修改一条数据
                intent = new Intent(this,UpdateActivity.class);
                intent.putExtra("para2",ask);
                intent.putExtra("para3", answer);
                intent.putExtra("para1",_id);
                startActivityForResult(intent,reqestCodeUpdate);
                break;
            case R.id.btn_add: //添加一条数据
                intent = new Intent(this,AddActivity.class);
                startActivityForResult(intent,reqestCodeAdd);
                break;
            case R.id.btn_delete: //删除一条数据
                intent = new Intent(this,DeleteActivity.class);
                intent.putExtra("para2",ask);
                intent.putExtra("para3", answer);
                intent.putExtra("para1",_id);
                startActivityForResult(intent,reqestCodeDelete);
                break;
        }*/
    }
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            int id = intent.getExtras().getInt("para1");
            String ask = intent.getExtras().getString("para2");
            String answer = intent.getExtras().getString("para3");
            String opttype = intent.getExtras().getString("para4");
            String optresult= intent.getExtras().getString("para5");

            Log.d("wxwx","-----id------------"+ id);
            Log.d("wxwx","-----ask------------"+ ask);
            Log.d("wxwx","-----answer------------"+ answer);
            Log.d("wxwx","-----opttype------------"+ opttype);
            Log.d("wxwx","-----optresult------------"+ optresult);

            switch (opttype){
                case "1"://查询
                  //  listmap.clear();
                    map1 = new HashMap<>();
                    map1.put("para1",id);
                    map1.put("para2",ask);
                    map1.put("para3",answer);
                    map1.put("para4",opttype);
                    map1.put("para5",optresult);
                    listmap.add(map1);
                    simpleAdapter.notifyDataSetChanged();
                    break;
                case "2": //修改
                    if(optresult.equals("1")){
                        listmap.get(listPosition).put("para2",ask);
                        listmap.get(listPosition).put("para3",answer);
                    }else {
                        Toast.makeText(context, "修改失败", Toast.LENGTH_SHORT).show();
                    }

                    simpleAdapter.notifyDataSetChanged();
                    break;
                case "3":
                    if(optresult.equals("1")){
                        listmap.remove(listPosition);
                        simpleAdapter.notifyDataSetChanged();
                        Toast.makeText(getApplicationContext(), "你删除了第"+listPosition+"条记录", Toast.LENGTH_SHORT).show();

                    }else {
                        Toast.makeText(context, "删除失败", Toast.LENGTH_SHORT).show();
                    }

                    break;
                case "4"://添加
                    //listmap.clear();
                    map1 = new HashMap<>();
                    map1.put("para1",id);
                    map1.put("para2",ask);
                    map1.put("para3",answer);
                    map1.put("para4",opttype);
                    map1.put("para5",optresult);
                    listmap.add(0,map1);
                    simpleAdapter.notifyDataSetChanged();
                    break;
            }

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case reqestCodeAdd: //添加
                switch (resultCode){
                    case RESULT_OK:
                        String database = data.getStringExtra("type");
                        int _id = data.getIntExtra("para1", 1000);
                        String ask = data.getStringExtra("para2");
                        String answer = data.getStringExtra("para3");
                        String operatorType = data.getStringExtra("para4");
                        Log.d("wxwx","------添加数据---------"+operatorType);


                        Map<String,Object> map1 = new HashMap<String, Object>();
                        map1.put("type",database);
                        map1.put("para1",_id);
                        map1.put("para2",ask);
                        map1.put("para3",answer);
                        map1.put("para4",operatorType);
                        listadd.add(map1);
                        Log.d("wxwx","--------listadd-----"+listadd.toString());
                        Gson gson = new Gson();
                        String add = gson.toJson(listadd);
                        Log.d("wxwx","------add------"+add);

                      //  MyApplication.mChatService.sendBTMessage(add);
                      //  GsonList.gsonList(database,_id,ask,answer,operatorType);
                        ConnectToServer.socketClient.sendMsg(add);
                        Toast.makeText(getApplicationContext(), "添加一条数据", Toast.LENGTH_SHORT).show();


                        break;
                }
                break;
            case reqestCodeDelete:  //删除
                switch (resultCode){
                    case RESULT_OK:
                        String database = data.getStringExtra("type");
                        int _id = data.getIntExtra("para1", 0);
                        String ask = data.getStringExtra("para2");
                        String answer = data.getStringExtra("para3");
                        String operatorType = data.getStringExtra("para4");

                        Map<String,Object> map2 = new HashMap<String, Object>();
                        map2.put("type",database);
                        map2.put("para1",_id);
                        map2.put("para2",ask);
                        map2.put("para3",answer);
                        map2.put("para4",operatorType);
                        listdelete.add(map2);
                        Log.d("wxwx","--------listdelete-----"+listdelete.toString());
                        Gson gson = new Gson();
                        String delete = gson.toJson(listdelete);
                        Log.d("wxwx","------add------"+delete);
                        listdelete.clear();

                        ConnectToServer.socketClient.sendMsg(delete);
                       // MyApplication.mChatService.sendBTMessage(delete);
                        simpleAdapter.notifyDataSetChanged(); //listvie发生改变
                        break;
                }
                break;
            case reqestCodeUpdate:
                switch (resultCode){
                    case RESULT_OK:
                        String database = data.getStringExtra("type");
                        int _id = data.getIntExtra("para1", 0);
                        String ask = data.getStringExtra("para2");
                        String answer = data.getStringExtra("para3");
                        String operatorType = data.getStringExtra("para4");

                        Map<String,Object> map3 = new HashMap<String, Object>();
                        map3.put("type",database);
                        map3.put("para1",_id);
                        map3.put("para2",ask);
                        map3.put("para3",answer);
                        map3.put("para4",operatorType);
                        listupdate.add(map3);
                        Log.d("wxwx","--------listupdate-----"+listupdate.toString());
                        Gson gson = new Gson();
                        String update = gson.toJson(listupdate);
                        Log.d("wxwx","------update------"+update);

                      //  MyApplication.mChatService.sendBTMessage(update);
                        ConnectToServer.socketClient.sendMsg(update);
                        break;
                }
                break;
        }
    }

    //listview的点击事件
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        listPosition = position;
        HashMap<String,Object> map = (HashMap<String, Object>) adapterView.getItemAtPosition(listPosition);
        ask = (String) map.get("para2");
        answer = (String) map.get("para3");
        _id = (int) map.get("para1");
        Toast.makeText(SqliteActivity.this, "你选择了第"+ _id +"条", Toast.LENGTH_SHORT).show();

        //设置可点击
        btn_update.setEnabled(true);
        btn_delete.setEnabled(true);

        btn_update.setOnClickListener(this);
        btn_delete.setOnClickListener(this);

    }
}
