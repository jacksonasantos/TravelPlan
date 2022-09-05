package com.jacksonasantos.travelplan.dao.general;

public class DialogItem implements ListItemDialog {
    String txt_description;
    String txt_detail;

    public DialogItem(String txt_description, String txt_detail) {
        setTxt_description(txt_description);
        setTxt_detail(txt_detail);
    }

    public String getTxt_description() {
        return txt_description;
    }
    public void setTxt_description(String txt_description) { this.txt_description = txt_description; }

    public String getTxt_detail() {
        return txt_detail;
    }
    public void setTxt_detail(String txt_detail) { this.txt_detail = txt_detail; }

    @Override
    public int getItemType() {
        return ListItemDialog.TYPE_ITEM;
    }
}