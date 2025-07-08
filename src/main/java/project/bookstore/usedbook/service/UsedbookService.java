package project.bookstore.usedbook.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.bookstore.member.entity.Member;
import project.bookstore.usedbook.dto.UsedBookListDto;
import project.bookstore.usedbook.dto.UsedBookSearchCondition;
import project.bookstore.usedbook.entity.UsedBook;
import project.bookstore.usedbook.entity.UsedStatus;
import project.bookstore.usedbook.repository.UsedBookRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UsedbookService {//중고책 관리 / 조회
    private final UsedBookRepository usedBookRepository;

    private UsedBook getUsedBookOrThrow(Long id) {
        return usedBookRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("중고책이 없습니다."));
    }

    /**
     * 조건 검색기능
     *
     * @param condition
     * @return
     */
    public Page<UsedBookListDto> searchUsedBook(UsedBookSearchCondition condition, Pageable pageable) {
        Page<UsedBook> usedBooks = usedBookRepository.searchUsedBook(condition, pageable);
        return usedBooks.map(usedBook -> UsedBookListDto.from(usedBook));
    }

    /**
     * 전체 중고책 목록
     */
    public List<UsedBook> findAll() {
        return usedBookRepository.findAll();
    }

    /**
     * 판매자 : 내 중고책만
     */
    public List<UsedBook> findBySeller(Member seller) {
        return usedBookRepository.findBySeller(seller);
    }

    /**
     * 상태별 조회
     */
    public List<UsedBook> findByStatus(UsedStatus status) {
        return usedBookRepository.findByStatus(status);
    }

    /**
     * 개별 중고책 상세
     */
    public UsedBook findById(Long id) {
        return getUsedBookOrThrow(id);
    }

    @Transactional
    public void delete(Long id) {
        usedBookRepository.deleteById(id);
    }
}
