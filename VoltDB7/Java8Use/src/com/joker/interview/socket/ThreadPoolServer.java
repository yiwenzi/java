package com.joker.interview.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by hunter on 2018/3/10.
 */
public class ThreadPoolServer {
    public static void main(String[] args) throws IOException {
        ExecutorService executor =  Executors.newFixedThreadPool(3);
        RequestHandler requestHandler = new RequestHandler();
        try (ServerSocket serverSocket = new ServerSocket(6666)) {
            System.out.println("Listening on " + serverSocket.getLocalSocketAddress());
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("The client ip is: " + clientSocket.getRemoteSocketAddress());
                executor.submit(new ClientHandler(clientSocket, requestHandler));
            }
        }
    }
}
