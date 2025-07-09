package project.bookstore.usedbook.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.bookstore.global.dto.ResultDto;
import project.bookstore.member.entity.Member;
import project.bookstore.usedbook.dto.UsedBookRequestDetailDto;
import project.bookstore.usedbook.dto.UsedBookRequestDto;
import project.bookstore.usedbook.dto.UsedBookRequestForm;
import project.bookstore.usedbook.entity.RequestStatus;
import project.bookstore.usedbook.entity.UsedBook;
import project.bookstore.usedbook.entity.UsedBookRequest;
import project.bookstore.usedbook.entity.UsedStatus;
import project.bookstore.usedbook.repository.UsedBookRepository;
import project.bookstore.usedbook.repository.UsedBookRequestRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UsedBookRequestService {//중고책 판매 신청

    private final UsedBookRequestRepository requestRepository;
    private final UsedBookRepository usedBookRepository;

    private UsedBookRequest getUsedBookRequestOrThrow(Long id) {
        return requestRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("신청내역이 없습니다."));
    }

    /**
     * 판매신청
     */
    @Transactional
    public Long requestSeller(Member seller, String title, String author, Integer price, String isbn, String description) {
        UsedBookRequest request = new UsedBookRequest(seller, title, author, price, isbn, description, RequestStatus.PENDING);
        requestRepository.save(request);
        return request.getId();
    }

    /***
     * 판매자 : 내 신청 내역
     */
    public List<UsedBookRequest> findMyRequest(Member seller) {
        return requestRepository.findBySeller(seller);
    }

    /**
     * 관리자 : 전체 신청 내역
     */
    public Page<UsedBookRequestDto> findAll(Pageable pageable) {
        Page<UsedBookRequest> usedBookRequests = requestRepository.findAll(pageable);
        return usedBookRequests.map(r -> UsedBookRequestDto.from(r));
    }

    /**
     * 관리자 : 승인 처리 -> 중고책 자동 등록
     */
    @Transactional
    public void approveRequest(Long requestId, Member admin) {
        UsedBookRequest request = getUsedBookRequestOrThrow(requestId);
        request.approve(admin);

        //중고책 등록
        UsedBook usedBook = new UsedBook(
                request.getTitle(),
                request.getAuthor(),
                request.getPrice(),
                request.getIsbn(),
                request.getDescription(),
                request.getSeller(),
                UsedStatus.FOR_SALE
        );
        usedBookRepository.save(usedBook);
    }

    /**
     * 관리자 : 거절 처리(사유 포함)
     */
    @Transactional
    public void rejectRequest(Long requestId, Member admin, String rejectReason) {
        UsedBookRequest request = getUsedBookRequestOrThrow(requestId);
        request.reject(rejectReason, admin);
    }

    /**
     * 개별 신청 상세 조회
     */
    public UsedBookRequestDetailDto findById(Long id) {
        UsedBookRequest usedBookRequest = getUsedBookRequestOrThrow(id);
        return UsedBookRequestDetailDto.from(usedBookRequest);
    }

    @Transactional
    public ResultDto updateRequest(Long id, Long memberId, UsedBookRequestForm form) {
        UsedBookRequest request = getUsedBookRequestOrThrow(id);
        //본인 상태 확인
        if (!request.getSeller().getId().equals(memberId)) {
            return new ResultDto(false, "권한이 없습니다.");
        }
        if (request.getStatus() != RequestStatus.PENDING) {
            return new ResultDto(false, "대기 상태에서만 수정이 가능합니다.");
        }
        request.updateInfo(form.getTitle(), form.getAuthor(), form.getPrice(), form.getIsbn(), form.getDescription());
        return new ResultDto(true, null);
    }

    @Transactional
    public void delete(Long id) {
        requestRepository.deleteById(id);
    }
}

