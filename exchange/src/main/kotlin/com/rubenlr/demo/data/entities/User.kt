package com.rubenlr.demo.data.entities

import jakarta.persistence.*

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @Column(nullable = false, length = 200)
    var name: String,

    @Column(nullable = false, length = 200, unique = true)
    var email: String
)