package com.skypro.petshelter.scheduler;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import com.skypro.petshelter.entity.Report;
import com.skypro.petshelter.repositories.UserRepository;
import com.skypro.petshelter.service.ReportService;
import com.skypro.petshelter.service.VolunteersService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDate;

/**
 * Класс обработки <i>периодических</i> проверок параметров пользователей
 *
 * @author Sharapov Yuri
 */
@Component
public class NotificationTimer {

    private final TelegramBot telegramBot;
    private final UserRepository userRepository;
    private final ReportService reportService;
    private final VolunteersService volunteersService;

    public NotificationTimer(TelegramBot telegramBot, UserRepository userRepository, ReportService reportService, VolunteersService volunteersService) {
        this.telegramBot = telegramBot;
        this.userRepository = userRepository;
        this.reportService = reportService;
        this.volunteersService = volunteersService;
    }

    /**
     * Проверка <b>раз в день</b>, у кого из пользователей закончился испытательный срок.
     * <br> {@link UserRepository#findUsersByDaysTrialNotNull()}
     */
    @Scheduled(cron = "0 50 15 * * ?")
    @Transactional
    public void decTrial() {
        userRepository.findUsersByDaysTrialNotNull().forEach(user -> {
            user.setDaysTrial(user.getDaysTrial() - 1);
            if (user.getDaysTrial() == 0) {
                user.setDaysTrial(null);
                SendMessage message = new SendMessage(user.getChatId(),
                        "Поздравляем!!! Вы прошли испытание, животина ваша!");
                telegramBot.execute(message);
            }
        });
    }

    @Scheduled(cron = "0 38 16 * * ?")
    @Transactional
    public void checkFails() {
        userRepository.findUsersByDaysTrialNotNull().forEach(user -> {
            Report report = reportService.getReport(user.getChatId(), LocalDate.now().minusDays(1));
            if (report == null && user.getDaysTrial() < 30) {
                user.setFailsInRow(user.getFailsInRow() + 1);
                SendMessage message = new SendMessage(user.getChatId(),
                        "Вчера Вы не отправили отчет, очень плохо!");
                telegramBot.execute(message);
                if (user.getFailsInRow() > 2) {
                    volunteersService.callVolunteer(user.getName(), user.getContact(),
                            "Клиент не отправлял отчет, дней: " + user.getFailsInRow());
                }
            } else {
                user.setFailsInRow(0);
                if (reportService.hasReportAnyNull(report)) {
                    SendMessage message = new SendMessage(user.getChatId(),
                            "Дорогой усыновитель, мы заметили, что ты заполняешь отчет не так подробно, как необходимо. " +
                                    "Пожалуйста, подойди ответственнее к этому занятию. В противном случае волонтеры " +
                                    "приюта будут обязаны самолично проверять условия содержания собаки");
                    telegramBot.execute(message);
                }
            }

        });
    }

}
