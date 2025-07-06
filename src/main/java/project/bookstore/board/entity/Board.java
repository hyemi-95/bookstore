package project.bookstore.board.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.bookstore.global.config.BaseEntity;
import project.bookstore.member.entity.Member;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;//pk

    @Column(nullable = false)
    private String title;//제목

    @Column(nullable = false , columnDefinition = "TEXT")
    private String content;//내용

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member writer;//작성자

    @OneToMany(mappedBy = "board" ,cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BoardFile> files = new ArrayList<>();//첨부파일

    @OneToMany(mappedBy = "board" ,cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();//댓글

    //생성자
    public Board(String title, String content, Member writer) {
        this.title = title;
        this.content = content;
        this.writer = writer;
    }

    //정적팩토리
    public static Board create(String title, String content, Member writer) {
        return new Board(title,content,writer);
    }

    //연관관계 메서드
    public void addFile(BoardFile file) {
        files.add(file);
        file.addBoard(this);
    }

    public void addComment(Comment comment) {
        comments.add(comment);
        comment.addComment(this);
    }

    //게시글 수정 메서드
    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Board board = (Board) o;
        return id != null && id.equals(board.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
