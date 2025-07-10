package project.bookstore.delivery.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.bookstore.admin.dto.AdminDeliveryListDto;
import project.bookstore.delivery.dto.DeliveryDto;
import project.bookstore.delivery.dto.DeliveryStatusHistoryDto;
import project.bookstore.delivery.entity.Delivery;
import project.bookstore.delivery.entity.DeliveryStatus;
import project.bookstore.delivery.entity.DeliveryStatusHistory;
import project.bookstore.delivery.repository.DeliveryRepository;
import project.bookstore.delivery.repository.DeliveryStatusHistoryRepository;
import project.bookstore.member.entity.Member;
import project.bookstore.member.repository.MemberRepository;

import org.springframework.security.access.AccessDeniedException;
import project.bookstore.seller.dto.SellerDeliveryDto;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final DeliveryStatusHistoryRepository historyRepository;
    private final MemberRepository memberRepository; //변경자 정보

    public Delivery findById(Long id) {
        return deliveryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("배송정보를 찾을 수 없습니다."));
    }

    /**
     * 배송상태 변경 및 변경이력 저장
     *
     * @param id
     * @param newStatus
     * @param changerId
     */
    public void changeDeliveryStatus(Long id, DeliveryStatus newStatus, Long changerId) throws AccessDeniedException {
        Delivery delivery = findById(id);

        DeliveryStatus before = delivery.getStatus();//상태 추출

        if (before == newStatus) return; //상태 동일하면 무시

        Member changer = memberRepository.findById(changerId).orElseThrow(() -> new IllegalArgumentException("변경자를 찾을 수 없습니다."));

        if (!changer.isAdminOrSeller()) {
            throw new AccessDeniedException("배송상태 변경 권한이 없습니다.");
        }

        delivery.changeStatus(newStatus);//배송상태 변경

        //이력 저장
        DeliveryStatusHistory history = new DeliveryStatusHistory(delivery, before, newStatus, changer);
        historyRepository.save(history);
    }

    /**
     * 배송상태 변경 이력 조회
     *
     * @param deliveryId
     * @return
     */
    public List<DeliveryStatusHistory> getDeliveryHistories(Long deliveryId) {
        return historyRepository.findByDeliveryId(deliveryId);
    }
    //관리자용 start
    public Page<AdminDeliveryListDto> findAllForAdmin(Pageable pageable) {
        return deliveryRepository.findBySellerIsNull(pageable)
                .map(AdminDeliveryListDto::from);
    }

    public Delivery findDetailForAdmin(Long id) {
        Delivery delivery = findById(id);
        if (delivery.getSeller() != null) throw new AccessDeniedException("신책 배송만 조회 가능합니다.");
        return delivery;
    }
    //관리자용 end

    //판매자용 start
    //내가 판매한 상품의 배송 목록(페이징)
    public Page<SellerDeliveryDto> findBySeller(Member seller, Pageable pageable) {
        Page<Delivery> deliveries = deliveryRepository.findAllBySeller(seller, pageable);
        return deliveries.map(SellerDeliveryDto::from);
    }

    //내 상품의 배송 상세만 조회(권한체크포함)
    public SellerDeliveryDto findDtoByIdForSeller(Long id, Member seller) {
        Delivery delivery = findById(id);

        //usedBook의 판매자가 seller가 맞는지확인
        boolean isMyDelivery = delivery.getOrder()
                .getOrderItems().stream()
                .anyMatch(item -> item.getUsedBook() != null && item.getUsedBook().getSeller().getId().equals(seller.getId()));

        if (!isMyDelivery) {
            throw new IllegalArgumentException("본인 판매책이 아닙니다.");
        }
        return SellerDeliveryDto.from(delivery);
    }

    //상태변경이력
    public List<DeliveryStatusHistoryDto> getDeliveryHistoryDtos(Long id) {
        List<DeliveryStatusHistory> histories = historyRepository.findByDeliveryId(id);
        return histories.stream().map(DeliveryStatusHistoryDto::form).collect(Collectors.toList());
    }
    //판매자용 end
}
