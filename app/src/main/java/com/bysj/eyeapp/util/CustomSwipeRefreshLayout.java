package com.bysj.eyeapp.util;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bysj.eyeapp.view.R;


/**
 * Created by lcplcp on 2018/1/8.
 */

public class CustomSwipeRefreshLayout extends SwipeRefreshLayout {
    private static final String TAG = CustomSwipeRefreshLayout.class.getSimpleName();
    private int mScaledTouchSlop;
    private View mFooterView;
    private ListView mListView;
    private OnLoadMoreListener mListener;

    /**
     * 正在加载状态
     */
    private boolean isLoading;
    private RecyclerView mRecyclerView;
    private int mItemCount;
    private Context context;

    public CustomSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        // 表示控件移动的最小距离，手移动的距离大于这个距离才能拖动控件
        mScaledTouchSlop = 300;//设置默认值为300
    }

    //@Override
//    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
//        super.onLayout(changed, left, top, right, bottom);
//        // 获取ListView,设置ListView的布局位置
////        if (mListView == null || mRecyclerView == null) {
////            // 判断容器有多少个孩子
////            if (getChildCount() > 0) {
////                // 判断第一个孩子是不是ListView
////                if (getChildAt(0) instanceof ListView) {
////                    // 创建ListView对象
////                    mListView = (ListView) getChildAt(0);
////
////                    // 设置ListView的滑动监听
////                    setListViewOnScroll();
////                } else if (getChildAt(0) instanceof RecyclerView) {
////                    // 创建ListView对象
////                    mRecyclerView = (RecyclerView) getChildAt(0);
////
////                    // 设置RecyclerView的滑动监听
////                    setRecyclerViewOnScroll();
////                }
////            }
////        }
//    }


    /**
     * 在分发事件的时候处理子控件的触摸事件
     */
    private float mDownY, mUpY;
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 移动的起点
                mDownY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                // 移动过程中判断时候能下拉加载更多
                if (canLoadMore()) {
                    // 加载数据
                    Log.d(TAG, "------->  加载更多数据");
                    loadData();
                }

                break;
            case MotionEvent.ACTION_UP:
                // 移动的终点
                mUpY = ev.getY();
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 判断是否满足加载更多条件
     */
    private boolean canLoadMore() {
        // 1. 是上拉状态
        float dinstance = mDownY - mUpY;
        boolean condition1 = dinstance >= mScaledTouchSlop;
        if (condition1) {
            Log.d(TAG, "------->  是上拉状态");
        }

        // 2. 当前页面可见的item是最后一个条目,一般最后一个条目位置需要大于第一页的数据长度
        boolean condition2 = false;
        if (mListView != null && mListView.getAdapter() != null) {
            if (mItemCount > 0) {
                int adaptorCount = mListView.getAdapter().getCount();
                if (adaptorCount < mItemCount) {
                    // 第一页未满，禁止下拉
                    condition2 = false;
                }else {
                    int vPostion = mListView.getLastVisiblePosition();
                    int cItem = mListView.getAdapter().getCount();
                    condition2 = (vPostion >= cItem - 1);
                }
            } else {
                // 未设置数据长度，则默认第一页数据不满时也可以上拉
                condition2 = mListView.getLastVisiblePosition() == (mListView.getAdapter().getCount() - 1);
            }

        }

        if (condition2) {
            Log.d(TAG, "------->  是最后一个条目");
        }
        // 3. 正在加载状态
        boolean condition3 = !isLoading;
        if (condition3) {
            Log.d(TAG, "------->  不是正在加载状态");
        }
        return condition1 && condition2 && condition3;
    }

    public void setItemCount(int itemCount) {
        this.mItemCount = itemCount;
    }

    /**
     * 处理加载数据的逻辑
     */
    private void loadData() {
        //System.out.println("加载数据...");
        if (mListener != null) {
            // 设置加载状态，让布局显示出来
            setLoading(true);
            mListener.onLoadMore();
            setLoading(false);
        }

    }

    /**
     * 设置加载状态，是否加载传入boolean值进行判断
     *
     * @param loading
     */
    public void setLoading(boolean loading) {
        // 修改当前的状态
        isLoading = loading;
        if (isLoading) {
            // 显示布局
            //mListView.addFooterView(mFooterView);
            this.mFooterView.setVisibility(VISIBLE);
        } else {
            // 隐藏布局
            //mListView.removeFooterView(mFooterView);
            this.mFooterView.setVisibility(GONE);
            // 重置滑动的坐标
            mDownY = 0;
            mUpY = 0;
        }
    }


    /**
     * 设置ListView的滑动监听
     */
    private void setListViewOnScroll() {

        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // 移动过程中判断时候能下拉加载更多
                if (canLoadMore()) {
                    // 加载数据
                    loadData();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }


    /**
     * 设置RecyclerView的滑动监听
     */
    private void setRecyclerViewOnScroll() {
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                // 移动过程中判断时候能下拉加载更多
                if (canLoadMore()) {
                    // 加载数据
                    loadData();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }


    /**
     * 上拉加载的接口回调
     */

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        this.mListener = listener;
    }

    /**
     * 设置加载中的底部提示栏
     */
    public View initLoadBar(){
        LinearLayout loadBar = new LinearLayout(context);
        loadBar.setMinimumHeight(60);
        loadBar.setGravity(Gravity.CENTER);
        loadBar.setOrientation(LinearLayout.HORIZONTAL);
        ViewGroup.LayoutParams mProgressBarLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        ViewGroup.LayoutParams mTipContentLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        //向"加载项"布局中添加一个圆型进度条
        ProgressBar mProgressBar = new ProgressBar(context);
        mProgressBar.setPadding(0, 0, 15, 0);
        loadBar.addView(mProgressBar, mProgressBarLayoutParams);
        //向"加载项"布局中添加提示信息
        TextView mTipContent = new TextView(context);
        mTipContent.setText(context.getResources().getString(R.string.global_loading));
        mTipContent.setTextColor(context.getResources().getColor(R.color.global_label));
        loadBar.addView(mTipContent);
        //包裹一层FrameLayout，解决footerView不能正常隐藏、显示的问题
//        FrameLayout loadBarHolder = new FrameLayout(getContext());
//        loadBarHolder.addView(loadBar, 0, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
//                FrameLayout.LayoutParams.WRAP_CONTENT));
        //loadBar.setBackgroundColor(Color.YELLOW);
        return loadBar;
    }

    /**
     * 设置下拉多长距离加载更多数据
     * @param dinstance
     * @return
     */
    public void setUpPullDistanceRefresh(int dinstance){
        mScaledTouchSlop = dinstance;
    }

    public void setmListView(ListView listView){
        // 填充底部加载布局
        mFooterView = initLoadBar();
        mListView = listView;
        mListView.addFooterView(mFooterView);
        //默认隐藏底部
       mFooterView.setVisibility(GONE);
    }
}
