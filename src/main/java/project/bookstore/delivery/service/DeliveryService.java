package project.bookstore.delivery.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.bookstore.delivery.entity.Delivery;
import project.bookstore.delivery.entity.DeliveryStatus;
import project.bookstore.delivery.entity.DeliveryStatusHistory;
import project.bookstore.delivery.repository.DeliveryRepository;
import project.bookstore.delivery.repository.DeliveryStatusHistoryRepository;
import project.bookstore.member.entity.Member;
import project.bookstore.member.repository.MemberRepository;

import org.springframework.security.access.AccessDeniedException;
import java.util.List;
import java.util.Optional;

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
     * @param id
     * @param newStatus
     * @param changerId
     */
    public void changeDeliveryStatus(Long id, DeliveryStatus newStatus, Long changerId) throws AccessDeniedException {
        Delivery delivery = deliveryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("배송정보를 찾을 수 없습니다."));

        DeliveryStatus before = delivery.getStatus();//상태 추출

        if (before == newStatus) return; //상태 동일하면 무시

        Member changer = memberRepository.findById(changerId).orElseThrow(()->new IllegalArgumentException("변경자를 찾을 수 없습니다."));

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
     * @param deliveryId
     * @return
     */
    public List<DeliveryStatusHistory> getDeliveryHistories(Long deliveryId) {
        return historyRepository.findByDeliveryId(deliveryId);
    }
}
