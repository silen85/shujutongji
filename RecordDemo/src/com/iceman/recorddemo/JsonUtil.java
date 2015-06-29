
package com.iceman.recorddemo;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.iceman.recorddemo.RecordItem.Field;
import com.iceman.recorddemo.RecordItem.ShowForm;

public class JsonUtil {

    public static RecordItem getRecordItem(String str) {
        try {
            RecordItem item = new RecordItem();
            JSONArray array = new JSONArray(str);
            JSONObject jsob = array.getJSONObject(0);
            JSONArray descarray = jsob.getJSONArray("desc");
            Field[] fields = new Field[descarray.length()];
            for (int i = 0; i < descarray.length(); i++) {
                jsob = descarray.getJSONObject(i);
                Field field = item.new Field();
                field.coord = jsob.getString("descCoord");
                field.id = jsob.getString("descId");
                field.name = jsob.getString("descName");
                field.type = jsob.getString("descType");
                field.decimal = jsob.getInt("decimal");
                fields[i] = field;
            }
            item.setFields(fields);

            jsob = array.getJSONObject(0);
            descarray = jsob.getJSONArray("data");
            String[][] data = new String[descarray.length()][item.getFields().length];
            for (int i = descarray.length() - 1; i >= 0; i--) {
                jsob = descarray.getJSONObject(i);
                for (int j = 0; j < item.getFields().length; j++) {
                    data[descarray.length() - 1 - i][j] = jsob.getString(item.getFields()[j].id);
                }
            }
            item.setData(data);

            jsob = array.getJSONObject(0);
            descarray = jsob.getJSONArray("show");
            ShowForm[] forms = new ShowForm[descarray.length()];
            for (int i = 0; i < forms.length; i++) {
                JSONObject obj = descarray.getJSONObject(i);
                ShowForm form = item.new ShowForm();
                form.content = obj.getString("showContent");
                form.type = obj.getInt("showType");
                form.title = obj.getString("showTitle");
                forms[i] = form;
            }
            item.setForms(forms);
            return item;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getJsonFromAssets(Context context, String fileName) {
        try {
            InputStreamReader inputReader = new InputStreamReader(context.getResources()
                    .getAssets().open(fileName), "utf-8");
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            StringBuilder result = new StringBuilder();
            while ((line = bufReader.readLine()) != null) {
                result.append(line);
            }
            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
