package project.bookstore.order.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.bookstore.book.entity.Book;
import project.bookstore.delivery.entity.Delivery;
import project.bookstore.global.config.BaseEntity;
import project.bookstore.usedbook.entity.UsedBook;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem extends BaseEntity {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order; //어떤 주문에 포함됬는지

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery; //어떤 주문에 포함됬는지

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book; //어떤 책을 주문했는지

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usedbook_id")
    private UsedBook usedBook;//어떤 중고책을 담았는지

    private int orderPrice; //주문금액

    private int count; //주문 수량

    //생성 메서드
    public static OrderItem createOrderItem(Book book, UsedBook usedBook,int orderPrice, int count) {
        OrderItem orderItem = new OrderItem();
        orderItem.book = book;
        orderItem.usedBook = usedBook;
        orderItem.orderPrice = orderPrice;
        orderItem.count =count;
        if (book != null) {
            book.removeStock(count);
        }
        if (usedBook != null) {
            usedBook.markSold();
        }

        return orderItem;
    }

    //신간용
    public static OrderItem createOrderItem(Book book, int orderPrice, int count) {
        return createOrderItem(book, null, orderPrice, count);
    }

    //중고용
    public static OrderItem createOrderItem(UsedBook usedBook, int orderPrice, int count) {
        return createOrderItem(null, usedBook, orderPrice, count);
    }

    public void addOrder(Order order) {
        this.order = order;
    }

    public void addDelivery(Delivery delivery) {
        this.delivery = delivery;
    }

    //가격 계산 메서드
    public int getTotalPrice(){
        return orderPrice * count;
    }

    public void cancel(){
        if(book != null) {//신간은 재고복구
            book.addStork(count);
        }
        if (usedBook != null) {//중고는 상태복구
            usedBook.markForSale();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem orderItem = (OrderItem) o;
        return id != null && id.equals(orderItem.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
