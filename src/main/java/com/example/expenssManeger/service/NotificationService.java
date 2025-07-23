package com.example.expenssManeger.service;

import com.example.expenssManeger.Repository.ProfileRepo;
import com.example.expenssManeger.dto.ExpenseDto;
import com.example.expenssManeger.entity.ProfileEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private  final ProfileService profileService;
    private  final ProfileRepo profileRepo;
    private  final ExpenseService expenseService;
    private  final EmailService emailService;
    private  final IncomeService incomeService;



    @Value("${money.manager.frontend.url}")
    private  String frontendUrl;


//@Scheduled(cron = "0 * * * * *",zone = "IST")
@Scheduled(cron = "0 0 22 * * *",zone = "IST")
    public  void  sendDailyIncomeExpenseNotification() {
    log.info("Job Satarted: sendDailyIncomeExpenseNotification");
    List<ProfileEntity> profile=profileRepo.findAll();

    for(ProfileEntity profile1 : profile)
    {
        String body = "Hello " + profile1.getFullName() + ",\n\n" +
                "Here is your daily income and expense report for " + LocalDate.now() + ":\n\n";
        emailService.sendEmail(profile1.getEmail(), "Daily Reminder",body);
    }

}


////@Scheduled(cron = "0 * * * * *", zone = "IST")
//    @Scheduled(cron = "0 0 23 * * *", zone = "IST")
//    public void sendDailyExpenseSummary() {
//        log.info("Job Started: sendDailyExpenseSummary");
//        List<ProfileEntity> profiles = profileRepo.findAll();
//
//        for (ProfileEntity profile : profiles) {
//            List<ExpenseDto> todaysExpenses = expenseService.getexpenesForUserDate(profile.getId(), LocalDate.now());
//            if (todaysExpenses == null || todaysExpenses.isEmpty()) {
//                continue; // Do nothing if no expenses
//            }
//
//            StringBuilder table = new StringBuilder();
//            table.append("<table border='1'><tr><th>Amount</th><th>Date</th><th>Time</th><th>Category</th></tr>");
//            for (ExpenseDto expense : todaysExpenses) {
//                table.append("<tr>")
//                        .append("<td>").append(expense.getAmount()).append("</td>")
//                        .append("<td>").append(expense.getDate()).append("</td>")
//                        .append("<td>").append(expense.getCreatedAt().substring(11, 19)).append("</td>")
//                        .append("<td>").append(expense.getCategoryName()).append("</td>")
//                        .append("</tr>");
//            }
//            table.append("</table>");
//
//            String body = "Hello " + profile.getFullname() + ",<br><br>"
//                    + "Here is your expense summary for " + LocalDate.now() + ":<br><br>"
//                    + table.toString();
//
//            emailService.sendEmail(profile.getEmail(), "Daily Expense Summary", body);
//        }
//    }


@Scheduled(cron = "0 0 23 * * *", zone = "IST")
public void sendDailyExpenseSummary() {
    log.info("Job Started: sendDailyExpenseSummary");
    List<ProfileEntity> profiles = profileRepo.findAll();

    for (ProfileEntity profile : profiles) {
        List<ExpenseDto> todaysExpenses = expenseService.getexpenesForUserOnDate(profile.getId(), LocalDate.now(ZoneId.of("Asia/Kolkata")));

        if (!todaysExpenses.isEmpty()) {
            StringBuilder table = new StringBuilder();

            table.append("<div style='font-family: Arial, sans-serif;'>")
                    .append("<h2 style='color: #333;'>Expense Summary for ")
                    .append(LocalDate.now(ZoneId.of("Asia/Kolkata")))
                    .append("</h2>");

            table.append("<table style='border-collapse: collapse; width: 100%; font-family: Arial, sans-serif;'>")
                    .append("<thead>")
                    .append("<tr style='background-color: #f2f2f2;'>")
                    .append("<th style='border: 1px solid #ddd; padding: 10px; text-align: center;'>#</th>")
                    .append("<th style='border: 1px solid #ddd; padding: 10px; text-align: center;'>Amount (₹)</th>")
                    .append("<th style='border: 1px solid #ddd; padding: 10px; text-align: center;'>Date</th>")
                    .append("<th style='border: 1px solid #ddd; padding: 10px; text-align: center;'>Time</th>")
                    .append("<th style='border: 1px solid #ddd; padding: 10px; text-align: center;'>Category</th>")
                    .append("</tr>")
                    .append("</thead>")
                    .append("<tbody>");

            int i = 1;
            for (ExpenseDto expenseDto : todaysExpenses) {
                table.append("<tr>")
                        .append("<td style='border: 1px solid #ddd; padding: 10px; text-align: center;'>").append(i++).append("</td>")
                        .append("<td style='border: 1px solid #ddd; padding: 10px; text-align: center;'>").append(expenseDto.getAmount()).append("</td>")
                        .append("<td style='border: 1px solid #ddd; padding: 10px; text-align: center;'>").append(expenseDto.getDate()).append("</td>")
                        .append("<td style='border: 1px solid #ddd; padding: 10px; text-align: center;'>")
                        .append(expenseDto.getCreatedAt().substring(11, 19)).append("</td>")
                        .append("<td style='border: 1px solid #ddd; padding: 10px; text-align: center;'>").append(expenseDto.getCategoryName()).append("</td>")
                        .append("</tr>");
            }

            table.append("</tbody>")
                    .append("</table>")
                    .append("<p style='margin-top: 20px;'>Regards,<br>Your Money Manager App</p>")
                    .append("</div>");

            String body = "Hello " + profile.getFullName() + ",<br><br>" +
                    "Here is your expense summary for " + LocalDate.now(ZoneId.of("Asia/Kolkata")) + ":<br><br>" +
                    table.toString();

            emailService.sendEmail(profile.getEmail(), "Daily Expense Summary", body);
        }
    }
    log.info("Job Ended: sendDailyExpenseSummary");
 }
}
