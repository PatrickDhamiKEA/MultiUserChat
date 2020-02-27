package com.muc;

import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.net.Socket;
import java.util.List;


public class ServerWorker extends Thread {

    private final Socket clientSocket;
    private final Server server;
    private String login = null;
    private OutputStream outputStream;
    private InputStream inputStream;

    public ServerWorker(Server server, Socket clientSocket) {
        this.server = server;
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            handleClientSocket();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void handleClientSocket() throws IOException, InterruptedException {
        this.inputStream = clientSocket.getInputStream();
        this.outputStream = clientSocket.getOutputStream();

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ( (line = reader.readLine()) != null) {
            String[] tokens = StringUtils.split(line);
            if (tokens != null && tokens.length > 0) {
                String cmd = tokens[0];
                //TODO skal laves til QUIT protocol
                if ("logoff".equals(cmd) || "quit".equalsIgnoreCase(cmd)) {
                    handleLogoff();
                    break;
                    //TODO skal enten laves til JOIN protocol eller tilføjes JOIN protocol
                    //TODO mangler IP/port
                } else if ("login".equalsIgnoreCase(cmd)) {
                    handleLogin(outputStream, tokens);
                }else {
                    String[] tokensMsg = StringUtils.split(line, null, 2);
                    handleMessage(tokensMsg);
                }
            }
        }

        clientSocket.close();
    }

    private void handleMessage(String[] tokens) throws IOException {
        String body = tokens[1];;

        List<ServerWorker> workerList = server.getWorkerList();
        for(ServerWorker worker : workerList) {
            String outMsg = login + " " + body + "\n";
            worker.send(outMsg);
                }
            }

    private void handleLogoff() throws IOException {
        server.removeWorker(this);
        List<ServerWorker> workerList = server.getWorkerList();

        // send other online users current user's status
        String onlineMsg = "offline " + login + "\n";
        for(ServerWorker worker : workerList) {
            if (!login.equals(worker.getLogin())) {
                worker.send(onlineMsg);
            }
        }
        clientSocket.close();
    }

    public String getLogin() {
        return login;
    }

    private void handleLogin(OutputStream outputStream, String[] tokens) throws IOException {
        if (tokens.length == 3) {
            String login = tokens[1];
            //TODO skal fjernes da der ikke skal bruges password
            String password = tokens[2];

            //TODO mulighed for at lave egne brugere, så det ikke er predefined
            if ((login.equals("Patrick") && password.equals("Patrick")) || (login.equals("Robin") && password.equals("Robin")) ) {
                String msg = "ok login\n";
                outputStream.write(msg.getBytes());
                this.login = login;
                System.out.println("User logged in succesfully: " + login);

                List<ServerWorker> workerList = server.getWorkerList();

                //TODO skal vises som LIST protocol
                for(ServerWorker worker : workerList) {
                    if (worker.getLogin() != null) {
                        if (!login.equals(worker.getLogin())) {
                            String msg2 = "online " + worker.getLogin() + "\n";
                            send(msg2);
                        }
                    }
                }

                String onlineMsg = "online " + login + "\n";
                for(ServerWorker worker : workerList) {
                    if (!login.equals(worker.getLogin())) {
                        worker.send(onlineMsg);
                    }
                }
            } else {
                String msg = "error login\n";
                outputStream.write(msg.getBytes());
                System.err.println("Login failed for " + login);
            }
        }
    }

    private void send(String msg) throws IOException {
        if (login != null) {
            try {
                outputStream.write(msg.getBytes());
            } catch(Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
