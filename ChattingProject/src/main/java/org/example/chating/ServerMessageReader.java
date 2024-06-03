package org.example.chating;

import java.io.BufferedReader;
import java.io.IOException;

public class ServerMessageReader implements Runnable {
    private BufferedReader in;
    public ServerMessageReader(BufferedReader in) {
        this.in = in;
    }

    @Override
    public void run() {
        // 서버로부터 메세지를 읽어, 화면에 출력하는 별도의 쓰레드
        try {
            String serverLine;

            while ((serverLine = in.readLine()) != null){
                System.out.println(serverLine); // 서버로부터 받은 메세지 출력
            }
        } catch (IOException e) {
            System.out.println("Server connection was closed");
            throw new RuntimeException(e);
        }
    }
}
