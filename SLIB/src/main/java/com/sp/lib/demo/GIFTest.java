package com.sp.lib.demo;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Movie;
import android.graphics.Paint;
import android.view.View;
import android.view.ViewGroup;

import com.sp.lib.R;

public class GIFTest extends SlibDemoWrapper {
    protected GIFTest(SlibDemo demo) {
        super(demo, "GIF", "gif test");
    }

    @Override
    protected void onCreate() {
        setContentView(new CustomGifView(getActivity()));
    }

    class CustomGifView extends View {
        Movie mMovie;
        long mMovieStart;
        Paint mPaint=new Paint();

        public CustomGifView(Context context) {
            super(context);
            mMovie = Movie.decodeStream(getResources().openRawResource(
                    R.raw.image));
            mPaint.setColor(Color.BLACK);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            setMeasuredDimension(400, 400);
        }

        public void onDraw(Canvas canvas) {
            long now = android.os.SystemClock.uptimeMillis();
            canvas.drawRect(0, 0, getWidth(), getHeight(),mPaint );
            if (mMovieStart == 0) { // first time
                mMovieStart = now;
            }
            if (mMovie != null) {

                int dur = mMovie.duration();
                if (dur == 0) {
                    dur = 1000;
                }
                int relTime = (int) ((now - mMovieStart) % dur);
                mMovie.setTime(relTime);
                mMovie.draw(canvas, 0, 0);
                invalidate();
            }
        }
    }
}
