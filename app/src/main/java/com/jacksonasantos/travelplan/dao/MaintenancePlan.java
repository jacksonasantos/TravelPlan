package com.jacksonasantos.travelplan.dao;

import androidx.annotation.NonNull;

public class MaintenancePlan {
    public Integer id;
    public int service_type;
    public String description;
    public int measure;
    public int expiration_default;
    public String recommendation;

    public MaintenancePlan() {
        this.id = id;
        this.service_type = service_type;
        this.description = description;
        this.measure = measure;
        this.expiration_default = expiration_default;
        this.recommendation = recommendation;
    }

    @NonNull
    @Override
    public String toString() { return description; }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public int getService_type() { return service_type; }
    public void setService_type(int service_type) { this.service_type = service_type; }

    public String getDescription() { return description;}
    public void setDescription(String description) {this.description = description; }

    public int getMeasure() { return measure; }
    public void setMeasure(int measure) { this.measure = measure; }

    public int getExpiration_default() { return expiration_default; }
    public void setExpiration_default(int expiration_default) { this.expiration_default = expiration_default; }

    public String getRecommendation() { return recommendation; }
    public void setRecommendation(String recommendation) { this.recommendation = recommendation; }
}
