package com.bysj.eyeapp.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/12/27.
 */

public class PersonCentreActivity extends BaseActivity {
    private Button button;
    private Context context;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_person);
        context =this;
        button = (Button) findViewById(R.id.button_person_info);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplication(),"123t",Toast.LENGTH_LONG).show();
                Log.i("pc","onClick");
                switch (v.getId()){
                    case R.id.button_person_info:
                        //Intent intent = new Intent(this,PersonInfo.class);
                        //startActivity(intent);
                        break;
                }
            }
        });

//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                switch (checkedId){
//                    case R.id.button_person_info: {
//                        Intent intent = new Intent(context,PersonInfo.class);
//                        startActivity(intent);
//                    break;
//                    }
//                }
//
//            }
//        });
    }


}

