package org.pgm.reservationback.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileService {

    @Value("${file.upload.dir:uploads}") // 기본값으로 프로젝트 경로 아래 uploads 디렉토리 사용
    private String uploadDir;

    public String saveFile(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }

        // 업로드 디렉토리 없으면 생성
        Files.createDirectories(Paths.get(uploadDir));

        // 원본 파일명에서 확장자 추출
        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        // 고유한 파일명 생성
        String uniqueFilename = UUID.randomUUID().toString() + extension;
        File dest = new File(uploadDir, uniqueFilename);

        file.transferTo(dest);

        // 브라우저 접근 가능한 URL을 반환해야 한다면,
        // 스프링 부트 정적 리소스 매핑(/uploads/** => 파일 경로) 또는 별도의 Controller를 통해 접근 경로를 만들어야 함.

        // 여기서는 단순히 "http://localhost:8082/uploads/파일명" 으로 접근 가능하다고 가정.
        // 실제로는 WebMvcConfigurer로 "/uploads" -> uploadDir 매핑을 해주어야 한다.
        return "/uploads/" + uniqueFilename;
    }
}
