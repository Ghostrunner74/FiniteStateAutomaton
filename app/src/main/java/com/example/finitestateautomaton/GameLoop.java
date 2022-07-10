package com.example.finitestateautomaton;

import android.graphics.Canvas;
import android.view.Surface;
import android.view.SurfaceHolder;

import java.util.Observer;

public class GameLoop extends Thread{

    private static final double MAX_UPS = 30;
    private static final double UPS_PERIOD = 1000/MAX_UPS;
    private boolean isRunning;
    private SurfaceHolder surfaceHolder;
    private Game game;
    private double getAverageUPS;
    private double getAverageFPS;

    public GameLoop(Game game, SurfaceHolder surfaceHolder) {
            this.game = game;
            this.surfaceHolder = surfaceHolder;
    }

    public double getAverageUPS() {
        return getAverageUPS;
    }

    public double getAverageFPS() {
        return getAverageFPS;
    }

    public void startLoop() {
        isRunning = true;
        start();
    }

    @Override
    public void run() {
        super.run();

        int updateCount = 0;
        int frameCount = 0;

        long startTime;
        long elapsedTime;
        long sleepTime;

        Canvas canvas = null;

        startTime = System.currentTimeMillis();
        //GameLoop
        while (isRunning) {

            try {
                canvas = surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                    game.update();
                    updateCount++;

                    game.draw(canvas);
                }
            } catch (IllegalArgumentException e){
                e.printStackTrace();
            } finally {
                if (canvas != null) {
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                        frameCount++;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            //Slowing down game loop to keep UPS < MAX_UPS

            elapsedTime = System.currentTimeMillis() - startTime;
            sleepTime = (long) (updateCount * UPS_PERIOD - elapsedTime);

            if(sleepTime > 0) {
                try {
                    sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            //Skip some frames to keep stable UPS

            while (sleepTime < 0 && updateCount < MAX_UPS - 1) {
                game.update();
                updateCount++;
                elapsedTime = System.currentTimeMillis() - startTime;
                sleepTime = (long) (updateCount * UPS_PERIOD - elapsedTime);
            }

            // Average FPS and average UPS calculation

            elapsedTime = System.currentTimeMillis() - startTime;

            if (elapsedTime >= 1000) {
                getAverageUPS = updateCount / ((elapsedTime)/1000);
                getAverageFPS = frameCount / ((elapsedTime)/1000);
                updateCount = 0;
                frameCount = 0;
                startTime = System.currentTimeMillis();
            }

        }
    }
}
