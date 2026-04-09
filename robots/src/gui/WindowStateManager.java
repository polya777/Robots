package gui;

import java.beans.PropertyVetoException;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JInternalFrame;
import javax.swing.SwingUtilities;

public class WindowStateManager {

    private static final String CONFIG_FILE = System.getProperty("user.home") +
            File.separator + ".robots_program_config.ser";

    private Map<String, WindowState> windowStates;

    public WindowStateManager() {
        windowStates = new HashMap<>();
    }

    public void saveWindowState(JInternalFrame frame, String windowId) {
        WindowState state = new WindowState();
        state.x = frame.getX();
        state.y = frame.getY();
        state.width = frame.getWidth();
        state.height = frame.getHeight();
        state.isIcon = frame.isIcon();
        windowStates.put(windowId, state);
    }

    public void restoreWindowState(JInternalFrame frame, String windowId) {
        WindowState state = windowStates.get(windowId);
        if (state != null) {
            frame.setBounds(state.x, state.y, state.width, state.height);
            if (state.isIcon) {
                SwingUtilities.invokeLater(() -> {
                    try {
                        if (frame.getDesktopPane() != null) {
                            frame.setIcon(true);
                        }
                    } catch (PropertyVetoException e) {
                        System.err.println("Не удалось восстановить свернутое состояние: " + e.getMessage());
                    }
                });
            }
        }
    }

    @SuppressWarnings("unchecked")
    public void loadFromFile() {
        File file = new File(CONFIG_FILE);
        if (!file.exists()) {
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            windowStates = (Map<String, WindowState>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Ошибка загрузки конфигурации: " + e.getMessage());
        }
    }

    public void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(CONFIG_FILE))) {
            oos.writeObject(windowStates);
        } catch (IOException e) {
            System.err.println("Ошибка сохранения конфигурации: " + e.getMessage());
        }
    }

    private static class WindowState implements Serializable {
        private static final long serialVersionUID = 1L;
        int x, y, width, height;
        boolean isIcon;
    }
}