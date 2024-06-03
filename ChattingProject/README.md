# ChattingProject
TCP 채팅 프로젝트 </br>
구현 기간 : 2024.04.26 ~ 2024.04.29

<br>

## 1. 채팅 프로젝트 트러블슈팅 기록
[노션 링크](https://www.notion.so/ba7c93dfa68c44a08fe26d3417390c4d)

<br>

## 2. 프로젝트 전체 설명서
### 2-1. 채팅 전체 흐름
- 클라이언트는 123포트로 대기 중인 ChatServer에 접속합니다.
  
- 서버에 접속하면, 사용자는 닉네임을 입력받아 서버에 전송해줍니다.
- 서버는 사용자의 닉네임을 받고, "ㅇㅇㅇ닉네임의 사용자가 연결했습니다."라고 출력해줍니다.
- 클라이언트가 접속하면 서버는 사용자의 IP주소를 출력해줍니다.
  <img src="https://github.com/minjikimkim2222/ChattingProject/assets/96869808/c7531eda-80ed-44cb-b482-429a5d885a03" width="400" height="100"/>

- 클라이언트는 닉네임을 입력한 후부터 서버로부터 메세지를 한줄씩 받아 화면에 출력합니다.
- 사용자가 메세지를 입력하면 서버에 전송합니다.


### 2-2. 구현한 기능 및 개선할 점
1. 서버 연결 및 닉네임 설정
2. 메세지 수신 및 발신
3. 대화방 생성
4. 방 입장 및 나가기
5. 중복 닉네임 방지
6. 모든 채팅방 유저들에게 메세지 보내기 (broadcast)
7. 자신과 같은 채팅방 유저들에게만 메세지 보내기 (sendMessageToCurrentRoom)
8. 귓말 보내기 (whisper)
9. 명령어 모음

### 추가로 구현하고 싶은 기능
10. (채팅방 비밀번호 설정)
<br>

## 3. 명령어 모음

서버는 클라이언트가 접속할 때마다, 클라이언트에게 아래 명령어들을 전송해줍니다.
<img src="https://github.com/minjikimkim2222/ChattingProject/assets/96869808/fc8dbdf6-32fe-4e83-8788-5c095fab8f3c" width="400" height="150"/>

### /list
  - /list 명령을 입력하면, 서버는 생성된 모든 방의 목록을 출력합니다.
  - 채팅방이 존재하지 않는다면, "현재 존재하는 채팅방은 없습니다" 라는 메세지를 출력합니다.
  <img src="https://github.com/minjikimkim2222/ChattingProject/assets/96869808/b4423d9a-f7c6-4598-8486-9b24def12b17" width="300" height="100%"/>


### /create
  - 클라이언트가 "/create"를 입력하면, 서버는 새 방을 생성합니다.
  - 방 번호는 1부터 만들어지며, 생성 시 "방 번호 [방 번호]가 생성되었습니다."를 출력합니다. <br>
    (위 /list 명령어에서 확인해볼 수 있다.)

### /join [방번호]
- "/join [방 번호]"를 특정 방에 입장할 수 있습니다.
- 방에 입장하면 "ㅇㅇㅇ님이 [방번호] 방에 입장하셨습니다."를 출력합니다.
- 만일 없는 방 번호라면, "채팅방 id : [방번호]는 존재하지 않습니다."를 출력합니다.
<img src="https://github.com/minjikimkim2222/ChattingProject/assets/96869808/0035ee33-a249-42b6-8c8f-b1d8bde5fd8a" width="300" height="100%"/>


### /msg 
- "/msg [할말]"을 입력하면, 클라이언트가 join한 방 번호의 사람들에게만 메세지를 보내줍니다. (자기 자신 제외)
  <img src="https://github.com/minjikimkim2222/ChattingProject/assets/96869808/a238477c-fc1a-403c-b69e-9e11a46b9b29" width="300" height="100%"/>
  <img src="https://github.com/minjikimkim2222/ChattingProject/assets/96869808/771c17ec-aeda-4f90-84b2-6684707a9d7c" width="300" height="100%"/>
  <img src="https://github.com/minjikimkim2222/ChattingProject/assets/96869808/5498b354-72c5-43eb-b9c9-f741ec77050a" width="300" height="100%"/> </br>
  실행결과, 1번방에 있는 user1이 보낸 메세지는, 1번방에 있는 user3만 받을 수 있다.


### /broad [닉네임] [할말]
- "/broad [닉네임] [할말]"을 입력하면, 모든 채팅방의 유저들에게 메세지를 보낼 수 있습니다.
  <img src="https://github.com/minjikimkim2222/ChattingProject/assets/96869808/71dec9f0-8f71-4c55-9956-379ac7153482" width="300" height="100%"/>
  <img src="https://github.com/minjikimkim2222/ChattingProject/assets/96869808/6b895a01-d2f2-4290-9997-b2c7d1c23ce7" width="300" height="100%"/>
  <img src="https://github.com/minjikimkim2222/ChattingProject/assets/96869808/9d221a6a-08d3-4d27-adae-c801469c5fbb" width="300" height="100%"/>


  실행결과, user1(1번방)이 보낸 메세지를, user2(2번방)과 user3(1번방) 사람들 모두 메세지를 받은 것을 확인할 수 있었다. </br>
  (/msg 실행결과 이어서...)

### /whisper [닉네임] [메세지]
- "/whisper [닉네임] [메세지]"를 입력하면, 특정 사용자에게만 메세지를 전송할 수 있습니다.
- 이때, 자기자신에게는 귓말을 보낼 수 없고, 자신이 위치한 채팅방 내의 사람들에게만 귓말을 보낼 수 있습니다.
  <img src="https://github.com/minjikimkim2222/ChattingProject/assets/96869808/0d9ddb3a-3394-4060-ad57-aada77f38640" width="400" height="100%"/> </br> </br>
  <img src="https://github.com/minjikimkim2222/ChattingProject/assets/96869808/39ba6fb2-2bf6-484e-bf6e-1da13ad73c12" width="400" height="100%"/>


### /exit
- 방에서 "/exit"를 입력하면, "ㅇㅇㅇ 님이 방을 나갔습니다." 메세지를 출력해줍니다.
  <img src="https://github.com/minjikimkim2222/ChattingProject/assets/96869808/c18df454-9cd5-4682-b72a-b3b2eacd2ef5" width="300" height="100%"/>

### /bye
- 사용자가 "/bye"를 입력하면, 연결을 종료하고 프로그램을 종료합니다.
- 서버에서 "ㅇㅇㅇ 닉네임의 사용자가 연결을 끊었습니다. 접속을 종료합니다" 메세지를 출력해줍니다.
  <img src="https://github.com/minjikimkim2222/ChattingProject/assets/96869808/46016940-bbc8-44ae-91d0-9e69cc9c67ae" width="300" height="100%"/>
  <img src="https://github.com/minjikimkim2222/ChattingProject/assets/96869808/a7746c0f-e9ab-4591-bed7-e544d91c4f01" width="300" height="100%"/>



