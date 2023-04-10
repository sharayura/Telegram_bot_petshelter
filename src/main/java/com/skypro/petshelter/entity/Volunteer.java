package com.skypro.petshelter.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "volunteers")
public class Volunteer {
    @Id
    @Column(name = "chat_id", nullable = false)
    private Long chatId;

    @Column(nullable = false)
    private String name;

    @Column
    private String contact;
}
