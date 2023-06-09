package com.study.board.common;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class ImageForm {
    private List<MultipartFile> imageFiles;

}
