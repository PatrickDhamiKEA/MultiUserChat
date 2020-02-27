package com.muc;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;


public class LoginWindow extends JFrame {
    private final ChatClient client;
    JTextField loginField = new JTextField();
    JPasswordField passwordField = new JPasswordField();
    JButton loginButton = new JButton("Login");

    public LoginWindow() {
        super("Login");

        this.client = new ChatClient("localhost", 8818);
        client.connect();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.add(loginField);
        //TODO fjernes
        p.add(passwordField);
        p.add(loginButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doLogin();
            }
        });

        getContentPane().add(p, BorderLayout.CENTER);

        pack();

        setVisible(true);
    }

    private void doLogin() {
        String login = loginField.getText();
        //TODO fjernes
        String password = passwordField.getText();

        try {
            //TODO fjern password
            if (client.login(login, password)) {
                MessagePane messagePane = new MessagePane(client, login);

                JFrame f = new JFrame("Logged in as: " + login);
                f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                f.setSize(500, 500);
                f.getContentPane().add(messagePane, BorderLayout.CENTER);
                f.setVisible(true);
            } else {
                // show error message
                JOptionPane.showMessageDialog(this, "Invalid login/password.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        LoginWindow loginWin = new LoginWindow();
        loginWin.setVisible(true);
    }
}
