package project.bookstore.member.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.bookstore.global.config.BaseEntity;
import project.bookstore.order.entity.Order;

import java.util.ArrayList;
import java.util.List;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private MemberStatus status = MemberStatus.NOMAL;//현재 상태

    private String suspendReason;//정지 사유

    @OneToMany(mappedBy = "member")
    private List<Order> order = new ArrayList<>();//주문 상품들

    @OneToMany(mappedBy = "member" , cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberSuspendHistory> suspendHistories = new ArrayList<>();//회원 정지 이력


    public Member(String email, String password, String nickname, Role role) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.role = role;
    }

    public boolean isAdmin(){
        return this.role == Role.ADMIN;
    }

    public boolean isSeller(){
        return this.role == Role.SELLER;
    }

    public boolean isAdminOrSeller(){
        return this.role == Role.ADMIN || this.role == Role.SELLER;
    }

    //계정정지
    public void suspend(String suspendReason) {
        this.status = MemberStatus.SUSPENDED;
        this.suspendReason = suspendReason;
    }

    //계정해제
    public void restore() {
        this.status = MemberStatus.NOMAL;
        this.suspendReason = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Member member = (Member) o;
        return id != null && id.equals(member.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
