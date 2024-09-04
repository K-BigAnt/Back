package com.bigant.gaeme.dto;

import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardCreateRequestDto {

    private String content;

    private Optional<Long> ancestorId;

    private List<MultipartFile> imageFiles;

}
