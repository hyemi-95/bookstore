package project.bookstore.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import project.bookstore.board.dto.BoardDetailDto;
import project.bookstore.board.dto.BoardSaveDto;
import project.bookstore.board.dto.BoardSearchCondition;
import project.bookstore.board.dto.BoardUpdateDto;
import project.bookstore.board.entity.Board;
import project.bookstore.board.entity.BoardFile;
import project.bookstore.board.repository.BoardFileRepository;
import project.bookstore.board.repository.BoardRepository;
import project.bookstore.member.entity.Member;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardFileRepository boardFileRepository;

    private  final FileService fileService;


    private Board getBoard(Long id) {
        return boardRepository.findById(id).orElseThrow(()->new IllegalArgumentException("게시글이 존재하지 않습니다."));
    }

    //게시글 등록
    public void createBoard(BoardSaveDto dto, Member writer) {
        Board board = Board.create(dto.getTitle(),dto.getContent(), writer);

        //첨부파일 리스트가 있다면
        if (dto.getFiles() != null) {
            for (MultipartFile mf : dto.getFiles()) {
                if (!mf.isEmpty()) {//실제 업로드된 파일만 처리(비어있지 않은 파일만 저장)
                    //파일 저장 서비스 호출 : 실제 서버 경로에 파일을 저장, 저장명은 UUID 사용하여 관리
                    String storedFileName = fileService.saveFile(mf);
                    //BoardFile 엔티티 생성(원본파일명, 저장파일명)
                    BoardFile file = new BoardFile(mf.getOriginalFilename(), storedFileName);
                    //Board <-> BoardFile 양방향 연관관계 연결 (file 리스트에 추가 + BoardFile의 board 세팅 )
                    board.addFile(file);
                }
            }
        }

        boardRepository.save(board);//cascade 설정으로 인해 게시글 저장하면 첨부파일도 자동으로 저장
    }

    //게시글 수정
    public void updateBoard(Long id, BoardUpdateDto dto) {
        Board board = getBoard(id);
        board.update(dto.getTitle(),dto.getContent());//제목,내용만 수정 메서드

        //새파일 추가
        if (dto.getNewFiles() != null) {
            for (MultipartFile mf : dto.getNewFiles()) {
                if (!mf.isEmpty()) {
                    String storedFileName = fileService.saveFile(mf);
                    BoardFile file = new BoardFile(mf.getOriginalFilename(), storedFileName);
                    board.addFile(file);
                }
            }
        }
        //파일 삭제(수정 폼에서 선택한 삭제파일만)
        if (dto.getDeleteFileIds() != null) {
            List<BoardFile> filesToDelete = new ArrayList<>();
            for (Long fileId : dto.getDeleteFileIds()) {
                boardFileRepository.findById(fileId).ifPresent(e -> filesToDelete.add(e));//엔티티 조회 시 없을 수도 있으니 ifPresent
            }
            for (BoardFile file : filesToDelete) {
                //양방향 연관관계 제거 (Board의 file 리스트에서 삭제)
                board.getFiles().remove(file); //->JPA orphanRemoval / Cascade 옵션이 있어도 부모리스트에서 진짜로 빠져야 자동해제
                //실제 파일 시스템에서 삭제
                fileService.deleteFile(file.getStoredFileName());
                //DB에서 BoardFile 레코드 삭제
                boardFileRepository.delete(file);
            }
        }

    }

    //게시글 삭제
    public void deleteBoard(Long id) {
        Board board = getBoard(id);
        boardRepository.delete(board);
        //연관 첨부파일 및 댓글은 cascade로 자동 삭제
    }

    //게시글 상세 조회
    @Transactional(readOnly = true)
    public BoardDetailDto getBoardDetail(Long id, Member currentUser) {
        Board board = getBoard(id);
        return BoardDetailDto.from(board, currentUser);
    }

    //게시글 페이징 + 검색
    @Transactional(readOnly = true)
    public Page<Board> searchBoard(BoardSearchCondition condition, Pageable pageable) {
        return boardRepository.searchBoards(condition, pageable);
    }

}
