package controller;

import model.RobotModel;
import java.awt.Point;
import java.util.Timer;
import java.util.TimerTask;

public class RobotController {
    private final RobotModel model;
    private final Timer timer;
    private long lastUpdateTime;

    public RobotController(RobotModel model) {
        this.model = model;
        this.timer = new Timer("model updater", true);
    }

    public void startModelUpdates(int periodMs) {
        lastUpdateTime = System.currentTimeMillis();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                long currentTime = System.currentTimeMillis();
                double deltaTime = (currentTime - lastUpdateTime) / 1000.0; // в секундах
                lastUpdateTime = currentTime;

                // Ограничиваем deltaTime, чтобы избежать больших скачков
                if (deltaTime > 0.05) {
                    deltaTime = 0.05;
                }

                model.updateModel(deltaTime);
            }
        }, 0, periodMs);
    }

    public void setTarget(Point target) {
        model.setTargetPosition(target.x, target.y);
    }

    public void stopModelUpdates() {
        timer.cancel();
    }
}
