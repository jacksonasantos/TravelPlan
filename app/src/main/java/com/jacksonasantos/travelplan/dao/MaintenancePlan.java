package com.jacksonasantos.travelplan.dao;

public class MaintenancePlan {
    public Integer id;
    public int service_type;
    public String description;
    public int measure;
    public int expiration;
    public String recommendation;

    public MaintenancePlan() {
        this.id = id;
        this.service_type = service_type;
        this.description = description;
        this.measure = measure;
        this.expiration = expiration;
        this.recommendation = recommendation;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public int getService_type() { return service_type; }
    public void setService_type(int service_type) { this.service_type = service_type; }

    public String getDescription() { return description;}
    public void setDescription(String description) {this.description = description; }

    public int getMeasure() { return measure; }
    public void setMeasure(int measure) { this.measure = measure; }

    public int getExpiration() { return expiration; }
    public void setExpiration(int expiration) { this.expiration = expiration; }

    public String getRecommendation() { return recommendation; }
    public void setRecommendation(String recommendation) { this.recommendation = recommendation; }
}
