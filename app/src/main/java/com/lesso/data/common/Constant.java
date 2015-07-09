package com.lesso.data.common;

import java.text.SimpleDateFormat;

/**
 * Created by meisl on 2015/6/25.
 */
public class Constant {

    public static final String URL_UPDATE = "http://192.168.4.13:65153/lswm_text/api/VersionHandler.ashx";
    public static final String URL_LOGIN = "http://192.168.4.13:65153/lswm_text/api/AccLogin_Handler.ashx";
    public static final String URL_SETUP_SCRATPWD = "http://192.168.4.13:65153/lswm_text/api/AccLogin_Handler.ashx";
    public static final String URL_PWD_AUTHORITY = "http://192.168.4.13:65153/lswm_text/api/AccLogin_Handler.ashx";
    public static final String URL_REPORT_ACCESS = "http://10.10.7.221/api";
    public static final String URL_REPORT_SALES = "http://192.168.4.13:65153/lswm_text/api/ZSDHandler.ashx";
    public static final String URL_REPORT_STORE = "http://192.168.4.13:65153/lswm_text/api/WM_OutHandler.ashx";
    public static final String URL_REPORT_USER = "http://10.10.7.137:8021/api/Hybris_Handler.ashx";

    public static final String APP_KEY = "fc98141d";

    public static final String SECRET_KEY = "94bc92c24bc8e5d98bfa3ddebb50d196";

    public static final SimpleDateFormat DATE_FORMAT_1 = new SimpleDateFormat("yyyy-MM-dd");

    public static final SimpleDateFormat DATE_FORMAT_2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static final SimpleDateFormat DATE_FORMAT_3 = new SimpleDateFormat("yyyy-MM");

    public static final SimpleDateFormat DATE_FORMAT_4 = new SimpleDateFormat("HHmmss");


    public static int HTTP_STATUS_CODE_SUCCESS = 200;
    public static String ACCESS_STATUS_CODE_SUCCESS = "1";
    public static String ACCESS_STATUS_CODE_FAILED = "0";

    public static int LOGIN_STATUS_CODE_WRONGPWD = -1; // -1密码或用户名不对
    public static int LOGIN_STATUS_CODE_WRONGDONGPWD = -2; // -2动态密码不对
    public static int LOGIN_STATUS_CODE_NODONGPWD = -3; // -3没有绑定动态密码器
    public static int LOGIN_STATUS_CODE_SUCCESS = 0; // 正数表示成功

}
