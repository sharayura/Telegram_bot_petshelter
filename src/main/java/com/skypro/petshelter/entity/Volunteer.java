package com.skypro.petshelter.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

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

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Volunteer volunteer = (Volunteer) o;
        return Objects.equals(chatId, volunteer.chatId) && Objects.equals(name, volunteer.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chatId, name);
    }
}
