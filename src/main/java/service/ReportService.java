package service;

import entity.Report;
import entity.User;
import service.impl.ReportServiceImpl;

public interface ReportService {

    static ReportService getInstance() {
        return new ReportServiceImpl();
    }

    void setReport(Report report, String chatId, String text);
}
