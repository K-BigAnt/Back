package com.bigant.gaeme.repository;

import com.bigant.gaeme.repository.entity.Board;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    Slice<Board> findAllBy(Pageable pageable);

    Slice<Board> findAllByUser_Id(Long userId, Pageable pageable);

}
