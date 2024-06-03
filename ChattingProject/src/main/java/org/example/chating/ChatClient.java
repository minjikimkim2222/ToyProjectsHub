package org.example.chating;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/*
ChatClient - 2개의 쓰레드
    main 쓰레드
        - 서버에게 닉네임 전송 (write)
        - 키보드로부터 메세지를 입력받아, 서버로 전송 (write)

    ServerMessageReader 쓰레드
        - 서버로부터 받은 메세지를 '한줄씩' 읽고 + 화면에 출력 (read)
 */
public class ChatClient {
    public static void main(String[] args) {
        String hostName = "localhost"; // 서버가 실행 중인 호스트 이름 또는 IP주소
        int portNumber = 123; // 서버와 동일한 포트번호 사용

        Socket socket = null;
        PrintWriter out = null;
        BufferedReader in = null;

        try {
            socket = new Socket(hostName, portNumber);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            Scanner keyboard = new Scanner(System.in);

            System.out.println("Enter your nickName");

            // 서버에 닉네임 전송 (main 쓰레드 1번 - write)
            String nickName = keyboard.nextLine();
            out.println(nickName);

            // 서버로부터 메세지를 읽어, 화면에 출력하는 별도의 쓰레드 (ServerMessageReader 쓰레드 1번 - read)
            Thread readFromServer = new Thread(new ServerMessageReader(in));
            readFromServer.start();

            // 키보드로부터 메세지를 입력받아, 서버로 전송 (main 쓰레드 2번 - write)
            String userInput;

            while (true){
                userInput = keyboard.nextLine();

                // '/bye'를 입력하면 클라이언트를 종료시킨다.
                if ("./bye".equalsIgnoreCase(userInput)){
                    out.println(userInput); // 서버에게 './bye' 메세지 보내고
                    break;
                }

                out.println(userInput);
            } // while

            // 클라이언트와 서버는 명시적으로 close를 한다.
            // close를 할 경우, 상대방쪽의 readLine()이 null을 반환한다. 이 값을 이용해 접속이 종료된 것을 알 수 있습니다.
            in.close();
            out.close();
            socket.close();
        } catch (IOException e){
            System.out.println("Exception caught when trying to connect to " + hostName + " on port " + portNumber);
            e.printStackTrace();
        }
    }
}
