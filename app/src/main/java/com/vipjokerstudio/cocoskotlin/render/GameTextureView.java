package com.vipjokerstudio.cocoskotlin.render;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.SurfaceTexture;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Choreographer;
import android.view.MotionEvent;
import android.view.TextureView;


public class GameTextureView extends TextureView
        implements TextureView.SurfaceTextureListener, Choreographer.FrameCallback {


    private Renderer renderer;
    private boolean isRunning = false;
    private long previousTime;
    private final int fps = 70;
    private MotionEvent lastEvent;
    public GameTextureView(Context context) {
        super(context);
        init();
    }

    public GameTextureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GameTextureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        setSurfaceTextureListener(this);
    }

    public void setRenderer(Renderer renderer){
        this.renderer = renderer;
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        Choreographer.getInstance().postFrameCallback(this);
        if(renderer!= null){
            renderer.init(width,height);
        }

    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {

//        boolean retry = true;
//
//        drawingThread.setRunning(false);
//
//        while (retry) {
//            try {
//                drawingThread.join();
//                retry = false;
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
        return false;
    }

    Paint paint = new Paint();

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

       lastEvent = event;
       processInput();
        return true;
    }

    private void processInput(){
        if(lastEvent != null) {
            switch (lastEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    renderer.onTouchStart(lastEvent.getX(), lastEvent.getY());
                    break;
                case MotionEvent.ACTION_MOVE:
                    renderer.onTouchMove(lastEvent.getX(), lastEvent.getY());
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    renderer.onTouchEnd(lastEvent.getX(), lastEvent.getY());
                    break;
            }
        }
    }

//    public Renderer getRenderer() {
//        return renderer;
//    }
//
//    public void setRenderer(Renderer renderer) {
//
//
//
//        this.renderer = renderer;
//        drawingThread = new DrawingThread(renderer, this);
//        drawingThread.setRunning(true);
//        drawingThread.start();
//
//        if(width!= 0 && height != 0){
//            this.renderer.init(width,height);
//        }
//
//
//
//    }

    @Override
    public void doFrame(long frameTimeNanos) {




        long currentTimeMillis = System.currentTimeMillis();
        long elapsedTimeMs = currentTimeMillis - previousTime;
        long sleepTimeMs = (long) (1000f / fps - elapsedTimeMs);
        paint.setTextSize(40);
        paint.setColor(Color.RED);
        Canvas canvas = lockCanvas();

        canvas.drawText("DEBUG " + elapsedTimeMs,100,100,paint);
        renderer.draw(canvas, elapsedTimeMs);

        unlockCanvasAndPost(canvas);
//        processInput();

        previousTime = System.currentTimeMillis();
        Choreographer.getInstance().postFrameCallback(this);


    }
}






//    mport android.content.Context;
//        import android.graphics.Canvas;
//        import android.graphics.drawable.Drawable;
//        import android.support.v4.content.res.ResourcesCompat;
//        import android.util.AttributeSet;
//        import android.view.Choreographer;
//        import android.view.View;
//
//public class MiniGameView extends View implements Choreographer.FrameCallback
//{
//    private final float mDisplayDensity;
//
//    private long mFrameTime = System.nanoTime();
//
//    private final Drawable mBackground;
//    private final Drawable mMonkey;
//
//    public MiniGameView(Context context)
//    {
//        this(context, null);
//    }
//
//    public MiniGameView(Context context, AttributeSet attrs)
//    {
//        super(context, attrs);
//
//        mDisplayDensity = getResources().getDisplayMetrics().density;
//
//        // Load graphics
//        mBackground = ResourcesCompat.getDrawable(getResources(), R.drawable.background, null);
//        mMonkey = ResourcesCompat.getDrawable(getResources(), R.drawable.monkey, null);
//
//        Choreographer.getInstance().postFrameCallback(this);
//    }
//
//    // Receive time in nano seconds at last VSYNC. Use this frameTime for smooth animations!
//    @Override
//    public void doFrame(long frameTimeNanos)
//    {
//        mFrameTime = frameTimeNanos;
//
//        Choreographer.getInstance().postFrameCallback(this);
//        invalidate();
//    }
//
//    // Draw game here
//    @Override
//    protected void onDraw(Canvas canvas)
//    {
//        drawBackground(canvas);
//        drawSprites(canvas);
//    }
//
//    private void drawBackground(Canvas canvas)
//    {
//        mBackground.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
//        mBackground.draw(canvas);
//    }
//
//    private void drawSprites(Canvas canvas)
//    {
//        double t = mFrameTime * 0.00000001;
//
//        int width = canvas.getWidth();
//        int height = canvas.getHeight();
//
//        for(int i=0;i<8;i++)
//        {
//            double x = width * (1 + Math.sin(-0.181 * t)) * 0.5;
//            double y = height * (1 - Math.cos(0.153 * t)) * 0.5;
//
//            int size = (int)Math.round((80 + 40 * Math.cos(0.2 * t)) * mDisplayDensity);
//
//            drawSprite(canvas, mMonkey, (int) x, (int) y, size, size);
//
//            t += 0.8;
//        }
//    }
//
//    private void drawSprite(final Canvas canvas, final Drawable sprite, int x, int y, int w2, int h2)
//    {
//        sprite.setBounds(x - w2, y - h2, x + w2, y + h2);
//        sprite.draw(canvas);
//    }
//
//}