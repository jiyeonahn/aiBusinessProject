package com.sparta.aibusinessproject.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.cglib.core.Local;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
@Table(name = "p_payment")
// Delete의 값이 null인 정보만 가져옴
@Where(clause = "deleted_at is NULL")
public class Payment extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private int payAmount;

    @Enumerated(value = EnumType.STRING)
    private PaymentStatusEnum status;

    private String merchantUid;

    private String impUid;

    private String payMethod;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private User user;

    public void cancelPayment(){
        this.status = PaymentStatusEnum.CANCELED;
    }
}
