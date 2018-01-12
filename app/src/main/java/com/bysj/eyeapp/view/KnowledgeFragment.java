package com.bysj.eyeapp.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bysj.eyeapp.exception.HttpException;
import com.bysj.eyeapp.service.KnowledgeService;
import com.bysj.eyeapp.util.CustomSwipeRefreshLayout;
import com.bysj.eyeapp.util.CustomToast;
import com.bysj.eyeapp.util.JavaBeanUtil;
import com.bysj.eyeapp.util.RegularUtil;
import com.bysj.eyeapp.vo.KnowledePaperVO;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KnowledgeFragment extends Fragment {
	//常量定义处
	private static final int PAGE_LIMIT = 10;//默认每次加载数据的数目，这里为10
	public static final int PAPER_TYPE_BLOG = R.id.radio_knowledge_blog;//文章类型：博客
	public static final int PAPER_TYPE_EATINGHABIT = R.id.radio_knowledge_eathabit;//文章类型：饮食习惯
	public static final int PAPER_TYPE_LECTURE = R.id.radio_knowledge_lecture;//文章类型：护眼讲座
	public static final int PAPER_TYPE_DEFAULT = -1;//默认文章类型为推荐文章，用-1代表
	private static final String VIEW_PAPER_ID_KEY = "文章id";
	private static final String REMIND_NOMORE_DATA = "没有更多数据啦！";
	//组件相关变量
	private View thisView;
	private ListView papersList;//文章列表对应的区域view对象
	private List<TextView> papersTitle;//文章标题
	private LinearLayout loadBar;//文章加载中的显示底部
	private RadioGroup menu;
	private TextView paperListTopTitle;
	private CustomSwipeRefreshLayout swipeRefreshLayout;


	//数据相关变量
	private int nowPage = 1;//当前文章页数，从1开始
	private List<Map<String,Object>> papers;
	private String listViewStatus;//listView的状态：加载中，加载完成
	private KnowledgeService service;
	private SimpleAdapter listViewAdaptor;
	private int paperType = PAPER_TYPE_DEFAULT; //当前选择的文章类型，默认-1，代表当前的文章显示的是推荐文章
	private int recommendPaperMaxPage = -1;//推荐文章最大页数
	private int blogPaperMaxPage = -1;//博客最大页数
	private int eatdietPaperMaxPages = -1;//饮食习惯最大页数存储
	private int lecturePaperMaxPage = -1;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view= inflater.inflate(R.layout.fragment_knowledge, null);
		thisView = view;
		init();
		return view;
	}

	/**
	 * 界面初始化方法
	 */
	private void init(){
		//初始化服务层类
		service = new KnowledgeService();
		//初始化功能栏区域
		initMenu();

		paperListTopTitle = thisView.findViewById(R.id.knowledge_main_bar_top_title);
		//tv = (TextView)findViewById(R.id.textView1);
		swipeRefreshLayout = (CustomSwipeRefreshLayout)thisView.findViewById(R.id.eyedata_bar_refresh);
		swipeRefreshLayout.setmListView((ListView) thisView.findViewById(R.id.knowledge_main_bar_list));
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
				loadMorePaper();
			}
		});

		//初始化文章列表区域
		initPapersBarList();

	}

	/**
	 * 初始化文章列表对应的Listview对象
	 */
	//相关常量
	private void initPapersBarList(){
		papersList = thisView.findViewById(R.id.knowledge_main_bar_list);
		//获取一个View原型
		View viewPaper = thisView.findViewById(R.id.knowledge_main_bar);
		//初始化推荐文章数据
		initPapers();
		//初始化底部加载中的提示栏
		//initLoadBar();
		//设置适配器
		listViewAdaptor = new SimpleAdapter(getActivity(),papers,R.layout.view_knowledge_main_bar_paperslist,
				new String[]{"title","sign","type","viewCount","date","id"},
				new int[]{R.id.knowledge_main_bar_papertitle,R.id.knowledge_main_bar_papersign,R.id.knowledge_main_bar_papertype,
				R.id.knowledge_main_bar_viewcount,R.id.knowledge_main_bar_date,R.id.knowledge_main_bar_date_paperid});
		papersList.setAdapter(listViewAdaptor);
		//设置是否显示加载中的状态栏（当滑动到最后一条数据时，显示）
		papersList.setOnScrollListener(new AbsListView.OnScrollListener() {
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
		papersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String paperIdStr = ((TextView)view.findViewById(R.id.knowledge_main_bar_date_paperid)).getText().toString();
				if(!RegularUtil.numberIsTrue(paperIdStr)){
					//Toast toast = Toast.makeText(getActivity(),"当前文章id非法！",Toast.LENGTH_SHORT);
					CustomToast.showToast(getActivity(),"当前文章id非法！");
					return ;
				}

				int paperId = Integer.parseInt(paperIdStr);
				//把paperid传给文章浏览界面
				showViewPaperActivity(paperId);
			}
		});
	}

	/**
	 * 文章列表区域，点击文章标题后，跳转到查看文章的页面
	 * @param paperId
	 */
	public void showViewPaperActivity(int paperId){
		Intent intent = new Intent();
		intent.setClass(thisView.getContext(),KnowledgeViewPaperActivity.class);
		intent.putExtra(VIEW_PAPER_ID_KEY,paperId);
		startActivity(intent);
	}
