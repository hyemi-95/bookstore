package project.bookstore.member.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.bookstore.global.config.baseEntity;
import project.bookstore.order.entity.Order;

import java.util.ArrayList;
import java.util.List;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends baseEntity {

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

    @OneToMany(mappedBy = "member")
    private List<Order> order = new ArrayList<>();//주문 상품들

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
}
