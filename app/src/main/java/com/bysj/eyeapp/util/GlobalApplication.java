package com.bysj.eyeapp.util;

/**
 * Created by lcplcp on 2018/1/10.
 */

import android.app.Application;
import android.app.NotificationManager;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bysj.eyeapp.util.chat.util.SharePreferenceUtil;
import com.bysj.eyeapp.view.R;
import com.bysj.eyeapp.view.TestAstigmatismResultViewActivity;
import com.bysj.eyeapp.view.TestColorbindResultActivity;
import com.bysj.eyeapp.view.TestColorbindResultActivityView;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 全局Application，用于存储全局性的信息：包括用户本身的相关的信息
 */
public class GlobalApplication extends Application {
    //全局map容器
    Map<String,Object> globalMap ;

    public GlobalApplication(){
        super();
        globalMap = new HashMap<>();
    }

    public void putGlobalVar (String tag,Object var){
        globalMap.put(tag,var);
    }

    public void removeGlobalVar(String tag){
        globalMap.remove(tag);
    }
    public Object getGlobalVar(String tag){
        return globalMap.get(tag);
    }

    /**********************聊天界面相关***************************************************/
    public static final String SP_FILE_NAME = "msg_sp";
    public static final int[] heads = { R.mipmap.expert_img, R.mipmap.person_center_communication_myself_head_img};
    public static final int NUM_PAGE = 6;// 总共有多少页
    public static int NUM = 20;// 每页20个表情,还有最后一个删除button
    private static GlobalApplication mApplication;
    private Map<String, Integer> mFaceMap = new LinkedHashMap<String, Integer>();
    private SharePreferenceUtil mSpUtil;
    private NotificationManager mNotificationManager;

    public synchronized static GlobalApplication getInstance() {
        return mApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
        initFaceMap();
        initData();
    }

    private void initData() {
        mNotificationManager = (NotificationManager) getSystemService(android.content.Context.NOTIFICATION_SERVICE);
    }

    public NotificationManager getNotificationManager() {
        if (mNotificationManager == null)
            mNotificationManager = (NotificationManager) getSystemService(android.content.Context.NOTIFICATION_SERVICE);
        return mNotificationManager;
    }

    public synchronized SharePreferenceUtil getSpUtil() {
        if (mSpUtil == null)
            mSpUtil = new SharePreferenceUtil(this, SP_FILE_NAME);
        return mSpUtil;
    }

    public Map<String, Integer> getFaceMap() {
        if (!mFaceMap.isEmpty())
            return mFaceMap;
        return null;
    }


