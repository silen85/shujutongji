package com.lesso.data.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lesso.data.LessoApplication;
import com.lesso.data.R;

/**
 * Created by meisl on 2015/7/6.
 */
public class SetupActivity extends Activity {

    private LessoApplication.LoginUser loginUser;


    private Button btn_back, setup_scratpwd, setup_logout;
    private RelativeLayout main_title;
    private TextView main_title_txt;
    private ImageView main_setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_setup);

        loginUser = ((LessoApplication) getApplication()).getLoginUser();

        btn_back = (Button) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SetupActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        main_title = (RelativeLayout) findViewById(R.id.main_title);
        main_title_txt = (TextView) findViewById(R.id.main_title_txt);
        main_setting = (ImageView) findViewById(R.id.main_setting);

        main_title_txt.setText("设置");
        main_setting.setVisibility(View.GONE);

        setup_scratpwd = (Button) findViewById(R.id.setup_scratpwd);
        setup_scratpwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SetupActivity.this, LockSetupActivity.class);
                startActivity(intent);
                finish();
            }
        });

        setup_logout = (Button) findViewById(R.id.setup_logout);
        setup_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((LessoApplication) getApplication()).setLoginUser(null);
                finish();
            }
        });
    }
}
