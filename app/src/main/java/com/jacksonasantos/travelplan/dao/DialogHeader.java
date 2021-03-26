package com.jacksonasantos.travelplan.dao;

import com.jacksonasantos.travelplan.dao.interfaces.ListItemDialog;

public class DialogHeader implements ListItemDialog {
    String txt_header;

    public DialogHeader(String txt_header) {
        this.txt_header = txt_header;
    }

    public String getTxt_header() {
        return txt_header;
    }
    public void setTxt_header(String txt_header) {
        this.txt_header = txt_header;
    }

    @Override
    public int getItemType() {
        return ListItemDialog.TYPE_HEADER;
    }
}