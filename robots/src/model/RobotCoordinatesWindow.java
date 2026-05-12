package model;

import javax.swing.*;
import java.awt.*;

public class RobotCoordinatesWindow extends JInternalFrame implements ModelObserver {
    private final JLabel positionLabel;

    public RobotCoordinatesWindow() {
        super("Координаты робота", true, true, true, true);

        setSize(250, 80);
        setLocation(10, 10);

        JPanel panel = new JPanel(new FlowLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        positionLabel = new JLabel();
        positionLabel.setFont(new Font("Monospaced", Font.BOLD, 14));

        panel.add(positionLabel);
        getContentPane().add(panel);
    }

    private void updateCoordinates(RobotModel model) {
        positionLabel.setText(String.format("X = %.2f , Y = %.2f",
                model.getRobotPositionX(),
                model.getRobotPositionY()));
    }

    @Override
    public void onModelUpdated(RobotModel model) {
        SwingUtilities.invokeLater(() -> updateCoordinates(model));
    }
}