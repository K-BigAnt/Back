package com.bigant.gaeme.repository.entity;

import com.bigant.gaeme.dto.BoardUpdateRequestDto;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User user;

    private String content;

    private String pictureUrl;

    private Long likeCnt;

    @CreatedDate
    private LocalDateTime createdAt = LocalDateTime.now();

    @LastModifiedDate
    private LocalDateTime updatedAt = LocalDateTime.now();

    private boolean isDeleted;

    public void update(BoardUpdateRequestDto updateDto) {
        if (updateDto.getContent() != null) {
            this.content = updateDto.getContent();
        }
        if (updateDto.getLikeCnt() != null) {
            this.likeCnt = updateDto.getLikeCnt();
        }
    }

}
