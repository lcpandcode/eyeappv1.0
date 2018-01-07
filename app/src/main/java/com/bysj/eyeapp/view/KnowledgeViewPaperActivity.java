package com.bysj.eyeapp.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

/**
 * 查看文章内容activity
 */
public class KnowledgeViewPaperActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_knowledge_view_paper);
    }

    /**
     * 标题栏返回按钮
     */
    public void titleReturn(View v){
        finish();
    }


}
