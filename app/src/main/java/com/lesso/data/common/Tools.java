package com.lesso.data.common;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tools {

    public static boolean isNetworkAvailable(Context context) {
        // Context context = mActivity.getApplicationContext();
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static String decodeContent(String paramString) {
        return URLDecoder.decode(paramString);
    }

    public static void dialTheNumber(Context paramContext, String paramString) {
        Uri localUri = Uri.parse("tel:" + paramString);
        Intent localIntent = new Intent("android.intent.action.DIAL", localUri);
        paramContext.startActivity(localIntent);
    }

    public static String encodeContent(String paramString) {
        try {
            return URLEncoder.encode(paramString, HTTP.UTF_8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return URLEncoder.encode(paramString);
        }
    }

    private static byte[] getBytes(InputStream paramInputStream)
            throws IOException {
        ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
        byte[] arrayOfByte = new byte[1024];
        while (true) {
            int i = paramInputStream.read(arrayOfByte, 0, 1024);
            if (i == -1)
                return localByteArrayOutputStream.toByteArray();
            localByteArrayOutputStream.write(arrayOfByte, 0, i);
            localByteArrayOutputStream.flush();
        }
    }

    public static boolean isEmail(String paramString) {
        return Pattern
                .compile(
                        "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$")
                .matcher(paramString).matches();
    }

    /**
     * 检测网络是否存在
     */
    public static void HttpTest(Activity mActivity) {
        if (!isNetworkAvailable(mActivity)) {
            AlertDialog.Builder builders = new AlertDialog.Builder(mActivity);
            builders.setTitle("温馨提示");
            builders.setMessage("连接异常,请检查您的网络是否正常！");
            /*
			 * LayoutInflater _inflater = LayoutInflater.from(mActivity); View
			 * convertView = _inflater.inflate(R.layout.error,null);
			 * builders.setView(convertView);
			 */
            builders.setPositiveButton("确定", null);
            builders.create().show();
        }
    }

    public static Bitmap laodImageFromFile(String filePath) {
        File localFile = new File(filePath);
        Bitmap localBitmap = null;
        if (localFile.exists()) {
            localBitmap = BitmapFactory.decodeFile(filePath);
        } else {
            localBitmap = null;// BitmapFactory.decodeFile("item_default_picture");
        }
        return localBitmap;
    }

    public static Bitmap loadImageFromStream(InputStream is) {
        byte[] arrayOfByte;
        Bitmap bitMap = null;
        try {
            arrayOfByte = getBytes(is);
            int i = arrayOfByte.length;
            bitMap = BitmapFactory.decodeByteArray(arrayOfByte, 0, i);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭InputStream
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bitMap;
    }

    public static Bitmap loadImageFromUrl(String strImageUrl) {
        Bitmap bitMap = null;
        URL aryURI;
        try {
            aryURI = new URL(strImageUrl);
            URLConnection conn = aryURI.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            bitMap = BitmapFactory.decodeStream(is);
            is.close();

        } catch (Exception e) {
        }
        return bitMap;
    }

    public static Bitmap loadImageFromUrlWithOpt(String strImageUrl) {
        Bitmap bitMap = null;
        URL aryURI;
        try {
            aryURI = new URL(strImageUrl);
            URLConnection conn = aryURI.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inSampleSize = 4;
            bitMap = BitmapFactory.decodeStream(is, null, opts);
            is.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitMap;
    }

    public static String md5(String paramString) {
        try {
            MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
            byte[] arrayOfByte = paramString.getBytes();
            localMessageDigest.update(arrayOfByte);
            String str1 = toHexString(localMessageDigest.digest());
            return str1;
        } catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
            while (true) {
                localNoSuchAlgorithmException.printStackTrace();
                String str2 = paramString;
            }
        }
    }

    public static boolean saveFile(Bitmap paramBitmap, String paramString1,
                                   String paramString2) {
        File localFile1 = new File(paramString1);
        boolean bool1;
        if (!localFile1.exists())
            bool1 = localFile1.mkdirs();
        File localFile2 = new File(paramString2);
        boolean bool2 = false;
        try {
            FileOutputStream localFileOutputStream = new FileOutputStream(
                    localFile2);
            BufferedOutputStream localBufferedOutputStream = new BufferedOutputStream(
                    localFileOutputStream);
            if (paramBitmap != null) {
                Bitmap.CompressFormat localCompressFormat = Bitmap.CompressFormat.PNG;
                bool2 = paramBitmap.compress(localCompressFormat, 100,
                        localBufferedOutputStream);
                localBufferedOutputStream.flush();
                localBufferedOutputStream.close();
            }
            return bool2;
        } catch (Exception localException) {
            while (true)
                localException.printStackTrace();
        }
    }

    public static String toHexString(byte[] paramArrayOfByte) {
        int i = paramArrayOfByte.length * 2;
        StringBuilder localStringBuilder1 = new StringBuilder(i);
        int j = 0;
        while (true) {
            int k = paramArrayOfByte.length;
            if (j >= k)
                return localStringBuilder1.toString();
            char[] arrayOfChar1 = new char[16];
            int m = (paramArrayOfByte[j] & 0xF0) >>> 4;
            char c1 = arrayOfChar1[m];
            StringBuilder localStringBuilder2 = localStringBuilder1.append(c1);
            char[] arrayOfChar2 = new char[16];
            int n = paramArrayOfByte[j] & 0xF;
            char c2 = arrayOfChar2[n];
            StringBuilder localStringBuilder3 = localStringBuilder1.append(c2);
            j += 1;
        }
    }

    /**
     * 将javabean集合转换成json字符串
     *
     * @param e javabean 集合对象
     * @return 返回json字符串
     */
    public static <E extends Object> String javaBeanListToJson(List<E> e) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<E>>() {
        }.getType();
        return gson.toJson(e, type);
        // beanListToJson;
    }

    public static Map<String, Object> jsonToMap(String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, Object>>() {
        }.getType();
        return gson.fromJson(json, type);
        // beanListToJson;
    }

    public static <T> List<T> jsonToList(String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<T>>() {
        }.getType();
        // Log.e("gggggggg",type.getClass().getName());
        return gson.fromJson(json, type);
        // beanListToJson;
    }

    public static <T> String listTojson(List<T> list) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<T>>() {
        }.getType();
        return gson.toJson(list, type);
        // beanListToJson;
    }

    public static String map2Json(Map m) {
        Gson gson = new Gson();
        Type type = new TypeToken<Map>() {
        }.getType();
        return gson.toJson(m, type);
        // beanListToJson;
    }

    /**
     * 从json HASH表达式中获取一个map，该map支持嵌套功能
     *
     * @param jsonString
     * @return
     * @throws JSONException
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static Map json2Map(String jsonString) {
        JSONObject jsonObject;
        Map valueMap = new HashMap();
        try {
            jsonObject = new JSONObject(jsonString);
            Iterator keyIter = jsonObject.keys();
            String key;
            Object value;

            while (keyIter.hasNext()) {
                key = (String) keyIter.next();
                value = jsonObject.get(key);

                if (value instanceof JSONArray) {
                    List vlist = new ArrayList();
                    vlist = Tools.getList(value.toString());
                    value = vlist;
                }
                valueMap.put(key, value);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return valueMap;
    }

    // 把json 转换为 ArrayList 形式
    @SuppressWarnings("unchecked")
    public static List<Map<String, Object>> getList(String jsonString) {
        List<Map<String, Object>> list = null;
        try {
            JSONArray jsonArray = new JSONArray(jsonString);
            JSONObject jsonObject;
            list = new ArrayList<Map<String, Object>>();
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                list.add(json2Map(jsonObject.toString()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void downloadFile(Context context, String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        context.startActivity(intent);
    }

    // 全角转化为半角的方法
    public static String ToDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }

    // 半角转化为全角的方法
    public static String ToSBC(String input) {
        // 半角转全角：
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 32) {
                c[i] = (char) 12288;
                continue;
            }
            if (c[i] < 127 && c[i] > 32)
                c[i] = (char) (c[i] + 65248);
        }
        return new String(c);
    }

    public static String stringFilter(String str) {
        str = str.replaceAll("【", "[").replaceAll("】", "]")
                .replaceAll("！", "!").replaceAll("：", ":");// 替换中文标号
        String regEx = "[『』]"; // 清除掉特殊字符
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    /**
     * 两个日期间的状态，现只到天、小时、分钟前
     *
     * @param dbeginDate
     * @param dendDate
     * @return
     */
    public static String getInternalDateTime(Date dbeginDate, Date dendDate) {
        String sStr = "";
        try {
            SimpleDateFormat format = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss");

            Date beginDate = format.parse(formatDate(dbeginDate,
                    "yyyy-MM-dd HH:mm:ss"));
            Date endDate = format.parse(formatDate(dendDate,
                    "yyyy-MM-dd HH:mm:ss"));

            double dLeftTime = Arith
                    .sub(endDate.getTime(), beginDate.getTime());
            double dDay = Arith.div(dLeftTime, 24 * 60 * 60 * 1000);
            double dHour = Arith.div(dLeftTime, 60 * 60 * 1000);
            double dMinute = Arith.div(dLeftTime, 60 * 1000);

            if (dDay >= 1) {
                sStr = (int) Math.ceil(dDay) + "天前";
            } else if (dHour >= 1) {
                sStr = (int) Math.ceil(dHour) + "小时前";
            } else if (dMinute >= 1) {
                sStr = (int) Math.ceil(dMinute) + "分钟前";
            } else {
                sStr = "在1分钟内";
            }
        } catch (Exception e) {

        }

        return sStr;
    }

    /**
     * 两个日期间的状态， 1、“当前时间”减去“发布时间”在1分钟内显示秒数，如：10秒前 2、1小时内显示分钟数，如：2分钟前
     * 3、1~24小时内显示小时，如：3小时前 4、24小时后显示月日，如：2010-03-01
     *
     * @param dbeginDate
     * @param dendDate
     * @return
     */
    public static String getDiffDateTime(Date dbeginDate, Date dendDate,
                                         String sformat) {
        String sStr = "";
        try {
            SimpleDateFormat format = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat format1 = new SimpleDateFormat(
                    sformat);
            Date beginDate = format.parse(formatDate(dbeginDate,
                    "yyyy-MM-dd HH:mm:ss"));
            Date endDate = format.parse(formatDate(dendDate,
                    "yyyy-MM-dd HH:mm:ss"));

            double dLeftTime = Arith
                    .sub(endDate.getTime(), beginDate.getTime());
            double dHour = Arith.div(dLeftTime, 60 * 60 * 1000);
            double dMinute = Arith.div(dLeftTime, 60 * 1000);
            double dSecond = Arith.div(dLeftTime, 1000);

            if (dHour >= 24) {
                sStr = format1.format(dbeginDate);
            } else if (dHour >= 1 && dHour < 24) {
                sStr = (int) Math.floor(dHour) + "小时前";
            } else if (dMinute >= 1 && dHour < 1) {
                sStr = (int) Math.floor(dMinute) + "分钟前";
            } else if (dSecond < 1) {
                sStr = "1秒前";
            } else {
                sStr = (int) Math.floor(dSecond) + "秒前";
            }
        } catch (Exception e) {

        }

        return sStr;
    }

    /**
     * 使用格式<b>_pattern</b>格式化日期输出
     *
     * @param _date    日期对象
     * @param _pattern 日期格式
     * @return 格式化后的日期
     */
    public static String formatDate(Date _date, String _pattern) {
        if (_date == null) {
            return "";
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(_pattern);
        String stringDate = simpleDateFormat.format(_date);

        return stringDate;
    }

    public static void callThePhone(Context context, String tel) {
        // 调用系统方法拨打电话
        Intent dialIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
                + tel));
        context.startActivity(dialIntent);
    }

    public static void sendMsg(Context context, String mobile) {
        // 调用系统方法发信息
        Intent dialIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"
                + mobile));
        context.startActivity(dialIntent);
    }

    public static void sendEmail(Context context, String email) {
        // 调用系统方法发邮件
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
        intent.setType("text/plain");
        // Intent.createChooser(intent, "请选择邮件类型");
        context.startActivity(intent);
    }

    public static String date2String(Date utilDate) {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return f.format(utilDate);
    }

    public static boolean isNum(String str) {
        return str.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
    }

    public static String formatPhone(String mobile) {

        if (!"".equals(mobile) && mobile.length() >= 11) {
            return mobile.substring(0, 3) + "****" + mobile.substring(7);
        } else {
            return "";
        }

    }
}