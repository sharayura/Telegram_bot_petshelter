package com.skypro.petshelter.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(name = "chat_id", nullable = false)
    private Long chatId;

    @Column(nullable = false)
    private String name;

    @Column
    private String contact;

    @Column(name = "days_trial")
    private Integer daysTrial;

    @Column(name = "dog_name")
    private String dogName;

    @Column(name = "fails_in_row")
    public int failsInRow;

    public int getFailsInRow() {
        return failsInRow;
    }

    public void setFailsInRow(int failsInRow) {
        this.failsInRow = failsInRow;
    }

    public Long getChatId() {
        return chatId;
    }

    public Integer getDaysTrial() {
        return daysTrial;
    }

    public void setDaysTrial(Integer daysTrial) {
        this.daysTrial = daysTrial;
    }

    public String getDogName() {
        return dogName;
    }

    public void setDogName(String dogName) {
        this.dogName = dogName;
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
        User user = (User) o;
        return Objects.equals(chatId, user.chatId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chatId);
    }
}
