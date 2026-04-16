package gui;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class WindowStateStorage {
    private static final String CONFIG_FILE = System.getProperty("user.home") +
            File.separator + ".robots_program_config.ser";

    public void saveToFile(Map<String, WindowState> windowStates) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(CONFIG_FILE))) {
            oos.writeObject(windowStates);
            System.out.println("Сохранено в файл: " + windowStates.size() + " состояний");
        } catch (IOException e) {
            System.err.println("Ошибка сохранения конфигурации: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public Map<String, WindowState> loadFromFile() {
        File file = new File(CONFIG_FILE);
        if (!file.exists()) {
            System.out.println("Файл конфигурации не найден");
            return new HashMap<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (Map<String, WindowState>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Ошибка загрузки конфигурации: " + e.getMessage());
            return new HashMap<>();
        }
    }

    static class WindowState implements Serializable {
        private static final long serialVersionUID = 1L;
        int x, y, width, height;
        boolean isIcon;
    }
}
