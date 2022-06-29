package com.vipjokerstudio.cocoskotlin.render;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;

import com.vipjokerstudio.cocoskotlin.core.nodes.Director;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Created by Oleh Makhobei  on 15.03.20.
 */
public class CocosSurfaceHolder implements SurfaceHolder.Callback {
    public static final String TAG = "RenderSurfaceHolder";
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    private long startTime = System.currentTimeMillis();
    public CocosSurfaceHolder() {
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.CYAN);
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.i(TAG, "surfaceCreated: ");
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Director.getInstance().init(width,height);
        render(holder);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if(!executorService.isShutdown()) {
            executorService.shutdown();
        }
        Log.i(TAG, "surfaceDestroyed: ");
    }

    private void render(SurfaceHolder holder){
        try {
            Canvas canvas = holder.lockCanvas();
            Director.getInstance().draw(canvas, System.currentTimeMillis() - startTime);
            holder.unlockCanvasAndPost(canvas);

            executorService.execute(() -> {
                render(holder);
            });//,1000/60, TimeUnit.MILLISECONDS);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