    private void initFaceMap() {
        // TODO Auto-generated method stub
        mFaceMap.put("[呲牙]", R.mipmap.f000);
        mFaceMap.put("[调皮]", R.mipmap.f001);
        mFaceMap.put("[流汗]", R.mipmap.f002);
        mFaceMap.put("[偷笑]", R.mipmap.f003);
        mFaceMap.put("[再见]", R.mipmap.f004);
        mFaceMap.put("[敲打]", R.mipmap.f005);
        mFaceMap.put("[擦汗]", R.mipmap.f006);
        mFaceMap.put("[猪头]", R.mipmap.f007);
        mFaceMap.put("[玫瑰]", R.mipmap.f008);
        mFaceMap.put("[流泪]", R.mipmap.f009);
        mFaceMap.put("[大哭]", R.mipmap.f010);
        mFaceMap.put("[嘘]", R.mipmap.f011);
        mFaceMap.put("[酷]", R.mipmap.f012);
        mFaceMap.put("[抓狂]", R.mipmap.f013);
        mFaceMap.put("[委屈]", R.mipmap.f014);
        mFaceMap.put("[便便]", R.mipmap.f015);
        mFaceMap.put("[炸弹]", R.mipmap.f016);
        mFaceMap.put("[菜刀]", R.mipmap.f017);
        mFaceMap.put("[可爱]", R.mipmap.f018);
        mFaceMap.put("[色]", R.mipmap.f019);
        mFaceMap.put("[害羞]", R.mipmap.f020);

        mFaceMap.put("[得意]", R.mipmap.f021);
        mFaceMap.put("[吐]", R.mipmap.f022);
        mFaceMap.put("[微笑]", R.mipmap.f023);
        mFaceMap.put("[发怒]", R.mipmap.f024);
        mFaceMap.put("[尴尬]", R.mipmap.f025);
        mFaceMap.put("[惊恐]", R.mipmap.f026);
        mFaceMap.put("[冷汗]", R.mipmap.f027);
        mFaceMap.put("[爱心]", R.mipmap.f028);
        mFaceMap.put("[示爱]", R.mipmap.f029);
        mFaceMap.put("[白眼]", R.mipmap.f030);
        mFaceMap.put("[傲慢]", R.mipmap.f031);
        mFaceMap.put("[难过]", R.mipmap.f032);
        mFaceMap.put("[惊讶]", R.mipmap.f033);
        mFaceMap.put("[疑问]", R.mipmap.f034);
        mFaceMap.put("[睡]", R.mipmap.f035);
        mFaceMap.put("[亲亲]", R.mipmap.f036);
        mFaceMap.put("[憨笑]", R.mipmap.f037);
        mFaceMap.put("[爱情]", R.mipmap.f038);
        mFaceMap.put("[衰]", R.mipmap.f039);
        mFaceMap.put("[撇嘴]", R.mipmap.f040);
        mFaceMap.put("[阴险]", R.mipmap.f041);

        mFaceMap.put("[奋斗]", R.mipmap.f042);
        mFaceMap.put("[发呆]", R.mipmap.f043);
        mFaceMap.put("[右哼哼]", R.mipmap.f044);
        mFaceMap.put("[拥抱]", R.mipmap.f045);
        mFaceMap.put("[坏笑]", R.mipmap.f046);
        mFaceMap.put("[飞吻]", R.mipmap.f047);
        mFaceMap.put("[鄙视]", R.mipmap.f048);
        mFaceMap.put("[晕]", R.mipmap.f049);
        mFaceMap.put("[大兵]", R.mipmap.f050);
        mFaceMap.put("[可怜]", R.mipmap.f051);
        mFaceMap.put("[强]", R.mipmap.f052);
        mFaceMap.put("[弱]", R.mipmap.f053);
        mFaceMap.put("[握手]", R.mipmap.f054);
        mFaceMap.put("[胜利]", R.mipmap.f055);
        mFaceMap.put("[抱拳]", R.mipmap.f056);
        mFaceMap.put("[凋谢]", R.mipmap.f057);
        mFaceMap.put("[饭]", R.mipmap.f058);
        mFaceMap.put("[蛋糕]", R.mipmap.f059);
        mFaceMap.put("[西瓜]", R.mipmap.f060);
        mFaceMap.put("[啤酒]", R.mipmap.f061);
        mFaceMap.put("[飘虫]", R.mipmap.f062);

        mFaceMap.put("[勾引]", R.mipmap.f063);
        mFaceMap.put("[OK]", R.mipmap.f064);
        mFaceMap.put("[爱你]", R.mipmap.f065);
        mFaceMap.put("[咖啡]", R.mipmap.f066);
        mFaceMap.put("[钱]", R.mipmap.f067);
        mFaceMap.put("[月亮]", R.mipmap.f068);
        mFaceMap.put("[美女]", R.mipmap.f069);
        mFaceMap.put("[刀]", R.mipmap.f070);
        mFaceMap.put("[发抖]", R.mipmap.f071);
        mFaceMap.put("[差劲]", R.mipmap.f072);
        mFaceMap.put("[拳头]", R.mipmap.f073);
        mFaceMap.put("[心碎]", R.mipmap.f074);
        mFaceMap.put("[太阳]", R.mipmap.f075);
        mFaceMap.put("[礼物]", R.mipmap.f076);
        mFaceMap.put("[足球]", R.mipmap.f077);
        mFaceMap.put("[闪电]", R.mipmap.f080);
        mFaceMap.put("[饥饿]", R.mipmap.f081);
        mFaceMap.put("[困]", R.mipmap.f082);
        mFaceMap.put("[咒骂]", R.mipmap.f083);

        mFaceMap.put("[折磨]", R.mipmap.f084);
        mFaceMap.put("[抠鼻]", R.mipmap.f085);
        mFaceMap.put("[鼓掌]", R.mipmap.f086);
        mFaceMap.put("[糗大了]", R.mipmap.f087);
        mFaceMap.put("[左哼哼]", R.mipmap.f088);
        mFaceMap.put("[哈欠]", R.mipmap.f089);
        mFaceMap.put("[快哭了]", R.mipmap.f090);
        mFaceMap.put("[吓]", R.mipmap.f091);
        mFaceMap.put("[篮球]", R.mipmap.f092);
        mFaceMap.put("[乒乓球]", R.mipmap.f093);
        mFaceMap.put("[NO]", R.mipmap.f094);
        mFaceMap.put("[跳跳]", R.mipmap.f095);
        mFaceMap.put("[怄火]", R.mipmap.f096);
        mFaceMap.put("[转圈]", R.mipmap.f097);
        mFaceMap.put("[磕头]", R.mipmap.f098);
        mFaceMap.put("[回头]", R.mipmap.f099);
        mFaceMap.put("[跳绳]", R.mipmap.f100);
        mFaceMap.put("[激动]", R.mipmap.f101);
        mFaceMap.put("[街舞]", R.mipmap.f102);
        mFaceMap.put("[献吻]", R.mipmap.f103);
        mFaceMap.put("[左太极]", R.mipmap.f104);

        mFaceMap.put("[右太极]", R.mipmap.f105);
        mFaceMap.put("[闭嘴]", R.mipmap.f106);
    }


