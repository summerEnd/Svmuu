package com.sp.lib.demo;

import android.database.Cursor;
import android.widget.TextView;

import com.sp.lib.activity.SContentProvider;

public class ContentProviderTest extends SlibDemoWrapper {
    public ContentProviderTest(SlibDemo demo) {
        super(demo, "Content Provider", "content provider test");
    }

    @Override
    protected void onCreate() {
        TextView tv = new TextView(getActivity());
        setContentView(tv);
        String projection[] = new String[]{SContentProvider.COLUMN_BYTES_SO_FAR, SContentProvider.COLUMN_STATUS, SContentProvider.COLUMN_LOCALE_URI};
        Cursor c = getActivity().getContentResolver().query(SContentProvider.CONTENT_URI, projection, null, null, null);
        if (c == null) {
            tv.setText("null");
            return;
        }

        while (c.moveToNext()) {
            StringBuilder sb = new StringBuilder();
            sb.append(c.getColumnName(0) + ":" + c.getInt(0));
            sb.append(c.getColumnName(1) + ":" + c.getInt(1));
            sb.append(c.getColumnName(2) + ":" + c.getString(2));
            tv.append(sb);
        }
        c.close();
    }
}
