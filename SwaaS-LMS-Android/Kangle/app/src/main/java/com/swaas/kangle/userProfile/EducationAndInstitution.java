package com.swaas.kangle.userProfile;

import com.swaas.kangle.userProfile.models.lstEducation;
import com.swaas.kangle.userProfile.models.lstInstitution;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Sayellessh on 05-07-2018.
 */

public class EducationAndInstitution implements Serializable {

    public List<lstInstitution> lstInstitution;
    public List<lstEducation> lstEducation;

    public List<lstInstitution> getLstInstitution() {
        return lstInstitution;
    }

    public void setLstInstitution(List<lstInstitution> lstInstitution) {
        this.lstInstitution = lstInstitution;
    }

    public List<lstEducation> getLstEducation() {
        return lstEducation;
    }

    public void setLstEducation(List<lstEducation> lstEducation) {
        this.lstEducation = lstEducation;
    }
}
