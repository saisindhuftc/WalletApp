package com.example.walletapplication.entity;
import com.example.walletapplication.enums.TransactionType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long walletId;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    public Transaction() {}

    public Transaction(Double amount, TransactionType transactionType,Long walletId) {
        this.walletId = walletId;
        this.amount = amount;
        this.transactionType = transactionType;
        this.timestamp = LocalDateTime.now();
    }

}

