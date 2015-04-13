package com.sp.lib.activity;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

public class SContentProvider extends ContentProvider {

    DBHelper helper;
    private static UriMatcher matcher;
    private static final String TABLE_NAME = "download";

    private static String AUTHORITY = "com.slib.downloads";

    public static final Uri CONTENT_URI =
            Uri.parse("content://" + AUTHORITY + "/downloads");

    public static final int GET_ALL = 1;
    public static final int GET_SINGLE = 2;

    static {
        matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(AUTHORITY, "downloads", GET_ALL);
        matcher.addURI(AUTHORITY, "downloads/#", GET_SINGLE);
    }

    @Override
    public boolean onCreate() {
        helper = new DBHelper(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor query = null;
        switch (matcher.match(uri)) {
            case GET_ALL:
                query = db.query(TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case GET_SINGLE:
                query = db.query(TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
        }
        return query;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = helper.getWritableDatabase();
        long insert = db.insert(TABLE_NAME, null, values);
        db.close();
        return Uri.withAppendedPath(CONTENT_URI, "" + insert);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    private static class DBHelper extends SQLiteOpenHelper {
        private static final String DB_NAME = "sDownload1";

        public DBHelper(Context context) {
            this(context, DB_NAME, null, 4);
        }

        public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            StringBuffer sql = new StringBuffer();
            sql.append("Create table ").append(TABLE_NAME)
                    .append("(")
                    .append("_id").append(" INTEGER PRIMARY KEY AUTOINCREMENT,")
                    .append(COLUMN_LOCALE_URI).append(" TEXT,")
                    .append(COLUMN_REMOTE_URL).append(" TEXT,")
                    .append(COLUMN_LOCALE_FILE_NAME).append(" TEXT,")
                    .append(COLUMN_BYTES_SO_FAR).append(" INTEGER,")
                    .append(COLUMN_BYTES_TOTAL).append(" INTEGER,")
                    .append(COLUMN_STATUS).append(" INTEGER")
                    .append(");");

            db.execSQL(sql.toString());

            ContentValues values = new ContentValues();
            values.put(COLUMN_LOCALE_URI, "file://");
            values.put(COLUMN_REMOTE_URL, "remote://");
            db.insert(TABLE_NAME, null, values);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_LOCALE_URI, "file://");
            values.put(COLUMN_REMOTE_URL, "remote://");
            db.insert(TABLE_NAME, null, values);
        }
    }

    /**
     * Uri where downloaded file will be stored.
     */
    public static final String COLUMN_LOCALE_URI = "locale_uri";
    /**
     * The pathname of the file where the download is stored.
     */
    public static final String COLUMN_LOCALE_FILE_NAME = "file_name";
    /**
     * the URL
     */
    public static final String COLUMN_REMOTE_URL = "remote_url";
    /**
     * Number of bytes download so far.
     */
    public static final String COLUMN_BYTES_SO_FAR = "bytes_so_far";
    /**
     * Total size of the download in bytes.  This will initially be -1 and will be filled in once
     * the download starts
     */
    public static final String COLUMN_BYTES_TOTAL = "bytes_total";
    /**
     * Current status of the download, as one of the STATUS_* constants
     */
    public static final String COLUMN_STATUS = "status";

    /**
     * Value of {@link #COLUMN_STATUS} when the download is waiting to start.
     */
    public final static int STATUS_PENDING = 1 << 0;

    /**
     * Value of {@link #COLUMN_STATUS} when the download is currently running.
     */
    public final static int STATUS_RUNNING = 1 << 1;

    /**
     * Value of {@link #COLUMN_STATUS} when the download is waiting to retry or resume.
     */
    public final static int STATUS_PAUSED = 1 << 2;

    /**
     * Value of {@link #COLUMN_STATUS} when the download has successfully completed.
     */
    public final static int STATUS_SUCCESSFUL = 1 << 3;

    /**
     * Value of {@link #COLUMN_STATUS} when the download has failed (and will not be retried).
     */
    public final static int STATUS_FAILED = 1 << 4;
}
