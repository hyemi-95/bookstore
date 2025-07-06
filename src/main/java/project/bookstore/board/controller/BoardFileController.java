package project.bookstore.board.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import project.bookstore.board.entity.BoardFile;
import project.bookstore.board.repository.BoardFileRepository;
import project.bookstore.board.service.FileService;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

@Controller
@RequiredArgsConstructor
@RequestMapping("board/file")
public class BoardFileController {

    private final BoardFileRepository boardFileRepository;
    private final FileService fileService;

    @GetMapping("/{id}/download")
    public void download(@PathVariable Long id, HttpServletResponse response) throws IOException {
        BoardFile file = boardFileRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("파일이 존재하지 않습니다."));

        String originalFileName = file.getOriginalFileName();
        String storedFileName = file.getStoredFileName();

        //해당 파일 가져오기
        File downloadFile = fileService.getFile(storedFileName);

        // Content-Disposition 헤더(파일명 인코딩)
        String encodedFileName = URLEncoder.encode(originalFileName, StandardCharsets.UTF_8)
                .replaceAll("\\+", "%20");

        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedFileName + "\"");

        Files.copy(downloadFile.toPath(), response.getOutputStream());
        response.getOutputStream().flush();
    }

}
