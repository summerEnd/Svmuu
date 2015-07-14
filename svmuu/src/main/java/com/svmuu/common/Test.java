package com.svmuu.common;

import android.test.AndroidTestCase;
import android.text.Html;

import com.svmuu.common.adapter.chat.ChatTagHandler;

/**
 * Created by user1 on 2015/7/14.
 */
public class Test extends AndroidTestCase {
    public void testSpan(){
        ChatTagHandler handler=new ChatTagHandler();
        String source="<p><span style=\"color: rgb(0, 176, 80);\">娃儿问问二为二为二为二热污染二为二</span><br/></p>";
        Html.fromHtml(source, null, handler);
    }
}
