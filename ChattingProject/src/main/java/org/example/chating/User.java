package org.example.chating;

import java.io.PrintWriter;
import java.net.Socket;

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
        //out.flush(); // 얘를 추가해봄!
    }
}