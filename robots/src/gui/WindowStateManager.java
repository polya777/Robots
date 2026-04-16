package gui;

import java.beans.PropertyVetoException;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JInternalFrame;
import javax.swing.SwingUtilities;

public class WindowStateManager {

    private Map<String, WindowStateStorage.WindowState> windowStates;
    private WindowStateStorage storage;

    public WindowStateManager() {
        windowStates = new HashMap<>();
        this.storage = new WindowStateStorage();
        this.windowStates = new HashMap<>();
    }

    public void saveWindowState(JInternalFrame frame, String windowId) {
        WindowStateStorage.WindowState state = new WindowStateStorage.WindowState();
        state.x = frame.getX();
        state.y = frame.getY();
        state.width = frame.getWidth();
        state.height = frame.getHeight();
        state.isIcon = frame.isIcon();
        windowStates.put(windowId, state);
    }

    public void restoreWindowState(JInternalFrame frame, String windowId) {
        WindowStateStorage.WindowState state = windowStates.get(windowId);
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

    public void loadFromFile() {
        windowStates = storage.loadFromFile();
    }

    public void saveToFile() {
        storage.saveToFile(windowStates);
    }

    public void setWindowId(JInternalFrame frame, String windowId) {
        frame.putClientProperty("windowId", windowId);
    }
}