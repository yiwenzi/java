package com.joker.interview.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by hunter on 2018/3/10.
 */
public class Server {
    public static void main(String[] args) throws IOException {
        RequestHandler requestHandler = new RequestHandler();
        try (ServerSocket serverSocket = new ServerSocket(6666)) {
            System.out.println("Listening on " + serverSocket.getLocalSocketAddress());
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("The client ip is: " + clientSocket.getRemoteSocketAddress());
                new ClientHandler(clientSocket, requestHandler);
            }
        }
    }
}
