package com.swaas.kangle.Reports;

import com.swaas.kangle.models.DashBoardDetailsModel;
import com.swaas.kangle.models.DashboardDetailsModelTeam;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Sayellessh on 12-11-2018.
 */

public class ReportModel implements Serializable{

    List<DashBoardDetailsModel> Individual_Report;
    List<DashboardDetailsModelTeam> Team_Report;

    public List<DashBoardDetailsModel> getIndividual_Report() {
        return Individual_Report;
    }

    public void setIndividual_Report(List<DashBoardDetailsModel> individual_Report) {
        Individual_Report = individual_Report;
    }

    public List<DashboardDetailsModelTeam> getTeam_Report() {
        return Team_Report;
    }

    public void setTeam_Report(List<DashboardDetailsModelTeam> team_Report) {
        Team_Report = team_Report;
    }
}
