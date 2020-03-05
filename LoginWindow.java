package com.muc;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;


public class LoginWindow extends JFrame {
    private final ChatClient client;
    JTextField loginField = new JTextField(); //GUI
    JButton loginButton = new JButton("Login"); //GUI

    public LoginWindow() {
        //JFrames constructor
        super("Login");

        this.client = new ChatClient("localhost", 8818);
        client.connect();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //GUI terminator

        JPanel p = new JPanel(); //GUI
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS)); //GUI
        p.add(loginField); //GUI
        //TODO fjernes
        p.add(loginButton); //GUI

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doLogin();
            }
        });

        getContentPane().add(p, BorderLayout.CENTER); //GUI

        pack(); //GUI

        setVisible(true); //GUI
    }

    private void doLogin() {
        String login = loginField.getText(); //GUI
        try {
            if (client.login(login)) {
                MessagePane messagePane = new MessagePane(client, login);

                JFrame f = new JFrame("Logged in as: " + login);//GUI
                f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);//GUI
                f.setSize(500, 500);//GUI
                f.getContentPane().add(messagePane, BorderLayout.CENTER);//GUI
                f.setVisible(true);//GUI


            } else {
                // show error message
                JOptionPane.showMessageDialog(this, "Invalid login");//GUI
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
