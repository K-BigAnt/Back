package com.bigant.gaeme.repository;


import com.bigant.gaeme.repository.entity.BoardTreePath;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardTreePathRepository extends JpaRepository<BoardTreePath, Long> {

    Slice<BoardTreePath> findAllByAncestor_Id(Long boardId, Pageable pageable);

    Slice<BoardTreePath> findAllByAncestor_IdAndAncestor_User_Id(Long boardId, Long userId, Pageable pageable);

}
