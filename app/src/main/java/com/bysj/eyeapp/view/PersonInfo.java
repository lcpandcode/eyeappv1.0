package com.bysj.eyeapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

/**
 * Created by Administrator on 2018/1/10.
 */

public class PersonInfo extends BaseActivity{
    private Button backward;
    private Button changePersonInfo;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personcentre_info);
        init();

    }

    private void init() {
        backward=findViewById(R.id.btn_backward);
        changePersonInfo=findViewById(R.id.btn_changePersonInfo);
        backward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });
    }

}