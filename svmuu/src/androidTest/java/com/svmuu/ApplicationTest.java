package com.svmuu;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.text.Html;

import com.svmuu.common.adapter.chat.ChatTagHandler;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        ChatTagHandler handler=new ChatTagHandler();
        String source="<p><span style=\"color: rgb(0, 176, 80);\">娃儿问问二为二为二为二热污染二为二</span><br/></p>";
        Html.fromHtml(source, null, handler);
    }
}