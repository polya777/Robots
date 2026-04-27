package gui;

import model.RobotCoordinatesWindow;
import model.RobotModel;

import java.awt.BorderLayout;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;

public class GameWindow extends JInternalFrame
{
    private final GameVisualizer m_visualizer;
    private final RobotModel model;
    private final RobotCoordinatesWindow coordinatesWindow;
    public GameWindow() 
    {
        super("Игровое поле", true, true, true, true);
        model = new RobotModel();
        m_visualizer = new GameVisualizer(model);
        coordinatesWindow = new RobotCoordinatesWindow(model);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_visualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
    }
    public RobotCoordinatesWindow getCoordinatesWindow() {
        return coordinatesWindow;
    }
}
