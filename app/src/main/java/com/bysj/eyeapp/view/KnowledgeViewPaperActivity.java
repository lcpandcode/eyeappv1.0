package com.bysj.eyeapp.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.bysj.eyeapp.exception.BackstageException;
import com.bysj.eyeapp.exception.HttpException;
import com.bysj.eyeapp.service.KnowledgeService;
import com.bysj.eyeapp.util.CustomToast;
import com.bysj.eyeapp.util.GlobalConst;
import com.bysj.eyeapp.vo.KnowledgePaperVO;

/**
 * 查看文章内容activity
 */
public class KnowledgeViewPaperActivity extends BaseActivity {


    private TextView paperTitle;
    private TextView paperContent;
    private TextView paperViewCount;

    private KnowledgeService service;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_knowledge_view_paper);
        init();
    }

    private void init(){
        paperTitle = findViewById(R.id.paper_title);
        paperContent = findViewById(R.id.paper_content);
        paperViewCount = findViewById(R.id.paper_viewcount);
        service = new KnowledgeService();
        Intent intent = getIntent();
        int paperId = intent.getIntExtra(GlobalConst.VIEW_PAPER_ID_KEY,-1);
        if(paperId==-1){
            CustomToast.showToast(getApplicationContext(),GlobalConst.PAPER_ID_ERROR);
            return ;
        }
        //请求
        KnowledgePaperVO paper = null;
        try {
            paper = service.getPaperDetail(paperId);
        } catch (HttpException e) {
            Log.e("网络异常：",GlobalConst.REMIND_NET_ERROR);
            CustomToast.showToast(getApplicationContext(),e.getMessage());
            return ;
        } catch (BackstageException e){
            Log.e("后台错误：",GlobalConst.REMIND_BACKSTAGE_ERROR);
            CustomToast.showToast(getApplicationContext(),"文章id非法");
            return ;
        }
        paperTitle.setText(paper.getTitle());
        paperContent.setText(paper.getContent());
        paperViewCount.setText(paper.getViewCount() + "");

    }

    /**
     * 标题栏返回按钮
     */
    public void titleReturn(View v){
        finish();
    }


}
