package org.example.boardcrud.service;

import lombok.RequiredArgsConstructor;
import org.example.boardcrud.domain.Board;
import org.example.boardcrud.repository.BoardRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    // 게시글 목록 조회
    @Transactional(readOnly = true)
    public Iterable<Board> findAllBoards(){
        return boardRepository.findAll();
    }

    // 페이징처리를 한 게시글 목록 조회
//    public Page<Board> findAllBoardsWithPaging(Pageable pageable){
//        // 게시글날짜 기준으로 desc - Page<Board> -- 여기서 Board 객체의 멤버변수를 기준으로 정렬
//        Pageable sortedByCreatedAt =
//                PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
//                        Sort.by(Sort.Direction.DESC, "created_at"));
//
//        // 정렬기준과 페이지번호,페이즈사이즈를 저장한 sortedByCreatedAt객체를 기준으로, select를 한다.
//        return boardRepository.findAll(sortedByCreatedAt);
//
//    }

    // 좋아요순, 최신순에 따른 페이징 처리
    public Page<Board> findBoardsWithPagingAndSort(Pageable pageable){
        return boardRepository.findAll(pageable);
    }

    // 게시글 상세 조회
    @Transactional(readOnly = true)
    public Board findBoardById(int id){
        return boardRepository.findById(id).orElse(null); // 없다면 null 객체 반환
    }

    // 게시글 등록
    @Transactional
    public Board saveBoard(Board board){
        return boardRepository.save(board); // board 없다면 insert, 있다면 update
    }

    // 게시글 삭제
    @Transactional
    public void deleteBoard(int id){
        boardRepository.deleteById(id);
    }

    // 좋아요 기능 증가
    @Transactional
    public void incrementLikes(int id){
        Board board = findBoardById(id);
        board.setLikes(board.getLikes() + 1); // 해당 id의 board의 like 필드 1 증가
        saveBoard(board); // 해당 변경사항을 DB에 반영해야 함..
    }
}
