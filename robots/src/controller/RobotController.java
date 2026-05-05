package controller;

import model.RobotModel;
import java.awt.Point;
import java.util.Timer;
import java.util.TimerTask;

public class RobotController {
    private final RobotModel model;
    private final Timer timer;
    private static final double FIXED_DELTA_TIME = 10;

    public RobotController(RobotModel model) {
        this.model = model;
        this.timer = new Timer("model updater", true);
    }

    public void startModelUpdates(int periodMs) {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                model.updateModel(FIXED_DELTA_TIME);
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
