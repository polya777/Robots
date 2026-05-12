package gui;

import controller.RobotController;
import model.RobotModel;

import java.awt.BorderLayout;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;

public class GameWindow extends JInternalFrame
{
    private final GameVisualizer m_visualizer;
    private final RobotModel model;
    private final RobotController controller;

    public GameWindow(RobotModel model1, RobotController controller1)
    {
        super("Игровое поле", true, true, true, true);
        this.model = model1;
        this.controller = controller1;
        m_visualizer = new GameVisualizer(model, controller);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_visualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
    }
}
