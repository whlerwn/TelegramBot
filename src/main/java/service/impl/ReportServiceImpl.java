package service.impl;

import entity.Report;
import service.ReportService;

public class ReportServiceImpl implements ReportService {

    @Override
    public void setDescription(Report report, String chatId, String description) {
        report.setDescription(description);
        report.setChatId(chatId);
    }

    @Override
    public void setTime(Report report, int time) {
        report.setTimeInMinutes(time);
    }
}
