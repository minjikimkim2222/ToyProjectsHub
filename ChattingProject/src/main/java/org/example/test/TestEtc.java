package org.example.test;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class TestEtc {
    public static void main(String[] args) {
        // sendMessageToMessage test
        String nickName = "user01";
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        User user = new User(out, nickName);

        // sendMessage test
        user.sendMessage("is it ok? to console");
        out.flush();

    }
}

class User {
    private String nickName;

    private PrintWriter out;

    public User(PrintWriter out, String nickName) {
        this.out = out;
        this.nickName = nickName;

    }

    public PrintWriter getOut() {
        return out;
    }

    public String getNickName() {
        return nickName;
    }

    public void sendMessage(String msg){
        out.println(msg);
    }
}
