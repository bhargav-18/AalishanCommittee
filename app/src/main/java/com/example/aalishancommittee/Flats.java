package com.example.aalishancommittee;

import java.util.Date;

public class Flats {

    private String OwnerName;
    private String OwnerNumber;
    private String flatType;
    private String flatNo;
    private String livingInHouse;
    private String lastMaintenanceMonth;
    private String lastMaintenanceYear;
    private String tenantName;
    private String tenantNumber;
    private Date created;
    private Date updated;
    private String objectId;

    public String getOwnerName() {
        return OwnerName;
    }

    public void setOwnerName(String ownerName) {
        OwnerName = ownerName;
    }

    public String getFlatNo() {
        return flatNo;
    }

    public void setFlatNo(String flatNo) {
        this.flatNo = flatNo;
    }

    public String getOwnerNumber() {
        return OwnerNumber;
    }

    public void setOwnerNumber(String ownerNumber) {
        OwnerNumber = ownerNumber;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public String getTenantNumber() {
        return tenantNumber;
    }

    public void setTenantNumber(String tenantNumber) {
        this.tenantNumber = tenantNumber;
    }

    public String getFlatType() {
        return flatType;
    }

    public void setFlatType(String flatType) {
        this.flatType = flatType;
    }

    public String getLivingInHouse() {
        return livingInHouse;
    }

    public void setLivingInHouse(String livingInHouse) {
        this.livingInHouse = livingInHouse;
    }

    public String getLastMaintenanceMonth() {
        return lastMaintenanceMonth;
    }

    public void setLastMaintenanceMonth(String lastMaintenanceMonth) {
        this.lastMaintenanceMonth = lastMaintenanceMonth;
    }

    public String getLastMaintenanceYear() {
        return lastMaintenanceYear;
    }

    public void setLastMaintenanceYear(String lastMaintenanceYear) {
        this.lastMaintenanceYear = lastMaintenanceYear;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }
}
