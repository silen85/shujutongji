package com.lesso.data.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lesso.data.LessoApplication;
import com.lesso.data.R;
import com.lesso.data.common.Constant;

/**
 * Created by meisl on 2015/7/6.
 */
public class SetupActivity extends Activity {

    public static final int REQUEST_CODE_SETUP_PASSWORD = 1;

    private Button btn_back, setup_password, setup_scratpwd, setup_logout;
    private RelativeLayout main_title;
    private TextView main_title_txt;
    private ImageView main_setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_setup);

        if (((LessoApplication) getApplication()).getLoginUser() == null) {
            finish();
        }

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
        main_setting.setVisibility(View.INVISIBLE);

        setup_password = (Button) findViewById(R.id.setup_password);
        setup_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(SetupActivity.this, SetupPWDActivity.class), REQUEST_CODE_SETUP_PASSWORD);
            }
        });

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

                SharedPreferences sp = getSharedPreferences(Constant.LESSOBI, Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.remove(Constant.LESSOBI_USERID);
                editor.remove(Constant.LESSOBI_USERNAME);
                editor.remove(Constant.LESSOBI_USERPASSWORD);
                editor.remove(Constant.LESSOBI_USERSCRATPWD);
                editor.commit();

                Intent intent = new Intent(SetupActivity.this, SplashLoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (RESULT_OK == resultCode) {
            switch (requestCode) {
                case REQUEST_CODE_SETUP_PASSWORD:
                    ((LessoApplication) getApplication()).setLoginUser(null);

                    SharedPreferences sp = getSharedPreferences(Constant.LESSOBI, Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.remove(Constant.LESSOBI_USERID);
                    editor.remove(Constant.LESSOBI_USERNAME);
                    editor.remove(Constant.LESSOBI_USERPASSWORD);
                    editor.remove(Constant.LESSOBI_USERSCRATPWD);
                    editor.commit();

                    Intent intent = new Intent(SetupActivity.this, SplashLoginActivity.class);
                    startActivity(intent);

                    sendBroadcast(new Intent(Constant.FINISH_ACTION));
                    finish();
                    break;
                default:
                    break;
            }
        }

    }

    @Override
    public void onBackPressed() {
        btn_back.performClick();
    }
}
