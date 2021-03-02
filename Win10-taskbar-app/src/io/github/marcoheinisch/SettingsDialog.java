package io.github.marcoheinisch;

import javax.swing.*;
import java.awt.event.*;

public class SettingsDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField ip_input;
    private JTextArea buttons_input;
    private boolean result = true;

    public SettingsDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        ip_input.setText((String) SaveVar.getThis("Ip"));
        buttons_input.setText((String) SaveVar.getThis("Buttons"));

    }

    private void onOK() {
        SaveVar.saveThis("Ip", ip_input.getText());
        SaveVar.saveThis("Buttons", buttons_input.getText());
        dispose();
    }

    private void onCancel() {
        dispose();
    }

    public boolean showDialog() {
        setVisible(true);
        return result;
    }

}