//
//	/**
//	 * 设置加载中的底部提示栏
//	 */
//	private void initLoadBar(){
//		loadBar = new LinearLayout(getActivity());
//		loadBar.setMinimumHeight(60);
//		loadBar.setGravity(Gravity.CENTER);
//		loadBar.setOrientation(LinearLayout.HORIZONTAL);
//		ViewGroup.LayoutParams mProgressBarLayoutParams = new LinearLayout.LayoutParams(
//				LinearLayout.LayoutParams.WRAP_CONTENT,
//				LinearLayout.LayoutParams.WRAP_CONTENT);
//		ViewGroup.LayoutParams mTipContentLayoutParams = new LinearLayout.LayoutParams(
//				LinearLayout.LayoutParams.WRAP_CONTENT,
//				LinearLayout.LayoutParams.WRAP_CONTENT);
//		//向"加载项"布局中添加一个圆型进度条
//		ProgressBar mProgressBar = new ProgressBar(getActivity());
//		mProgressBar.setPadding(0, 0, 15, 0);
//		loadBar.addView(mProgressBar, mProgressBarLayoutParams);
//		//向"加载项"布局中添加提示信息
//		TextView mTipContent = new TextView(getActivity());
//		mTipContent.setText(getActivity().getResources().getString(R.string.global_loading));
//		mTipContent.setTextColor(getActivity().getResources().getColor(R.color.global_label));
//		papersList.addFooterView(loadBar);
//		View view = new View(getActivity());
//		papersList.addFooterView(new View(getActivity()));//这个View对象仅仅用于吧底部加载条显示出来，无其他作用
//
//		loadBar.addView(mTipContent, mTipContentLayoutParams);
//		//hideLoadBar();
//	}

	/**
	 * 初始化功能栏区域方法
	 */
	private void initMenu(){
		menu = (RadioGroup) thisView.findViewById(R.id.knowledge_menu);
		int checkedId = menu.getCheckedRadioButtonId();
		//设置监听事件
		menu.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if(checkedId==R.id.radio_knowledge_advisory){
					//跳转到提问界面
					menu.clearCheck();
					showAdvisoryActivity();
				}else {
					//改变文章列表区域显示的文章类型
					changePaperType(checkedId);
				}

			}
		});
	}

	/**
	 * 改变文章列表显示的文章类型：该方法在点击界面功能栏按钮时，筛选文章类型时使用
	 * @param checkedId 功能栏中被选中的功能模块，代表某种类型的文章
	 */
	private void changePaperType(int checkedId){
		//showLoadBar();
		//清空paper区域的数据
		clearPaperList();
		//获取新类型文章的数据
		List<KnowledePaperVO> papersNew = null;
		try {
			papersNew = service.getPaperByType(checkedId,nowPage,PAGE_LIMIT);
		} catch (HttpException e) {
			//Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_SHORT).show();
			CustomToast.showToast(getActivity(),e.getMessage());
			//hideLoadBar();
			return ;
		}
		if(papersNew.size()==0){
			//Toast.makeText(getActivity(),REMIND_NOMORE_DATA,Toast.LENGTH_SHORT).show();
			CustomToast.showToast(getActivity(),REMIND_NOMORE_DATA);
			//hideLoadBar();
			return ;
		}
		//对象转换并刷新数据
		papers.addAll(paperListToMapList(papersNew));
		//刷新页面
		listViewAdaptor.notifyDataSetChanged();
		//回到顶部
		papersList.setSelectionAfterHeaderView();
		//改变标题（文章列表区域的标题以及activity的标题）
		if(checkedId==PAPER_TYPE_LECTURE){
			getActivity().setTitle(R.string.knowledge_lecture);
			paperListTopTitle.setText(R.string.knowledge_lecture);
			lecturePaperMaxPage = service.getLecturePaperMaxPage();
		}else if(checkedId==PAPER_TYPE_BLOG){
			getActivity().setTitle(R.string.knowledge_blog);
			paperListTopTitle.setText(R.string.knowledge_blog);
			blogPaperMaxPage = service.getBlogPaperMaxPage();
		}else {
			getActivity().setTitle(R.string.knowledge_eathabit);
			paperListTopTitle.setText(R.string.knowledge_eathabit);
			eatdietPaperMaxPages = service.getEatdietPaperMaxPages();
		}
		//改变文章类型变量的值
		paperType = checkedId;
		//hideLoadBar();
	}

	/**
	 * 显示提问界面
	 */
	private void showAdvisoryActivity(){
		Intent intent = new Intent();
		intent.setClass(thisView.getContext(),KnowledgeAdvisoryActivity.class);
		startActivity(intent);
	}

