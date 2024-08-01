package com.rubenlr.demo.data.entities

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.OffsetDateTime

@Entity
@Table(name = "transactions")
data class Transaction(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    /**
     * Account of the origin of the transaction
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "from_account_id")
    var fromAccount: Account,

    /**
     * Value taken from the destination, not including fee
     */
    var fromValue: BigDecimal,

    /**
     * Type of the transaction to understand
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var type: TransactionType,

    /**
     * Account of the destination of the transaction
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "to_account_id")
    var toAccount: Account,

    /**
     * Value arrived in the destination of the transaction
     */
    var toValue: BigDecimal,

    /**
     * When the transaction was executed
     */
    var executedAt: OffsetDateTime
)

enum class TransactionType {
    DEPOSIT,
    WITHDRAW,
    EXCHANGE
}