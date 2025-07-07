package project.bookstore.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class FileService {
    //업로드 경로 (외부설정) ->application.yml
    @Value(("${file.upload-dir}"))
    private String uploadDir;

    //허용 확장자 목록(보안)
    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png", "pdf", "txt", "zip", "docx", "xlsx");
    private  static  final long MAX_FILE_SIZE = 10 * 1024 * 1024;

    // 파일 저장
    public String saveFile(MultipartFile file) {

        if (file.isEmpty()) return null;

        String originalFileName = file.getOriginalFilename();

        if (originalFileName == null || originalFileName.isBlank()) {
            throw new IllegalArgumentException("업로드 파일명이 비어있습니다.");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("파일 크기는 10MB 이하만 가능합니다.");
        }

        //확장자 체크(보안)
        String extension = "";
        int idx = originalFileName.lastIndexOf(".");

        if (idx > -1) {
            extension = originalFileName.substring(idx + 1).toLowerCase();
        } else {
            throw new IllegalArgumentException("파일 확장자가 없습니다.");
        }
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new IllegalArgumentException("허용되지 않은 파일 형식입니다." + extension);
        }


        //저장 파일명(중복 방지)
        String storedFileName = UUID.randomUUID().toString() +"."+ extension;

        //경로 보안 : 외부 경로 침투 방지
        if (storedFileName.contains("..") || storedFileName.contains("/") || storedFileName.contains("\\")) {
            throw new IllegalArgumentException("잘못된 파일명 입니다.");
        }

        File dest = new File(uploadDir, storedFileName);
        File folder = new File(uploadDir);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        try {
            file.transferTo(dest);
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 실패: " + originalFileName, e);
        }
        return storedFileName;
    }

    // 파일 삭제 (다운로드 경로와 동일하게)
    public void deleteFile(String storedFileName) {
        File file = new File(uploadDir, storedFileName);
        if (file.exists()) {
            file.delete();
        }
    }

    //파일 조회(다운로드용)
    public File getFile(String storedFileName) {
        File file = new File(uploadDir, storedFileName);
        //파일이 실제로 존재하는지, 디렉토리 침투 방지 등 추가 체크 가능
        if (!file.exists() || file.isDirectory()) {
            throw new IllegalArgumentException("파일이 존재하지 않거나 잘못된 접근입니다.");
        }
        return file;
    }
}
