package com.bysj.eyeapp.view;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bysj.eyeapp.service.KnowledgeService;
import com.bysj.eyeapp.util.JavaBeanUtil;
import com.bysj.eyeapp.vo.KnowledePaperVO;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KnowledgeFragment extends Fragment {
	//常量定义处
	private static final int PAGE_LIMIT = 10;//默认每次加载数据的数目，这里为10
	private static final String STATUS_LOADING = "loading";//页面状态：加载中
	private static final String STATUS_LOAD_FINISH = "loading finish";//页面状态：加载完成
	//组件相关变量
	private View thisView;
	private ListView papersList;//文章列表对应的区域view对象
	private List<TextView> papersTitle;//文章标题
	private LinearLayout loadBar;//文章加载中的显示底部


	//数据相关变量
	private int nowPage = 1;//当前文章页数，从1开始
	private List<Map<String,Object>> papers;
	private String listViewStatus;//listView的状态：加载中，加载完成
	private KnowledgeService service;
	private SimpleAdapter listViewAdaptor;


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
		service = new KnowledgeService();
		initPapersBarList();
	}


	/**
	 * 初始化文章列表对应的文章栏（即这个变量：List<TextView> papers）
	 * 该方法主要用于查找文章列表栏中所有文章标题的Textview对象，并把它放在papers这个列表对象中
	 */
	private void initPapersBar(){
		//获取知识库界面的文章列表对象
		View barList = thisView.findViewById(R.id.knowledge_main_barlist);
		/*抽取文章标题所在的区域的View对象（这里抽取时特别注意：修改布局文件后有可能会影响代码运行，所以对应需要修改这里代码）
			为了需要抽取正确的组件对象，需要注意一下：
			1、避免抽取了横线View  ，所以在选取对象时先判断
		 */

	}

	/**
	 * 初始化文章列表对应的Listview对象
	 */
	private void initPapersBarList(){
		papersList = thisView.findViewById(R.id.knowledge_main_bar_list);
		//初始化数据（这里先写死）
		//获取一个View原型
		View viewPaper = thisView.findViewById(R.id.knowledge_main_bar);
		//初始化推荐文章数据
		initPapers();
		//初始化底部加载中的提示栏
		initLoadBar();
		//设置适配器
		listViewAdaptor = new SimpleAdapter(getActivity(),papers,R.layout.view_knowledge_main_bar_paperslist,
				new String[]{"title","type","readCount"},new int[]{R.id.knowledge_main_bar_papertitle,R.id.knowledge_main_bar_papertype,
				R.id.knowledge_main_bar_readcount});
		papersList.setAdapter(listViewAdaptor);
		//设置是否显示加载中的状态栏（当滑动到最后一条数据时，显示）
		papersList.setOnScrollListener(new AbsListView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				//判断状态
				if(listViewStatus==null || listViewStatus.equals(STATUS_LOADING)){
					return ;
				}
				if(firstVisibleItem + visibleItemCount>=totalItemCount){
					//加载数据，刷新页面
					//showLoadBar();
					refreshPaperList();
					//hideLoadBar();
				}
			}
		});

		listViewStatus = STATUS_LOAD_FINISH;
	}

	/**
	 * 设置加载中的底部提示栏
	 */
	private void initLoadBar(){
		loadBar = new LinearLayout(getActivity());
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
		ProgressBar mProgressBar = new ProgressBar(getActivity());
		mProgressBar.setPadding(0, 0, 15, 0);
		loadBar.addView(mProgressBar, mProgressBarLayoutParams);
		//向"加载项"布局中添加提示信息
		TextView mTipContent = new TextView(getActivity());
		mTipContent.setText(getActivity().getResources().getString(R.string.global_loading));
		mTipContent.setTextColor(getActivity().getResources().getColor(R.color.global_label));
		loadBar.addView(mTipContent, mTipContentLayoutParams);
		papersList.addFooterView(loadBar);
		papersList.addFooterView(new View(getActivity()));//这个View对象仅仅用于吧底部加载条显示出来，无其他作用
	}

	/**
	 * 显示底部加载中的提示
	 */
	private void showLoadBar(){
		papersList.setVisibility(View.VISIBLE);
	}

	/**
	 * 隐藏底部加载中的提示
	 */
	private void hideLoadBar(){
		loadBar.setVisibility(View.INVISIBLE);
	}

	/**
	 * 刷新文章列表
	 */
	private void refreshPaperList(){
		listViewStatus = STATUS_LOADING;
		//加载数据
		papers.addAll(paperListToMapList(service.getRecommendPaper(nowPage,PAGE_LIMIT)));
		//刷新页面
		listViewAdaptor.notifyDataSetChanged();
		//改变当前页码
		nowPage++;
		listViewStatus = STATUS_LOAD_FINISH;
	}

	private void initPapers(){
		listViewStatus = STATUS_LOADING;
		if(papers==null){
			papers = new ArrayList<>();
		}
		papers.addAll(paperListToMapList(service.getRecommendPaper(nowPage,PAGE_LIMIT)));
		nowPage++;
		listViewStatus = STATUS_LOAD_FINISH;
	}

	/**
	 * 工具方法：把文章列表转换为List<Map>的方法
	 */
	private List<Map<String,Object>> paperListToMapList(List<KnowledePaperVO> papers){
		List<Map<String,Object>> rtn = new ArrayList<>();
		for(KnowledePaperVO paper : papers){
			rtn.add(JavaBeanUtil.ObjtoMap(paper));
		}
		return rtn;
	}

}
