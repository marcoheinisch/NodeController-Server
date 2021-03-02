package io.github.marcoheinisch;

import java.awt.*;
import java.awt.event.*;


public class Main {
    private static NodeMcuController nmc;

    public static void main(String arg[]) {
        if (!SystemTray.isSupported()) {
            System.out.println("SystemTray is not supported");
            return;
        }
        final SystemTray tray = SystemTray.getSystemTray();

        // Scale Image manually (trayIcon.setImageAutoSize(true) -> bad results)
        Image trayImage = Toolkit.getDefaultToolkit().getImage("res/light_bold.png");
        Dimension trayIconSize = tray.getTrayIconSize();
        trayImage = trayImage.getScaledInstance(trayIconSize.width, trayIconSize.height, Image.SCALE_SMOOTH);
        TrayIcon trayIcon = new TrayIcon(trayImage, "NodeController");


        // Create a pop-up menu components
        final PopupMenu popup = createPopupMenu();
        trayIcon.setPopupMenu(popup);
        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            System.out.println("TrayIcon could not be added.");
        }
        System.out.println("Successful built!");
    }

    protected static PopupMenu createPopupMenu() {
        final PopupMenu popup = new PopupMenu();

        MenuItem aboutItem = new MenuItem("NodeController");
        aboutItem.addActionListener((event) -> {
            SettingsDialog dialog = new SettingsDialog();
            dialog.pack();
            dialog.showDialog();
        });
        popup.add(aboutItem);

        popup.addSeparator();

        String[] OPTIONS = SaveVar.getArray("Buttons");

        for(int i=0; i<OPTIONS.length;i++){
            CheckboxMenuItem menuItem = new CheckboxMenuItem(OPTIONS[i]);
            int finalI = i;
            menuItem.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    set(finalI,menuItem.getState());
                }
            });
            popup.add(menuItem);
        }

        return popup;
    }

    static void set(int nr, boolean status){
        int status_byte = status ? 1 : 0;
        System.out.println("Get an order: "+nr+"z"+status_byte);
        nmc = new NodeMcuController();
        var response = nmc.sendOrder("switch",nr+"z"+status_byte);
        System.out.println(response);
    }
}