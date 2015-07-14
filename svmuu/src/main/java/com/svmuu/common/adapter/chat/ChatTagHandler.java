package com.svmuu.common.adapter.chat;

import android.text.Editable;
import android.text.Html;

import org.xml.sax.XMLReader;

/**
 * Created by user1 on 2015/7/14.
 * 聊天标签处理
 */
public class ChatTagHandler implements Html.TagHandler {
    @Override
    public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
        if (opening) {
            if ("span".equals(tag)) {

            }
        }
    }


}
