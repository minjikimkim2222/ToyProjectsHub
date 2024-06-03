package org.example.test;

import java.util.*;

public class HashMapEtc {
    public static void main(String[] args) {
        // Key - clientId, Value - client 객체
        Map<Integer, List<Client>> maps = new HashMap<>();

        maps.put(1, new ArrayList<>());
        maps.put(2, new ArrayList<>());

        maps.get(1).add(new Client("user1", 10));
        maps.get(1).add(new Client("user2", 20));

        maps.get(2).add(new Client("user3", 30));

        // 모든 클라이언트들에게 메세지 출력
        // values는 해당 map의 value 목록을 Collection 형태로 리턴

        Collection<List<Client>> values = maps.values();

        System.out.println(values);

        // for loops
        for (List<Client> clients : values){
            for (Client client : clients){
                System.out.println(client.getUserName());
            }
        }


    }
}

class Client {
    private String userName;
    private int age;

    public Client(String userName, int age) {
        this.userName = userName;
        this.age = age;
    }

    public String getUserName() {
        return userName;
    }

    public int getAge() {
        return age;
    }
}
