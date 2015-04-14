package com.sp.lib.demo;

import android.content.Intent;

import com.sp.lib.activity.album.PhotoAlbumActivity;

public class AlbumTest extends SlibDemoWrapper {
    public AlbumTest(SlibDemo demo) {
        super(demo, "Album", "test Album");
    }

    @Override
    protected void onCreate() {
        startActivity(new Intent(getActivity(), PhotoAlbumActivity.class));
    }
}
