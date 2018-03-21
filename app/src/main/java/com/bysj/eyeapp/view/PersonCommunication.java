package com.bysj.eyeapp.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.bysj.eyeapp.exception.HttpException;
import com.bysj.eyeapp.exception.PersonException;
import com.bysj.eyeapp.exception.SystemException;
import com.bysj.eyeapp.exception.UserException;
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

public class PersonCommunication extends BaseActivity{

    private CustomSwipeRefreshLayout swipeRefreshLayout;
    private ListView expertList;
    private MyAdapter listViewAdaptor;

    private PersonService service;
    private List<ExpertCommunicationVO> experts = new ArrayList<>();
    private int pageNow = 1;
    private int limit = 10;
    private boolean isInit = false;
    private boolean isFirstInit = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personcentre_communication);
        init();
    }

    @Override
    public void onStart(){
        super.onStart();
        if(isInit ){
            return ;
        }
        refresh();
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
        boolean isInitData = initExpertData();//初始化数据
        //初始化专家列表区域
        boolean isInitUi = initExpertBarList();
        isInit = isInitData && isInitUi;
        if(isFirstInit) {
            isFirstInit = !isInit;
        }
    }

    private boolean initExpertData(){
        List<ExpertCommunicationVO> expertsData = null;
        try {
            experts= service.getExpertCommunicationList(limit,pageNow);
        } catch (HttpException e) {
            Log.e(GlobalConst.REMIND_NET_ERROR,e.getMessage());
            CustomToast.showToast(getApplication(), GlobalConst.REMIND_NET_ERROR);
            return false;
        }catch (UserException e){
            String msg = e.getMessage();
            if(msg.equals(GlobalConst.REMIND_NOT_LOGIN)){
                //跳转到登录界面
                CustomToast.showToast(getApplication(), GlobalConst.REMIND_NOT_LOGIN );
                goToLogin();
                return false;
            }
            Log.e(GlobalConst.REMIND_NOT_LOGIN,e.getMessage());
            CustomToast.showToast(getApplication(), e.getMessage());
            return false;
        }catch (PersonException e){
            CustomToast.showToast(getApplication(),e.getMessage());
            return false;
        }catch (SystemException e){
            String msg = e.getMessage();
            if(msg.equals(GlobalConst.OUT_OF_PAGE)){
                CustomToast.showToast(getApplication(),"已无更多数据");
                return false;
            }else {
                CustomToast.showToast(getApplication(),e.getMessage());
                return false;
            }
        }
        return true;
    }

    /*
    private boolean initExpertBarList(){
        //设置适配器
        listViewAdaptor = new SimpleAdapter(getApplication(),experts,R.layout.view_person_center_expert_list,
                new String[]{"name","message","date","id","count"},
                new int[]{R.id.person_center_expert_name,R.id.person_center_message,R.id.person_center_date,
                        R.id.person_center_expert_id,R.id.person_center_message_center_msg_count});
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
                //更新消息状态
                int expertId = Integer.parseInt(expertIdStr);
                boolean readResult = readMessage(expertId);
                if(readResult){
                    showExpertCommunicationDetailActivity(expertId);
                } else {
                    CustomToast.showToast(getApplication(),GlobalConst.REMIND_NET_ERROR);
                    return ;
                }
            }
        });
        return true;
    }*/
    private boolean initExpertBarList(){
        //设置适配器
        listViewAdaptor = new MyAdapter(this,experts);
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
                //更新消息状态
                int expertId = Integer.parseInt(expertIdStr);
                boolean readResult = readMessage(expertId,position);
                if(readResult){
                    TextView expertName = (TextView)view.findViewById(R.id.person_center_expert_name);
                    showExpertCommunicationDetailActivity(expertId,expertName.getText().toString());
                } else {
                    CustomToast.showToast(getApplication(),GlobalConst.REMIND_NET_ERROR);
                    return ;
                }
            }
        });
        return true;
    }

    private void loadMoreExpert(){
        Log.d("debug","test1");
        CustomToast.showToast(getApplication(),"上拉加载更多功能尚未完成");
    }

    private void refresh(){
        //刷新数据
        pageNow = 1;
        experts.clear();

        List<ExpertCommunicationVO> expertsData = null;
        try {
            experts = service.getExpertCommunicationList(limit,pageNow);
        } catch (HttpException e) {
            Log.e(GlobalConst.REMIND_NET_ERROR,e.getMessage());
            CustomToast.showToast(getApplication(), GlobalConst.REMIND_NET_ERROR);
            return ;
        }catch (UserException e){
            String msg = e.getMessage();
            Log.e(GlobalConst.REMIND_NOT_LOGIN,e.getMessage());
            return ;
        }catch (PersonException e){
            CustomToast.showToast(getApplication(),e.getMessage());
            return ;
        }catch (SystemException e){
            String msg = e.getMessage();
            if(msg.equals(GlobalConst.OUT_OF_PAGE)){
                CustomToast.showToast(getApplication(),"已无更多数据");
                return ;
            }else {
                CustomToast.showToast(getApplication(),e.getMessage());
                return ;
            }
        }
        //刷新页面
        listViewAdaptor.notifyDataSetChanged();
        //回到顶部
        expertList.setSelectionAfterHeaderView();
        CustomToast.showToast(getApplication(),GlobalConst.REMIND_REFRESH_SUCCESS);
    }

    private void showExpertCommunicationDetailActivity(int expertId,String expertName){
        Intent intent = new Intent(getApplicationContext(),ChatActivity.class);
        intent.putExtra("expertId",expertId);
        intent.putExtra("expertName",expertName);
        startActivity(intent);
    }

    /**
     * 标题栏的返回按钮
     * @param view
     */
    public void titleReturn(View view){
        finish();
    }

    /**
     * 登录跳转，跳转到登录界面
     */

    private void goToLogin(){
        Intent intent = new Intent();
        //设置标志数据，登录完成跳回原来的提问界面
        intent.putExtra(GlobalConst.LOGIN_TO_OTHER_UI_TAG,"PersonCommunication");
        intent.setClass(getApplicationContext(),LoginActivity.class);
        startActivity(intent);
    }

    /**
     * 当点开消息列表时，把所有未读消息变为已读
     * @param expertId 对应的聊天的专家id
     */
    private boolean readMessage(int expertId,int position){
        //判断该item是否有未读消息
        ExpertCommunicationVO data = experts.get(position);
        if(data.getUnReadCount()<=0){
            return true;
        }
        try {
            service.readMessage(expertId);
        } catch (HttpException e) {
            Log.e(GlobalConst.REMIND_NET_ERROR,e.getMessage());
            return false;
        }catch(SystemException e){
            Log.e(GlobalConst.SYSTEM_ERROR,e.getMessage());
            CustomToast.showToast(getApplication(),"系统错误：消息是否已读状态更新失败！");
            return false;
        }catch(UserException e){
            if(GlobalConst.REMIND_NOT_LOGIN.equals(e.getMessage())){
                CustomToast.showToast(getApplication(),GlobalConst.REMIND_NOT_LOGIN);
                goToLogin();
                return false;
            }
        }
        return true;
    }



    static class MyAdapter  extends BaseAdapter{
        private LayoutInflater mInflater;
        private List<ExpertCommunicationVO> mData;
        private ViewHolder holder;
        //MyAdapter需要一个Context，通过Context获得Layout.inflater，然后通过inflater加载item的布局
        public MyAdapter(Context context, List<ExpertCommunicationVO> data) {
            mInflater = LayoutInflater.from(context);
            mData = data;
        }

        //返回数据集的长度
        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Object getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        //这个方法才是重点，我们要为它编写一个ViewHolder
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.view_person_center_expert_list, parent, false); //加载布局
                holder = new ViewHolder();
                holder.name = (TextView) convertView.findViewById(R.id.person_center_expert_name);
                holder.msg = (TextView) convertView.findViewById(R.id.person_center_message);
                holder.date = (TextView) convertView.findViewById(R.id.person_center_date);
                holder.expertId = (TextView) convertView.findViewById(R.id.person_center_expert_id);
                holder.msgCount = (TextView) convertView.findViewById(R.id.person_center_message_center_msg_count);

                convertView.setTag(holder);
            } else {   //else里面说明，convertView已经被复用了，说明convertView中已经设置过tag了，即holder
                holder = (ViewHolder) convertView.getTag();
            }

            ExpertCommunicationVO data = mData.get(position);
            holder.name.setText(data.getName());
            holder.date.setText(data.getDate());
            holder.expertId.setText(data.getId() + "");
            int count = data.getUnReadCount();
            if(count<=0){
                holder.msgCount.setVisibility(View.INVISIBLE);
            }else {
                holder.msgCount.setText(count + "");
            }

            return convertView;
        }
        public ViewHolder getHolder(){
            return holder;
        }

        //这个ViewHolder只能服务于当前这个特定的adapter，因为ViewHolder里会指定item的控件，不同的ListView，item可能不同，所以ViewHolder写成一个私有的类
        private class ViewHolder {
            TextView name;
            TextView msg;
            TextView date;
            TextView expertId;
            TextView msgCount;
        }


    }

}
