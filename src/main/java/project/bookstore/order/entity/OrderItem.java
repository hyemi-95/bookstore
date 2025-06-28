package project.bookstore.order.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.bookstore.book.entity.Book;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order; //어떤 주문에 포함됬는지

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book; //어떤 책을 주문했는지

    private int orderPrice; //주문금액

    private int count; //주문 수량

    //생성 메서드
    public static OrderItem createOrderItem(Book book, int orderPrice, int count) {
        OrderItem orderItem = new OrderItem();
        orderItem.book = book;
        orderItem.orderPrice = orderPrice;
        orderItem.count =count;
        book.removeStock(count);

        return orderItem;
    }

    public void addOrder(Order order) {
        this.order = order;
    }

    //가격 계산 메서드
    public int getTotalPrice(){
        return orderPrice * count;
    }

    public void cancel(){
        book.addStork(count);
    }
}
