package com.skypro.petshelter.entity;

import javax.persistence.*;
import java.time.LocalDate;

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
    private byte[] photo;

    @Column
    private String ration;

    @Column
    private String health;

    @Column
    private String habits;

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

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
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
}
