package com.joker.interview.socket;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by hunter on 2018/3/10.
 */
public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private final RequestHandler requestHandler;

    public ClientHandler(Socket client, RequestHandler requestHandler) {
        this.clientSocket = client;
        this.requestHandler = requestHandler;
    }

    @Override
    public void run() {
        try (Scanner input = new Scanner(clientSocket.getInputStream())) {
            while (true) {
                String request = input.nextLine();
                if (request.equals("quit")) {
                    break;
                }
                System.out.println(String.format("Request from %s: %s",
                        clientSocket.getRemoteSocketAddress(),
                        request));
                String response = requestHandler.handler(request);
                clientSocket.getOutputStream().write(response.getBytes());
            }
        } catch (IOException e) {
            System.out.println("Caught exception: " + e);
            throw new RuntimeException(e);
        }
    }
}
