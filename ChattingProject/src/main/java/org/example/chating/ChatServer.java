package org.example.chating;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

/*
    main 쓰레드
        - 다수의 클라이언트가 접속받는다.

    UserManager 쓰레드
        - 각 연결된 클라이언트와 채팅방 관리를 해주는 쓰레드

 */
public class ChatServer {
    private static Map<Integer, List<User>> rooms = new HashMap<>();

    public static void main(String[] args) {
        try {
            int portNumber = 123;

            ServerSocket serverSocket = new ServerSocket(123);
            System.out.println("서버가 포트번호 : " + portNumber + "에 연결되었습니다.");

            while (true) {
                Socket clientSocket = serverSocket.accept();

                // UserManager 쓰레드 - 각 연결된 클라이언트와 채팅방 관리를 해주는 전반적인 쓰레드
                UserManager userManager = new UserManager(clientSocket);
                userManager.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Map<Integer, List<User>> getRooms() {
        return rooms;
    }
}

class UserManager extends Thread {
    private Socket clientSocket;
    private Map<Integer, List<User>> rooms;
    private String nickName;
    private PrintWriter out;
    private BufferedReader in;
    private int currentRoomId; // 현재 사용자가 위치한 채팅방 ID 저장 변수
    private static List<String> temporaryUserList = new ArrayList<>(); // 임시 사용자 리스트 - 아이디 중복 여부 체크

    public UserManager(Socket clientSocket) {
        this.clientSocket = clientSocket;
        this.rooms = ChatServer.getRooms();

        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);

            // 연결된 클라이언트로부터 사용자의 닉네임을 받아, 메세지와 사용자 IP주소 출력
            // 여기서 입출력통로인 in, out 이 초기화되었다!
            setNickname();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            commandCall(); // 모든 명령어 보여주는 함수

            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                processCommand(inputLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setNickname() throws IOException {
        String inputNickName;

        while (true) {
            inputNickName = in.readLine();

            if (!isNicknameAlreadyUsed(inputNickName)) {
                break;
            } else {
                out.println("이미 사용중인 닉네임입니다. 다른 닉네임을 선택해주세요!");
            }
        }
        temporaryUserList.add(inputNickName);
        this.nickName = inputNickName;
        System.out.println(nickName + "닉네임의 사용자가 연결했습니다.");
        System.out.println("IP주소 : " + clientSocket.getInetAddress().getHostAddress());
    }

    private void processCommand(String inputLine) {
        String[] words = inputLine.trim().split(" ");
        String command = words[0];

        switch (command.toLowerCase()) {
            case "/list":
                showRoomList();
                break;
            case "/create":
                createRoom();
                break;
            case "/join":
                joinRoom(words);
                break;
            case "/exit":
                exitRoom();
                break;
            case "/bye":
                closeConnection();
                break;
            case "/msg": // "/msg " 제외한 부분 문자열
                sendMessageToCurrentRoom(inputLine.substring(5));
                break;
            case "/broad": // "/broad " 제외한 부분 문자열
                broadcastMsg(inputLine.substring(7));
                break;
            case "/whisper": // /whisper [특정 사용자 닉네임] [할말]
                whisper(inputLine.substring(10 + words[1].length()), words[1]);
                break;
            default:
                out.println("알 수 없는 명령어입니다. 다시 입력해주세요");
                commandCall();
        }
    }

    private void commandCall() {
        out.println("방 목록 보기 : /list");
        out.println("방 생성 : /create");
        out.println("방 입장 : /join [방 번호]");
        out.println("내 채팅방 사람들에게만 메세지 보내기 : /msg");
        out.println("모든 사람들에게 메세지 보내기 : /broad [닉네임] [할말]");
        out.println("귓말 보내기 : /whisper [닉네임] [메세지]");
        out.println("방 나가기 : /exit");
        out.println("접속종료 : /bye");
    }

    // 만들어진 모든 roomId list 출력
    private void showRoomList() {
        synchronized (rooms) {
            if (rooms.isEmpty()) {
                out.println("현재 존재하는 채팅방은 없습니다.");
            } else {
                out.println("현재 생성된 모든 방의 목록 : ");
                for (Integer roomId : rooms.keySet()) {
                    out.println("방 번호 : " + roomId);
                }
            }
        }
    }

    // 방 생성
    private void createRoom() {
        synchronized (rooms) {
            int roomId = rooms.size() + 1;
            rooms.put(roomId, new ArrayList<>());
            out.println("방 번호 [" + roomId + "]가 생성되었습니다.");
        }
    }

    // 특정 사용자가, 특정 방에 들어가게끔 하기
    private void joinRoom(String[] words) {
        int roomId = Integer.parseInt(words[1]); // 방 id

        synchronized (rooms) {
            if (rooms.containsKey(roomId)) {
                User user = new User(out, nickName);
                List<User> users = rooms.get(roomId);
                users.add(user);
                this.currentRoomId = roomId;
                out.println(nickName + "님이 [ " + currentRoomId + " ]방에 입장하셨습니다."); // 내 out통로에도 출력해주고
                sendMessageToCurrentRoom(nickName + "님이 [ " + currentRoomId + " ]방에 입장하셨습니다.");
                //  나랑 같은 채팅방 사람들에게도 알리고..
            } else {
                out.println("채팅방 id : " + roomId + "는 존재하지 않습니다.");
            }
        }
    }

    // user가 방을 나가고, 방에 아무도 없다면 해당 방 삭제하기
    private void exitRoom() {
        synchronized (rooms) {
            boolean isUserLeftRoom = false; // 사용자가 방을 나갔는지 여부를 저장할 플래그 변수

            for (List<User> userList : rooms.values()) {
                Iterator<User> iterator = userList.iterator();

                while (iterator.hasNext()) {
                    User user = iterator.next();

                    if (user.getNickName().equals(nickName)) {
                        iterator.remove();
                        isUserLeftRoom = true;
                        break;
                    }
                }
                if (isUserLeftRoom) break;
            }

            if (isUserLeftRoom) {
                String msg = nickName + " 님이 방을 나갔습니다.";
                out.println(msg);
                sendMessageToCurrentRoom(msg);
                rooms.entrySet().removeIf(entry -> entry.getValue().isEmpty());
            } else {
                out.println("현재 방에 입장 중이 아니거나, 해당 " + nickName + " 사용자가 존재하지 않습니다.");
            }
        }
    }

    // 접속 종료
    private void closeConnection() {
        System.out.println(nickName + "닉네임 사용자가 연결을 끊었습니다.");
        try {
            in.close();
            out.close();
            clientSocket.close();
            System.out.println("접속을 종료합니다.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 내가 위치한 채팅방 사람들에게만 메세지 보내기
    private void sendMessageToCurrentRoom(String msg) {
        synchronized (rooms) {
            List<User> users = rooms.get(currentRoomId); // 현재 객체가 위치한 roomId

            if (users != null) {
                for (User user : users) {
                    if (!user.getNickName().equals(nickName)) { // 객체 자기자신 제외
                        user.sendMessage(nickName + " : " + msg);
                        user.getOut().flush();
                    }
                }
            }
        }
    }

    // 모든 채팅방, 모든 유저에게 메세지 보내기
    private void broadcastMsg(String msg) {
        for (List<User> users : rooms.values()) {
            for (User user : users) {
                user.sendMessage(nickName + " : " + msg);
                user.getOut().flush();
            }
        }
    }

    // 귓말 기능
        // 나자신에게는 귓말 못 보냄
        // 없는 유저에게는 귓말 못 보냄
        // 나랑 '같은' 채팅방에 위치한 유저들에게만, 귓말 보낼 수 있음..
    private void whisper(String msg, String to) {
        synchronized (rooms) {
            List<User> users = rooms.get(currentRoomId);

            if (users != null) {
                boolean userFound = false; // 대상 사용자를 찾았는지 여부

                for (User user : users) {
                    if (user.getNickName().equals(nickName)) // 나 자신에게는 귓말을 보내지 않는다.
                        continue;

                    if (user.getNickName().equals(to)) { // 내가 귓말 보낼 대상자랑 일치한다면...
                        PrintWriter pw = user.getOut();

                        if (pw != null) {
                            pw.println(nickName + " : " + msg);
                            user.getOut().flush();
                            userFound = true;
                            break;
                        } else {
                            out.println(to + "닉네임을 가진 유저가 " + currentRoomId + "번 방에 존재하지 않습니다.");
                            return;
                        }
                    }
                }
                if (!userFound) {
                    out.println(to + "닉네임을 가진 유저가 " + currentRoomId + "번 방에 존재하지 않습니다.");
                }
            } else {
                out.println(to + "닉네임을 가진 유저가 " + currentRoomId + "번 방에 존재하지 않습니다.");
            }
        }
    }

    // 중복된 닉네임이 있는지 여부 체크
    private boolean isNicknameAlreadyUsed(String nickName) {
        for (String str : temporaryUserList) {
            if (str.equals(nickName)) {
                return true;
            }
        }
        return false;
    }
}