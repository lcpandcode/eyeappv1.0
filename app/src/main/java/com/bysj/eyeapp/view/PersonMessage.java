package com.bysj.eyeapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.bysj.eyeapp.service.PersonService;
import com.bysj.eyeapp.util.CustomSwipeRefreshLayout;
import com.bysj.eyeapp.util.CustomToast;
import com.bysj.eyeapp.util.GlobalApplication;
import com.bysj.eyeapp.util.GlobalConst;
import com.bysj.eyeapp.util.RegularUtil;
import com.bysj.eyeapp.vo.ExpertCommunicationVO;
import com.bysj.eyeapp.vo.UserVO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/1/10.
 */

public class PersonMessage extends BaseActivity{

    private CustomSwipeRefreshLayout swipeRefreshLayout;

    private PersonService service;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personcentre_message);
        init();
    }

    /**
     * 界面初始化方法
     */
    private void init(){
        //初始化服务层类
        UserVO user = (UserVO) ((GlobalApplication)getApplication()).getGlobalVar(GlobalConst.TAG_USER);
        service = new PersonService(user);
    }



    public void showMsgDetail(View view){
        Intent intent = new Intent(getApplicationContext(),PersonCommunicationNewMessage.class);
        startActivity(intent);
    }

    public void showSysMsgDetail(View view){
        CustomToast.showToast(getApplicationContext(),"该功能待定");
    }

    /**
     * 标题栏的返回按钮
     * @param view
     */
    public void titleReturn(View view){
        finish();
    }
}
