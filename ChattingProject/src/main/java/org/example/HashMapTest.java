package org.example;

import java.net.Socket;
import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HashMapTest {

    public static void main(String[] args) {
        Map<Integer, List<User>> rooms = new HashMap<>();
            // key - roomId
            // value - 각 roomId에 위치하는 User들
        // 새 방 생성
        int roomId = rooms.size() + 1;
        rooms.put(roomId, new ArrayList<>());

        // 클라이언트를 새 방에 추가
//        rooms.get(roomId).add(new User(new Socket(9999), ))

        // List<String> -- List<User> 대응시켜 생성한다고 생각하면 쉽겠다!
        List<String> strList = new ArrayList<>();

        strList.add("minjiki2");
        strList.add("two");

        System.out.println(strList);

        System.out.println("*****************");
        // split ex
        String line = "    /join 1       ";
        String[] words = line.trim().split(" ");

        for (String word : words){
            System.out.print("time : ");
            System.out.println(word);
        }
    }
}

class User {
    private Socket socket; // 각 User(클라이언트)에 연결된 소켓 -> 이 소켓이 있어야 I/O 기능 수행 가능!
    private String nickName;

    public User(Socket socket, String nickName) {
        this.socket = socket;
        this.nickName = nickName;
    }

    public Socket getSocket() {
        return socket;
    }

    public String getNickName() {
        return nickName;
    }
}
