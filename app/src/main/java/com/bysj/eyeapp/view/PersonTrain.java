package com.bysj.eyeapp.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.bysj.eyeapp.exception.HttpException;
import com.bysj.eyeapp.service.PersonService;
import com.bysj.eyeapp.service.TestService;
import com.bysj.eyeapp.util.CustomToast;
import com.bysj.eyeapp.util.GlobalConst;
import com.bysj.eyeapp.util.RegularUtil;
import com.bysj.eyeapp.vo.PersonTrainVO;
import com.bysj.eyeapp.vo.TestResultVO;
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
    private TestService service;
    private List<TestResultVO> resultVOS = new ArrayList<>();
    private TextView rtn ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personcentre_train);
        init();
    }


    private void init(){
        rtn = findViewById(R.id.button_backward_test_list);
        rtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        service = new TestService();
        List<PersonTrainVO> trainVOs = null;
        trains = new ArrayList<>();
        try {
            resultVOS = service.getTestResult(1,10);
        } catch (HttpException e) {
            e.printStackTrace();
            CustomToast.showToast(getApplicationContext(), GlobalConst.REMIND_NET_ERROR);

        }
        for(TestResultVO vo :resultVOS ){
            HashMap<String,Object> map1 = new HashMap<>();
            String dateData = vo.getDate();
            String date = dateData + "的测试记录";
            map1.put("trainDate",date);
            map1.put("trainId",vo.getId());
            map1.put("trainType",vo.getType());
            map1.put("trainEye",vo.getEye());
            map1.put("trainResult",vo.getTestResult());
            map1.put("trainCorrect",vo.getCorrectRate());
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
                new String[]{"trainDate","trainId","trainType","trainCorrect","trainResult","trainEye"},
                new int[]{R.id.train_date,R.id.train_id,R.id.train_type,R.id.train_correctRate,R.id.train_result,R.id.train_eye});
        trainList.setAdapter(listViewAdaptor);

    }

    private void initTrainList(){

    }

}