    /**
     * 为了方便，这里是处理测试记录跳转的方法，后面可能需要重构
     * @param view
     */
    public void viewTrainDetail(View view){
        LinearLayout linearLayout = (LinearLayout)view.getParent();
        TextView textViewId = (TextView)linearLayout.getChildAt(1);
        int id = Integer.parseInt(textViewId.getText().toString());
        String type = ((TextView)linearLayout.getChildAt(2)).getText().toString();
        TextView textViewResult = (TextView)linearLayout.getChildAt(3);
        TextView textViewCorrectRate = (TextView)linearLayout.getChildAt(4);
        TextView textViewEye = (TextView)linearLayout.getChildAt(5);

        String result = textViewResult.getText().toString();
        String eye = textViewEye.getText().toString();
        String correctRate = textViewCorrectRate.getText().toString();
        if("色盲".equals(type)){
            Intent intent = new Intent();
            intent.putExtra("id",id);
            intent.putExtra("result",result);
            intent.putExtra("eye",eye);
            intent.putExtra("correctRate",correctRate);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
            intent.setClass(this, TestColorbindResultActivityView.class);
            startActivity(intent);
        } else if("视力".equals(type)){
            Intent intent = new Intent();
            intent.putExtra("id",id);
            intent.putExtra("result",result);
            intent.putExtra("eye",eye);
            intent.putExtra("correctRate",correctRate);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
            intent.setClass(this, TestAstigmatismResultViewActivity.class);
            startActivity(intent);
        } else if("敏感度".equals(type)){
            Intent intent = new Intent();
            intent.putExtra("id",id);
            intent.putExtra("result",result);
            intent.putExtra("eye",eye);
            intent.putExtra("correctRate",correctRate);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
            intent.setClass(this, TestAstigmatismResultViewActivity.class);
            startActivity(intent);
        }else {
            Intent intent = new Intent();
            intent.putExtra("id",id);
            intent.putExtra("result",result);
            intent.putExtra("eye",eye);
            intent.putExtra("correctRate",correctRate);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
            intent.setClass(this, TestAstigmatismResultViewActivity.class);
            startActivity(intent);
        }
    }
}
