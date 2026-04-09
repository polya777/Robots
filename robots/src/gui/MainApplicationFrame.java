package gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.JOptionPane;

import log.Logger;

public class MainApplicationFrame extends JFrame
{
    private final JDesktopPane desktopPane = new JDesktopPane();
    private WindowStateManager stateManager;

    private static final String LOG_WINDOW_ID = "logWindow";
    private static final String GAME_WINDOW_ID = "gameWindow";

    private LogWindow logWindow;
    private GameWindow gameWindow;

    public MainApplicationFrame() {
        stateManager = new WindowStateManager();
        stateManager.loadFromFile();

        initializeFrame();
        createAndAddWindows();
        setupMenuBar();
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closeApplication();
            }
        });
    }

    private void initializeFrame() {
        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
                screenSize.width  - inset*2,
                screenSize.height - inset*2);
        setContentPane(desktopPane);
    }

    private void createAndAddWindows() {
        logWindow = createLogWindow();
        addWindow(logWindow, LOG_WINDOW_ID);

        gameWindow = createGameWindow();
        addWindow(gameWindow, GAME_WINDOW_ID);
    }

    private GameWindow createGameWindow() {
        GameWindow gameWindow = new GameWindow();
        gameWindow.setSize(400, 400);
        return gameWindow;
    }

    protected LogWindow createLogWindow()
    {
        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource());
        configureLogWindow(logWindow);
        Logger.debug("Протокол работает");
        return logWindow;
    }

    private void configureLogWindow(LogWindow logWindow) {
        logWindow.setLocation(10, 10);
        logWindow.setSize(300, 800);
        setMinimumSize(logWindow.getSize());
        logWindow.pack();
    }
    
    protected void addWindow(JInternalFrame frame, String windowId)
    {
        desktopPane.add(frame);
        stateManager.restoreWindowState(frame, windowId);
        frame.setVisible(true);
    }

    private void setupMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        menuBar.add(createLookAndFeelMenu());
        menuBar.add(createTestMenu());
        menuBar.add(createExitMenu());

        setJMenuBar(menuBar);
    }

    private void closeApplication() {
        saveAllWindowsState();
        UIManager.put("OptionPane.yesButtonText"   , "Да"    );
        UIManager.put("OptionPane.noButtonText"    , "Нет"   );

        int result = JOptionPane.showConfirmDialog(
                this,
                "Вы действительно хотите выйти из приложения?",
               "Подтверждение выхода",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );


        if (result == JOptionPane.YES_OPTION) {
            stateManager.saveToFile();
            System.exit(0);
        }
    }

    private JMenu createLookAndFeelMenu() {
        JMenu menu = new JMenu("Режим отображения");
        menu.setMnemonic(KeyEvent.VK_V);
        menu.getAccessibleContext().setAccessibleDescription(
                "Управление режимом отображения приложения");

        menu.add(createSystemLookAndFeelItem());
        menu.add(createCrossPlatformLookAndFeelItem());

        return menu;
    }

    private JMenuItem createSystemLookAndFeelItem() {
        JMenuItem item = new JMenuItem("Системная схема", KeyEvent.VK_S);
        item.addActionListener(event -> {
            setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            this.invalidate();
        });
        return item;
    }

    private JMenuItem createCrossPlatformLookAndFeelItem() {
        JMenuItem item = new JMenuItem("Универсальная схема", KeyEvent.VK_S);
        item.addActionListener(event -> {
            setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            this.invalidate();
        });
        return item;
    }

    private JMenu createTestMenu() {
        JMenu menu = new JMenu("Тесты");
        menu.setMnemonic(KeyEvent.VK_T);
        menu.getAccessibleContext().setAccessibleDescription(
                "Тестовые команды");

        menu.add(createAddLogMessageItem());

        return menu;
    }

    private JMenuItem createAddLogMessageItem() {
        JMenuItem item = new JMenuItem("Сообщение в лог", KeyEvent.VK_S);
        item.addActionListener(event -> {
            Logger.debug("Новая строка");
        });
        return item;
    }

    private JMenu createExitMenu() {
        JMenu menu = new JMenu("Закрытие приложения");
        menu.setMnemonic(KeyEvent.VK_E);
        menu.getAccessibleContext().setAccessibleDescription(
                "Закрыть приложение");

        menu.add(createExitMenuItem());

        return menu;
    }

    private JMenuItem createExitMenuItem() {
        JMenuItem item = new JMenuItem("Выход", KeyEvent.VK_X);
        item.addActionListener(event -> {
            closeApplication();
        });
        return item;
    }

    private void setLookAndFeel(String className)
    {
        try
        {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(this);
        }
        catch (ClassNotFoundException | InstantiationException
            | IllegalAccessException | UnsupportedLookAndFeelException e)
        {
            // just ignore
        }
    }

    private void saveAllWindowsState() {
        if (logWindow != null) {
            stateManager.saveWindowState(logWindow, LOG_WINDOW_ID);
        }

        if (gameWindow != null) {
            stateManager.saveWindowState(gameWindow, GAME_WINDOW_ID);
        }
    }
}
