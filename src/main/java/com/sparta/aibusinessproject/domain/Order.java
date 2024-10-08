package com.sparta.aibusinessproject.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
@Table(name = "p_order")
// Delete의 값이 null인 정보만 가져옴
@Where(clause = "deleted_at is NULL")
public class Order extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String storeName;

    private String paymentMethod;

    private int totalPrice;

    private String requests;

    @Enumerated(value = EnumType.STRING)
    private OrderStatusEnum status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Store store;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderMenu> orderMenuList = new ArrayList<>();

    public void cancelOrder() {
        modifyOrderStatus(OrderStatusEnum.CANCELLED);
    }

    public void addOrderMenuAndTotalPrice(OrderMenu menu) {
        orderMenuList.add(menu);
        updateTotalPrice(menu.calculatePrice()); // 가격 업데이트
    }

    private void updateTotalPrice(int additionalPrice) {
        this.totalPrice += additionalPrice; // 기존 총 가격에 추가된 메뉴 가격을 더함
    }

    public void modifyOrderStatus(OrderStatusEnum status) {
        this.status = status;
    }

    public void updatePaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
        this.status = OrderStatusEnum.PAID;
    }
}
