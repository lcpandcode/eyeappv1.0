package com.bysj.eyeapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bysj.eyeapp.exception.HttpException;
import com.bysj.eyeapp.service.KnowledgeService;
import com.bysj.eyeapp.vo.KnowledgeAdvisoryQuestionVO;

import java.util.Map;

/**
 * Created by lcplcp on 2017/12/25.
 */
public class KnowledgeAdvisoryActivity extends BaseActivity {
    //页面控件的相关变量
    private EditText titleInput;
    private EditText contentInput;

    KnowledgeService service ;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_knowledge_advisory);
        init();
    }

    /**
     * 初始化方法
     */
    private void init(){
        titleInput = findViewById(R.id.knowledge_advisory_input_title);
        contentInput = findViewById(R.id.knowledge_advisory_input_content);
        service = new KnowledgeService();
    }

    /**
     * 标题栏返回按钮
     */
    public void titleReturn(View v){
        finish();
    }

    /**
     * 提交按钮
     */
    public void submit(View v){
        String title = titleInput.getText().toString();
        String content = contentInput.getText().toString();
        //判断输入是否合法
        if(title==null || "".equals(title) || content==null || "".equals(content)){
            Toast.makeText(getApplicationContext(),"问题标题或内容不能为空！",Toast.LENGTH_SHORT).show();
            return ;
        }
        KnowledgeAdvisoryQuestionVO question = new KnowledgeAdvisoryQuestionVO();
        question.setTitle(title);
        question.setContent(content);
        Map<String,String> result = null;
        try {
            result = service.advisorySubmitQUestion(question);
        } catch (HttpException e) {
            e.printStackTrace();
        }
        String status = result.get("status");
        if("success".equals(status)){
            Toast.makeText(getApplicationContext(),"提交成功",Toast.LENGTH_SHORT).show();
            finish();
        }else {
            Toast.makeText(getApplicationContext(),"失败:" +result.get("info"),Toast.LENGTH_SHORT).show();
        }

    }











}
