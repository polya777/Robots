package model;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

public class RobotCoordinatesWindow extends JInternalFrame implements Observer {
    private final RobotModel model;
    private final JLabel positionLabel;

    public RobotCoordinatesWindow(RobotModel model) {
        super("Координаты робота", true, true, true, true);
        this.model = model;

        setSize(250, 80);
        setLocation(10, 10);

        JPanel panel = new JPanel(new FlowLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        positionLabel = new JLabel();
        positionLabel.setFont(new Font("Monospaced", Font.BOLD, 14));

        panel.add(positionLabel);
        getContentPane().add(panel);
        model.addObserver(this);
        updateCoordinates();
    }

    private void updateCoordinates() {
        positionLabel.setText(String.format("X = %.2f , Y = %.2f",
                model.getRobotPositionX(),
                model.getRobotPositionY()));
    }

    @Override
    public void update(Observable o, Object arg) {
        SwingUtilities.invokeLater(this::updateCoordinates);
    }
}