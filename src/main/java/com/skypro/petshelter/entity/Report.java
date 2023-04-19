package com.skypro.petshelter.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "reports")
public class Report {

    /**
     * Уникальный идентификатор отчета, состоящий из даты отправки отчета и идентификатора пользователя.
     * Если при отправке отчета такой будет найден в таблице, то найденная запись модифицируется
     */
    @Id
    @Column(name = "date_chat_id", nullable = false)
    private String dateChatId;

    @Column(name = "chat_id", nullable = false)
    private Long chatId;

    @Column (nullable = false)
    private LocalDate date;

    @Column
    @Lob
    @JsonIgnore
    private byte[] photo;

    @Column
    private String ration;

    @Column
    private String health;

    @Column
    private String habits;

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getDateChatId() {
        return dateChatId;
    }

    public void setDateChatId(String dateChatId) {
        this.dateChatId = dateChatId;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getRation() {
        return ration;
    }

    public void setRation(String ration) {
        this.ration = ration;
    }

    public String getHealth() {
        return health;
    }

    public void setHealth(String health) {
        this.health = health;
    }

    public String getHabits() {
        return habits;
    }

    public void setHabits(String habits) {
        this.habits = habits;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Report report = (Report) o;
        return Objects.equals(dateChatId, report.dateChatId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dateChatId);
    }
}
