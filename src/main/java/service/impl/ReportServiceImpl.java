package service.impl;

import entity.Report;
import entity.User;
import service.ReportService;

public class ReportServiceImpl implements ReportService {

    @Override
    public void setReport(Report report, String chatId, String text) {
        report.setText(text);
        report.setChatId(chatId);
    }
}
