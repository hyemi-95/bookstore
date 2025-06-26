package project.bookstore.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id @GeneratedValue
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;
    private String nickname;
}
