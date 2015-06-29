
package com.iceman.recorddemo;

public class RecordItem {
    private Field[] fields;

    private ShowForm[] forms;

    private String[][] data;

    public Field[] getFields() {
        return fields;
    }

    public void setFields(Field[] fields) {
        this.fields = fields;
    }

    public ShowForm[] getForms() {
        return forms;
    }

    public void setForms(ShowForm[] forms) {
        this.forms = forms;
    }

    public String[][] getData() {
        return data;
    }

    public void setData(String[][] data) {
        this.data = data;
    }

    public RecordItem() {
        super();
    }

    public class Field {
        public String coord;

        public String id;

        public String name;

        public String type;

        public int decimal;

        public Field() {
            super();
        }
    }

    public class ShowForm {
        public String content;

        public int type;

        public String title;

        public ShowForm() {
            super();
        }
    }
}