//	/**
//	 * 显示底部加载中的提示
//	 */
//	private void showLoadBar(){
//		loadBar.setVisibility(View.VISIBLE);
//	}
//
//	/**
//	 * 隐藏底部加载中的提示
//	 */
//	private void hideLoadBar(){
//		loadBar.setVisibility(View.INVISIBLE);
//	}

	/**
	 * 刷新文章列表，该方法在下拉文章时动态加载文章列表
	 */
	private void loadMorePaper(){
		if(isLastPage()){
			CustomToast.showToast(getActivity(),REMIND_NOMORE_DATA);
			return ;
		}
		//showLoadBar();
		//加载数据
		List<KnowledePaperVO> result = null;
		try {
			result = service.getPaperByType(paperType,nowPage,PAGE_LIMIT);
		} catch (HttpException e) {
			//Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_SHORT).show();
			CustomToast.showToast(getActivity(),e.getMessage());
			//hideLoadBar();
			return;
		}
		if(result.size()==0){
			//Toast.makeText(getActivity(),REMIND_NOMORE_DATA,Toast.LENGTH_SHORT).show();
			CustomToast.showToast(getActivity(),REMIND_NOMORE_DATA);
			//hideLoadBar();
			return ;
		}
		papers.addAll(paperListToMapList(result));
		//刷新页面
		listViewAdaptor.notifyDataSetChanged();
		//改变当前页码
		nowPage++;
		//hideLoadBar();
	}

	private void initPapers(){
		//showLoadBar();
		if(papers==null){
			papers = new ArrayList<>();
		}
		List<KnowledePaperVO> result = null;
        try {
            result = service.getPaperByType(paperType,nowPage,PAGE_LIMIT);
            int lastPage = service.getRecommendPaperMaxPage();
            recommendPaperMaxPage = lastPage;
        } catch (HttpException e) {
            //Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_SHORT).show();
			CustomToast.showToast(getActivity(),e.getMessage());
			return ;
        }
        papers.addAll(paperListToMapList(result));
		nowPage++;
		//hideLoadBar();
	}

	/**
	 * 清空文章列表方法：在切换文章类型时用到，为了清晰，抽取出来独立为一个方法
	 */
	private void clearPaperList(){
		//清空papers对象
		if(papers!=null){
			papers.clear();
		}
		//页数重新变为1
		nowPage = 1;
	}


	/**判断当前页码是否是最后一页
	 *
	 * @return 判断结果
	 */
	private boolean isLastPage(){
		switch (paperType){
			case PAPER_TYPE_DEFAULT:
				if(recommendPaperMaxPage!=-1 &&nowPage>recommendPaperMaxPage){
					return true;
				}
			case PAPER_TYPE_BLOG:
				if(blogPaperMaxPage!=-1 && nowPage>blogPaperMaxPage){
					return true;
				}
			case PAPER_TYPE_EATINGHABIT:
				if(eatdietPaperMaxPages!=-1 && nowPage>eatdietPaperMaxPages){
					return true;
				}
			case PAPER_TYPE_LECTURE:
				if(lecturePaperMaxPage!=-1 && nowPage>lecturePaperMaxPage){
					return true;
				}
		}
		return false;
	}


	/**
	 * 工具方法：把文章列表转换为List<Map>的方法
	 */
		private List<Map<String,Object>> paperListToMapList(List<KnowledePaperVO> papers){
			List<Map<String,Object>> rtn = new ArrayList<>();
			for(KnowledePaperVO paper : papers){
				rtn.add(JavaBeanUtil.objToMap(paper));
			}
			return rtn;
		}

	/**
	 * 刷新页面方法
	 */
	private void refresh(){
		switch (paperType) {
			case PAPER_TYPE_DEFAULT :
				//清空数据
				clearPaperList();
				//重新初始化
				initPapers();
				//刷新页面
				listViewAdaptor.notifyDataSetChanged();
				//回到顶部
				papersList.setSelectionAfterHeaderView();
				break;
			case PAPER_TYPE_BLOG :
				changePaperType(PAPER_TYPE_BLOG);
				break;
			case PAPER_TYPE_LECTURE :
				changePaperType(PAPER_TYPE_LECTURE);
				break;
			case PAPER_TYPE_EATINGHABIT :
				changePaperType(PAPER_TYPE_EATINGHABIT);
		}
	}






}
