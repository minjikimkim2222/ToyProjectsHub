# Bulletin_board_Project
Spring Boot - DB 연동 CRUD 게시판 만들기 <br>
구현기간 : 2024.05.23 ~ 2024.05.25
<br><br>
##### IDE : Intellj
##### Framework : SpringBoot
##### dependencies : Spring Boot Devtools, Lombok, Spring Web, Spring Data JDBC, Thymeleaf, SpringBoot validation
##### Language : Java 21, HTML 5, CSS, 부트스트랩 버전 5.3.3
##### DB : mysql 8.0.33 version

## 1. 게시판 프로젝트 트러블슈팅 기록
[노션 링크](https://www.notion.so/CRUD-JAR-68a21b9201ff4261b4f6ee807ae542fc)

<br>

## 2. 프로젝트 전체 설명서
### 기본구현기능
- 글 목록 보기 (```/list```)
  - 최신 글부터 보여짐
  - ID, 제목, 이름, 등록일(YYYY/MM/DD) 형식으로 목록이 보여짐
  - 페이징 처리
    
- 글 상세 조회 (```/view?id=아이디```)
  - 암호는 보여지면 안됨
  - 글 등록일은 YYYY/MM/DD hh24:mi 형식으로 보여짐
    
- 글 등록 (```/writeform```)
  - 이름, 제목, 암호, 본문을 입력
  - 등록일, ID는 자동으로 저장
  - form 유효성 검사 기능 추가

- 수정 (```/updateform```)
  - 이름, 제목, 본문을 수정
  - 글 등록시 입력했던 암호를 입력을 제대로 입력해야, 수정 성공
  - 수정일은 자동으로 저장
    
- 삭제 (```/deleteform```)
  - 암호는 글 등록시 입력했던 암호를 입력해야함
  - 글 등록시 입력했던 암호를 입력을 제대로 입력해야, 삭제 성공

---

### 추가기능
- 좋아요 기능 추가
- 좋아요/최신순 정렬 기능 추가
<br>

### 2-2. 요청/응답 처리 클래스 & 시퀀스 다이어그램

![image](https://github.com/minjikimkim2222/Bulletin_board_Project/assets/96869808/32987604-6d99-485c-a936-d0c746b947bf)

<br>

## 3. URL 구조 및 실행

### 1. 게시글 목록 보기 (`/list`)
```
- URL : /list, /list?page=2 ..
- 기능 :
    1. 게시글 목록을 '페이지별'로 보여준다.
    2. 'page' 파라미터가 없으면, 기본적으로 1페이지부터 보여준다.
    3. 각 페이지는 최신 글부터 보여지며, 페이징 처리가 적용되어있다.
    4. 하단에는 페이지 네비게이터가 있어, 다른 페이지로 쉽게 이동할 수 있다.
    5. 각 게시글은 ID, 제목, 이름, 등록일(YYYY/MM/DD 형식)로 목록이 구성된다.

```
<img src="https://github.com/minjikimkim2222/Bulletin_board_Project/assets/96869808/8221b2b5-499c-4d62-986c-e39a6beecc8c" width="500" height="100%"/>

<br>

### 2. 게시글 글 상세조회 (`/view?id=아이디`)
```
- URL : /view?id=아이디
- 기능 :
    1. 특정 게시글의 상세 내용을 보여준다.
    2. list/edit/delete 링크를 제공해, 해당 기능을 수행할 수 있는 페이지로 이동할 수 있다.
    3. 게시글의 등록일은 YYYY/MM/DD hh24:mi 형식으로 포멧팅되어있다.
    4. 게시글의 암호는 보이지 않는다.
```
<img src="https://github.com/minjikimkim2222/Bulletin_board_Project/assets/96869808/ceaf42f4-a242-4159-a5f9-3e65f1e63764" width="500" height="100%"/>

<br>

### 3. 게시글 등록 폼 (`/writeform`)
```
- URL : /writeform
- 기능 :
    1. 특정 게시글을 등록하기 위한 폼을 제공한다.
    2. 사용자는 이름,제목,내용,암호를 입력하고, submit 버튼을 눌러 등록을 요청한다.
    3. 모든 내용이 잘 입력된 경우 /list로 리다이렉트되지만, 유효성 검사에 실패한 경우 다시 write.html 뷰로 보내진다.
    4. 폼 유효성 검사를 추가하고, 유효성 검사를 실패한 경우 에러메세지를 띄워준다.
```
<img src="https://github.com/minjikimkim2222/Bulletin_board_Project/assets/96869808/194ff63d-5230-459b-884b-ad92d9e8e1a5" width="200" height="80%"/>
<img src="https://github.com/minjikimkim2222/Bulletin_board_Project/assets/96869808/18682a09-7c8f-439e-95fc-66b3533172b3" width="200" height="80%"/>


<br>

### 4. 게시글 삭제 폼 (`/deleteform?id=아이디`)
```
- URL : /deleteform?id=아이디
- 기능 :
    1. 특정 게시글을 삭제하기 위한 폼을 제공한다.
    2. 사용자가 암호를 입력하고, 올바른 암호일경우에만 삭제기능이 수행된다.
    3. 만일 비밀번호가 틀렸다면, 다시 입력하라는 에러메세지가 뜬다.
```
<img src="https://github.com/minjikimkim2222/Bulletin_board_Project/assets/96869808/289968e8-0497-497e-9742-39b383f63216" width="200" height="80%"/>
<img src="https://github.com/minjikimkim2222/Bulletin_board_Project/assets/96869808/8447f61c-cc62-4ff5-b79d-6c76356f135b" width="200" height="80%"/>

<br>

### 5. 게시글 수정 폼 (`/updateform?id=아이디`)
```
- URL : /updateform?id=아이디
- 기능 :
    1. 특정 게시글을 수정하기 위한 폼을 제공한다.
    2. 이름,제목,본문,암호 필드를 포함하며, 사용자는 이를 수정할 수 있다.
    3. update 버튼을 눌러 수정요청을 보내고, 수정이 완료되면 /list로 리다이렉트된다.
    4. 사용자가 입력한 암호가 올바른 경우에만, 수정기능이 수행된다.
    5. 만일 비밀번호가 틀렸다면, 다시 입력하라는 에러메세지가 뜬다.
```
<img src="https://github.com/minjikimkim2222/Bulletin_board_Project/assets/96869808/a8418c5a-9add-4646-8b99-fd3c58fb8455" width="200" height="80%"/>
<img src="https://github.com/minjikimkim2222/Bulletin_board_Project/assets/96869808/47846d3f-28d2-4b2c-bc87-d352629db190" width="200" height="80%"/>

<br>

### 6. 좋아요 기능

```
- URL : /like?id=[아이디]
- 기능 :
    1. view.html 뷰에서 좋아요 버튼을 누르면, 폼이 POST 요청을 /like?id=[아이디] URL로 보낸다.
    2. 컨트롤러에서, 해당 id 게시글의 좋아요 필드를 증가시킨 뒤, /view?id=[아이디] 로 redirect된다.
```
<img src="https://github.com/minjikimkim2222/Bulletin_board_Project/assets/96869808/6ada0848-098b-4714-b898-a266a42fb207" width="500" height="100%"/>

<br>

### 7. 좋아요/최신순 정렬 기능

```
/list 컨트롤러 리팩토링

- URL : /list?sort=lastest, /list?sort=likes
- 기능 :
    1. list.html 뷰에서 사용자가 최신순,좋아요순 버튼을 누르면, 각각 다음의 url이 보내진다.
        /list?sort=latest
        /list?sort=likes
    2. 컨트롤러에서, 최신순 / 정렬순 기준에 따라 정렬된 화면을 보여준다.
        디폴트로 최신순 정렬이 된다.
```
<img src="https://github.com/minjikimkim2222/Bulletin_board_Project/assets/96869808/1ea98400-87e0-4c2f-acfa-3a6362494677" width="350" height="100%"/>
<img src="https://github.com/minjikimkim2222/Bulletin_board_Project/assets/96869808/35798453-05ad-4a76-92a3-ad6634d7a792" width="350" height="100%"/>



