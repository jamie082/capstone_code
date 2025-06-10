package com.example.d308vacationplanner2.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "excursion")
public class Excursion {
    @PrimaryKey(autoGenerate = true)
    private int excursionID;
    private String excursionName;

    private int vacationID;

    private String excursionDate;

    public int getExcursionID() {
        return excursionID;
    }

    public void setExcursionID(int excursionID) {
        this.excursionID = excursionID;
    }

    public String getExcursionName() {
        return excursionName;
    }

    public void setExcursionName(String excursionName) {
        this.excursionName = excursionName;
    }


    public int getVacationID() {
        return vacationID;
    }

    public void setVacationID(int vacationID) {
        this.vacationID = vacationID;
    }

    public void setExcursionDate(String excursionDate) {this.excursionDate = excursionDate;}
    public String getExcursionDate() {
        return excursionDate;
    }

    public Excursion(int excursionID, String excursionName, int vacationID, String excursionDate) {
        this.excursionID = excursionID;
        this.excursionName = excursionName;
        this.vacationID = vacationID;
        this.excursionDate = excursionDate;
    }
}

