package com.skypro.petshelter.scheduler;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import com.skypro.petshelter.repositories.UserRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

/**
 * Класс обработки <i>периодических</i> проверок параметров пользователей
 * @author Sharapov Yuri
 */
@Component
public class NotificationTimer {

    private final TelegramBot telegramBot;
    private final UserRepository userRepository;

    public NotificationTimer(TelegramBot telegramBot, UserRepository userRepository) {
        this.telegramBot = telegramBot;
        this.userRepository = userRepository;
    }

    /**
     * Проверка <b>раз в день</b>, у кого из пользователей закончился испытательный срок.
     * <br> {@link UserRepository#findUsersByDaysTrialNotNull()}
     */
    @Scheduled(cron = "0 23 13 * * ?")
    @Transactional
    public void endTrial() {
        userRepository.findUsersByDaysTrialNotNull().forEach(user -> {
            if (user.getDaysTrial() == 0) {
                user.setDaysTrial(null);
                SendMessage message = new SendMessage(user.getChatId(),
                        "Поздравляем!!! Вы прошли испытание, животина ваша!");
                telegramBot.execute(message);
            }
        });
    }

}
