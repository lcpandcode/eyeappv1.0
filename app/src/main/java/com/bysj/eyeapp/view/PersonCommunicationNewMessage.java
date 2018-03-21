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

public class PersonCommunicationNewMessage extends BaseActivity{

    private CustomSwipeRefreshLayout swipeRefreshLayout;
    private ListView expertList;
    private SimpleAdapter listViewAdaptor;

    private PersonService service;
    private List<Map<String,Object>> experts = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personcentre_communication_new_msg);
        init();
    }

    /**
     * 界面初始化方法
     */
    private void init(){
        //初始化服务层类
        UserVO user = (UserVO) ((GlobalApplication)getApplication()).getGlobalVar(GlobalConst.TAG_USER);
        service = new PersonService(user);

        expertList = findViewById(R.id.person_center_expert_list);
        swipeRefreshLayout = (CustomSwipeRefreshLayout)findViewById(R.id.person_certer_bar_refresh);
        swipeRefreshLayout.setmListView((ListView) findViewById(R.id.person_center_expert_list));
        //设置刷新时动画的颜色，可以设置4个
        swipeRefreshLayout.setColorSchemeResources(R.color.global_refresh_loadbar_color1,
                R.color.global_refresh_loadbar_color2,R.color.global_refresh_loadbar_color3);
        //设置下拉刷新事件
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                //tv.setText("正在刷新");
                // TODO Auto-generated method stub
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        //tv.setText("刷新完成");
                        refresh();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 0);
            }
        });
        //设置向上拉时加载更多
        swipeRefreshLayout.setOnLoadMoreListener(new CustomSwipeRefreshLayout.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                loadMoreExpert();
            }
        });
        initExpertData();//初始化数据
        //初始化专家列表区域
        initExpertBarList();

    }

    private void initExpertData(){
        List<ExpertCommunicationVO> expertsData = service.getExpertCommunicationNewMsgList(10,1);
        for(ExpertCommunicationVO expert : expertsData){
            Map<String,Object> expertMap = new HashMap<>();
            expertMap.put("name",expert.getName());
            expertMap.put("date",expert.getDate());
            expertMap.put("message",expert.getMessage());
            expertMap.put("newMsgCount",expert.getUnReadCount());
            expertMap.put("id",expert.getId());
            experts.add(expertMap);
        }
    }

    private void initExpertBarList(){
        //设置适配器
        listViewAdaptor = new SimpleAdapter(getApplication(),experts,R.layout.view_person_center_expert_list,
                new String[]{"name","message","date","newMsgCount","id"},
                new int[]{R.id.person_center_expert_name,R.id.person_center_message,R.id.person_center_date,
                        R.id.person_center_message_center_msg_count,R.id.person_center_expert_id});
        expertList.setAdapter(listViewAdaptor);
        //设置是否显示加载中的状态栏（当滑动到最后一条数据时，显示）
        expertList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //设置itemCount的值
                if(swipeRefreshLayout!=null){
                    Log.d("itemCountSet","------>设置了itemCount");
                    swipeRefreshLayout.setItemCount(totalItemCount);
                }
            }
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }
        });
        //设置item监听事件
        expertList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String expertIdStr = ((TextView)view.findViewById(R.id.person_center_expert_id)).getText().toString();
                if(!RegularUtil.numberIsTrue(expertIdStr)){
                    CustomToast.showToast(getApplication(),"当前id非法！");
                    return ;
                }

                int experId = Integer.parseInt(expertIdStr);
                showExpertCommunicationDetailActivity(experId);
            }
        });
    }

    private void loadMoreExpert(){
        Log.d("debug","test1");
        CustomToast.showToast(getApplication(),"上拉加载更多功能尚未完成");
    }

    private void refresh(){
        CustomToast.showToast(getApplication(),"刷新功能尚未完成");
    }

    private void showExpertCommunicationDetailActivity(int experId){
        Intent intent = new Intent(getApplicationContext(),ChatActivity.class);
        startActivity(intent);
    }

    /**
     * 标题栏的返回按钮
     * @param view
     */
    public void titleReturn(View view){
        finish();
    }


}
