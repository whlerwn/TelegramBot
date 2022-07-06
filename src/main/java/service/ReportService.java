package service;

import entity.Report;
import service.impl.ReportServiceImpl;

public interface ReportService {

    static ReportService getInstance() {
        return new ReportServiceImpl();
    }

    void setDescription(Report report, String chatId, String description);
    void setTime(Report report, int time);
}
