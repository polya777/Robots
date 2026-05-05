package gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.util.Observer;
import javax.swing.JPanel;

import controller.RobotController;
import model.RobotModel;
import model.ModelObserver;

public class GameVisualizer extends JPanel implements ModelObserver
{
    private RobotModel model;
    private RobotController controller;

    public GameVisualizer(RobotModel model, RobotController controller)
    {
        this.model = model;
        this.controller = controller;

        model.addObserver(this);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                controller.setTarget(e.getPoint());
            }
        });
        setDoubleBuffered(true);
    }

    @Override
    public void paint(Graphics g)
    {
        super.paint(g);
        Graphics2D g2d = (Graphics2D)g;
        drawRobot(g2d, (int)Math.round(model.getRobotPositionX()),
                (int)Math.round(model.getRobotPositionY()),
                model.getRobotDirection());
        drawTarget(g2d, model.getTargetPositionX(), model.getTargetPositionY());
    }

    @Override
    public void onModelUpdated(RobotModel model) {
        EventQueue.invokeLater(this::repaint);
    }

    private static void fillOval(Graphics g, int centerX, int centerY, int diam1, int diam2)
    {
        g.fillOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }
    
    private static void drawOval(Graphics g, int centerX, int centerY, int diam1, int diam2)
    {
        g.drawOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }
    
    private void drawRobot(Graphics2D g, int x, int y, double direction)
    {
        int robotCenterX = x;
        int robotCenterY = y;
        AffineTransform t = AffineTransform.getRotateInstance(direction, robotCenterX, robotCenterY); 
        g.setTransform(t);
        g.setColor(Color.MAGENTA);
        fillOval(g, robotCenterX, robotCenterY, 30, 10);
        g.setColor(Color.BLACK);
        drawOval(g, robotCenterX, robotCenterY, 30, 10);
        g.setColor(Color.WHITE);
        fillOval(g, robotCenterX  + 10, robotCenterY, 5, 5);
        g.setColor(Color.BLACK);
        drawOval(g, robotCenterX  + 10, robotCenterY, 5, 5);
        g.setTransform(new AffineTransform());
    }
    
    private void drawTarget(Graphics2D g, int x, int y)
    {
        AffineTransform t = AffineTransform.getRotateInstance(0, 0, 0);
        g.setTransform(t);
        g.setColor(Color.GREEN);
        fillOval(g, x, y, 5, 5);
        g.setColor(Color.BLACK);
        drawOval(g, x, y, 5, 5);
    }
}
