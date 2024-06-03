package org.example.boardcrud.repository;

import org.example.boardcrud.domain.Board;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface BoardRepository extends CrudRepository<Board, Integer>, PagingAndSortingRepository<Board, Integer> {
    // 최신 게시글부터 조회하기 위해, 메서드 커스터마이징

}
