package com.bysj.eyeapp.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.bysj.eyeapp.service.PersonService;
import com.bysj.eyeapp.util.CustomToast;
import com.bysj.eyeapp.util.RegularUtil;
import com.bysj.eyeapp.vo.PersonTrainVO;
import com.bysj.eyeapp.vo.UserVO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/1/10.
 */

public class PersonTrain extends BaseActivity{
    private ListView trainList;
    private ListAdapter listViewAdaptor;
    private List<Map<String,Object>> trains;
    private PersonService service;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personcentre_train);
        init();
    }

    private void init(){
        service = new PersonService(null);
        trains = new ArrayList<>();
        List<PersonTrainVO> trainVOs = service.getTrainList();
        for(PersonTrainVO personTrainVO :trainVOs ){
            HashMap<String,Object> map1 = new HashMap<>();
            String dateData = personTrainVO.getTrainDate();
            String date = dateData + "的测试记录";
            map1.put("trainDate",date);
            map1.put("trainId",personTrainVO.getTrainId());
            map1.put("trainType",personTrainVO.getType());
            trains.add(map1);
        }
        initTrainListBarList();
    }

    private void initTrainListBarList(){
        trainList = findViewById(R.id.person_center_train_list);
        //获取一个View原型
        initTrainList();
        //初始化底部加载中的提示栏
        //initLoadBar();
        //设置适配器
        listViewAdaptor = new SimpleAdapter(getApplicationContext(),trains,R.layout.view_person_main_bar_trainlist,
                new String[]{"trainDate","trainId","trainType"},
                new int[]{R.id.train_date,R.id.train_id,R.id.train_type});
        trainList.setAdapter(listViewAdaptor);

    }

    private void initTrainList(){

    }

}
