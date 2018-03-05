package com.bysj.eyeapp.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.bysj.eyeapp.util.GlobalApplication;
import com.bysj.eyeapp.util.chat.adapter.FaceAdapter;
import com.bysj.eyeapp.util.chat.adapter.FacePageAdeapter;
import com.bysj.eyeapp.util.chat.adapter.MessageAdapter;
import com.bysj.eyeapp.util.chat.util.SharePreferenceUtil;
import com.bysj.eyeapp.util.chat.view.CirclePageIndicator;
import com.bysj.eyeapp.util.chat.view.JazzyViewPager;
import com.bysj.eyeapp.util.chat.xlistview.MsgListView;

public class ChatActivity extends Activity
		implements OnClickListener,OnTouchListener,MsgListView.IXListViewListener {

	private static final int EXPERT_HEAD_ICON = 0;//专家头像的id,对应GlobalApplication类的头像数组的游标
	private static final int USER_HEAD_ICON = 1;//用户头像的id

	private Button sendBtn;
	private ImageButton faceBtn;
	private LinearLayout faceLinearLayout;
	private EditText msgEt;
	private InputMethodManager mInputMethodManager;
	private MessageAdapter mMessageAdapter;
	private JazzyViewPager faceViewPager;
	private MsgListView mMsgListView;
	private GlobalApplication mApplication;
	private SharePreferenceUtil mSpUtil;
	private WindowManager.LayoutParams mLayoutParams;
	private List<String> mListFaceKeys;
	private int currentPage = 0;
	private boolean isFaceShow = false;
	private static int MsgPagerNum;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat_main);

		initData();
		initUI();
		initFacePage();
	}

	private void initData() {
		mApplication = GlobalApplication.getInstance();
		//SharePreference存储类
		mSpUtil = new SharePreferenceUtil(this, "message_save");
		//初始化消息列表适配器
		mMessageAdapter = new MessageAdapter(this, initMsgData());

		//加载表情的列表
		Set<String> keySet = GlobalApplication.getInstance().getFaceMap().keySet();
		mListFaceKeys = new ArrayList<String>();
		mListFaceKeys.addAll(keySet);
		MsgPagerNum = 0;
	}
	private void initUI() {
		mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		//获取窗口触摸操作
		mLayoutParams = getWindow().getAttributes();

		mMsgListView = (MsgListView) findViewById(R.id.msg_listView);
		// 触摸ListView隐藏表情和输入法
		mMsgListView.setOnTouchListener(this);
		mMsgListView.setPullLoadEnable(false);
		mMsgListView.setXListViewListener(this);
		mMsgListView.setAdapter(mMessageAdapter);
		mMsgListView.setSelection(mMessageAdapter.getCount() - 1);

		sendBtn = (Button) findViewById(R.id.send_btn);
		faceBtn = (ImageButton) findViewById(R.id.face_btn);
		faceLinearLayout = (LinearLayout) findViewById(R.id.face_ll);
		msgEt = (EditText) findViewById(R.id.msg_et);
		faceLinearLayout = (LinearLayout) findViewById(R.id.face_ll);
		faceViewPager = (JazzyViewPager) findViewById(R.id.face_pager);

		//标题栏控件
		TextView mTitle = (TextView) findViewById(R.id.ivTitleName);
		mTitle.setText("砖家A");
		TextView mTitleLeftBtn = (TextView) findViewById(R.id.ivTitleBtnLeft);
		mTitleLeftBtn.setVisibility(View.VISIBLE);
		mTitleLeftBtn.setOnClickListener(this);

		//输入框的触摸监听的绑定
		msgEt.setOnTouchListener(this);
		msgEt.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					if (mLayoutParams.softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
							|| isFaceShow) {
						faceLinearLayout.setVisibility(View.GONE);
						isFaceShow = false;
						// imm.showSoftInput(msgEt, 0);
						return true;
					}
				}
				return false;
			}
		});

		//输入框的实时输入长度的监听
		msgEt.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
									  int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
										  int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (s.length() > 0) {
					sendBtn.setEnabled(true);
				} else {
					sendBtn.setEnabled(false);
				}
			}
		});

		faceBtn.setOnClickListener(this);
		sendBtn.setOnClickListener(this);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (v.getId()) {
			case R.id.msg_listView:   //ListView触摸实现
				mInputMethodManager.hideSoftInputFromWindow(msgEt.getWindowToken(), 0);
				faceLinearLayout.setVisibility(View.GONE);
				isFaceShow = false;
				break;
			case R.id.msg_et:    //输入M框触摸实现
				mInputMethodManager.showSoftInput(msgEt, 0);
				faceLinearLayout.setVisibility(View.GONE);
				isFaceShow = false;
				break;

			default:
				break;
		}
		return false;
	}

	//历史数据，在开始时显示
	private List<ChatMessageItem> initMsgData() {
		List<ChatMessageItem> msgList = new ArrayList<ChatMessageItem>();// 消息对象数组

		ChatMessageItem item1 = new ChatMessageItem(ChatMessageItem.MESSAGE_TYPE_TEXT,
				mSpUtil.getNick(), System.currentTimeMillis(), "你好啊哈哈哈",
				USER_HEAD_ICON, false, 0);
		ChatMessageItem item2 = new ChatMessageItem(ChatMessageItem.MESSAGE_TYPE_TEXT,
				mSpUtil.getNick(), System.currentTimeMillis(), "你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好你好",
				EXPERT_HEAD_ICON, true, 0);
		msgList.add(item1);
		msgList.add(item2);
		return msgList;

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.face_btn:  //弹出表情
				if (!isFaceShow) {
					mInputMethodManager.hideSoftInputFromWindow(msgEt.getWindowToken(), 0);
					try {
						Thread.sleep(80);// 解决此时会黑一下屏幕的问题
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					faceLinearLayout.setVisibility(View.VISIBLE);
					isFaceShow = true;
				} else {
					faceLinearLayout.setVisibility(View.GONE);
					isFaceShow = false;
				}
				break;
			case R.id.send_btn:// 发送消息
				String msg = msgEt.getText().toString();
				ChatMessageItem item = new ChatMessageItem(ChatMessageItem.MESSAGE_TYPE_TEXT,
						mSpUtil.getNick(), System.currentTimeMillis(), msg,
						mSpUtil.getHeadIcon(), false, 0);
				mMessageAdapter.upDateMsg(item);

				mMsgListView.setSelection(mMessageAdapter.getCount() - 1);
				msgEt.setText("");
				break;
			case R.id.ivTitleBtnLeft:
				finish();
				break;
//		case R.id.ivTitleBtnRigh:
//			break;
			default:
				break;
		}

	}

	//加载表情，以及设置翻页效果
	private void initFacePage() {
		List<View> lv = new ArrayList<View>();
		for (int i = 0; i < GlobalApplication.NUM_PAGE; ++i)
			lv.add(getGridView(i));
		FacePageAdeapter adapter = new FacePageAdeapter(lv, faceViewPager);
		faceViewPager.setAdapter(adapter);
		faceViewPager.setCurrentItem(currentPage);
		faceViewPager.setTransitionEffect(mEffects[mSpUtil.getFaceEffect()]);
		CirclePageIndicator indicator = (CirclePageIndicator) findViewById(R.id.indicator);
		indicator.setViewPager(faceViewPager);
		adapter.notifyDataSetChanged();
		faceLinearLayout.setVisibility(View.GONE);
		indicator.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				currentPage = arg0;
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// do nothing
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// do nothing
			}
		});

	}

	//表情表格控件放置，设置背景
	private GridView getGridView(int i) {
		GridView gv = new GridView(this);
		gv.setNumColumns(7);      //一行显示7个表情
		gv.setSelector(new ColorDrawable(Color.TRANSPARENT));// 屏蔽GridView默认点击效果
		gv.setBackgroundColor(Color.TRANSPARENT);
		gv.setCacheColorHint(Color.TRANSPARENT);
		gv.setHorizontalSpacing(1);
		gv.setVerticalSpacing(1);
		gv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
		gv.setGravity(Gravity.CENTER);
		gv.setAdapter(new FaceAdapter(this, i));
		gv.setOnTouchListener(forbidenScroll());    	// 防止乱pageview乱滚动
		gv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
									long arg3) {
				if (arg2 == GlobalApplication.NUM) {   // 删除表情键的位置
					int selection = msgEt.getSelectionStart();
					String text = msgEt.getText().toString();
					if (selection > 0) {
						String text2 = text.substring(selection - 1);
						if ("]".equals(text2)) {
							int start = text.lastIndexOf("[");
							int end = selection;
							msgEt.getText().delete(start, end);
							return;
						}
						msgEt.getText().delete(selection - 1, selection);
					}
				} else {
					int count = currentPage * GlobalApplication.NUM + arg2;
					// 注释的部分，在EditText中显示字符串
					// String ori = msgEt.getText().toString();
					// int index = msgEt.getSelectionStart();
					// StringBuilder stringBuilder = new StringBuilder(ori);
					// stringBuilder.insert(index, keys.get(count));
					// msgEt.setText(stringBuilder.toString());
					// msgEt.setSelection(index + keys.get(count).length());

					// 下面这部分，在EditText中显示表情
					Bitmap bitmap = BitmapFactory.decodeResource(
							getResources(), (Integer) GlobalApplication
									.getInstance().getFaceMap().values()
									.toArray()[count]);
					if (bitmap != null) {
						int rawHeigh = bitmap.getHeight();
						int rawWidth = bitmap.getHeight();
						int newHeight = 40;
						int newWidth = 40;
						// 计算缩放因子
						float heightScale = ((float) newHeight) / rawHeigh;
						float widthScale = ((float) newWidth) / rawWidth;
						// 新建立矩阵
						Matrix matrix = new Matrix();
						matrix.postScale(heightScale, widthScale);
						// 设置图片的旋转角度
						// matrix.postRotate(-30);
						// 设置图片的倾斜
						// matrix.postSkew(0.1f, 0.1f);
						// 将图片大小压缩
						// 压缩后图片的宽和高以及kB大小均会变化
						Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0,
								rawWidth, rawHeigh, matrix, true);
						ImageSpan imageSpan = new ImageSpan(ChatActivity.this,
								newBitmap);
						String emojiStr = mListFaceKeys.get(count);
						SpannableString spannableString = new SpannableString(
								emojiStr);
						spannableString.setSpan(imageSpan,
								emojiStr.indexOf('['),
								emojiStr.indexOf(']') + 1,
								Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
						msgEt.append(spannableString);
					} else {
						String ori = msgEt.getText().toString();
						int index = msgEt.getSelectionStart();
						StringBuilder stringBuilder = new StringBuilder(ori);
						stringBuilder.insert(index, mListFaceKeys.get(count));
						msgEt.setText(stringBuilder.toString());
						msgEt.setSelection(index + mListFaceKeys.get(count).length());
					}
				}
			}
		});
		return gv;
	}

	// 防止乱pageview乱滚动
	private OnTouchListener forbidenScroll() {
		return new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_MOVE) {
					return true;
				}
				return false;
			}
		};
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		mInputMethodManager.hideSoftInputFromWindow(msgEt.getWindowToken(), 0);
		faceLinearLayout.setVisibility(View.GONE);
		isFaceShow = false;
		super.onPause();
	}


	//处理下拉刷新的效果
	@Override
	public void onRefresh() {
		MsgPagerNum++;
		List<ChatMessageItem> msgList = initMsgData();
		int position = mMessageAdapter.getCount();
		mMessageAdapter.setMessageList(msgList);
		mMsgListView.stopRefresh();

		mMsgListView.setSelection(mMessageAdapter.getCount() - position - 1);
		Log.i("Show","MsgPagerNum = " + mMessageAdapter + ", adapter.getCount() = "
				+ mMessageAdapter.getCount());

	}
	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub

	}

	private JazzyViewPager.TransitionEffect mEffects[] = { JazzyViewPager.TransitionEffect.Standard,
			JazzyViewPager.TransitionEffect.Tablet, JazzyViewPager.TransitionEffect.CubeIn,
			JazzyViewPager.TransitionEffect.CubeOut, JazzyViewPager.TransitionEffect.FlipVertical,
			JazzyViewPager.TransitionEffect.FlipHorizontal, JazzyViewPager.TransitionEffect.Stack,
			JazzyViewPager.TransitionEffect.ZoomIn, JazzyViewPager.TransitionEffect.ZoomOut,
			JazzyViewPager.TransitionEffect.RotateUp, JazzyViewPager.TransitionEffect.RotateDown,
			JazzyViewPager.TransitionEffect.Accordion, };// 表情翻页效果

}
