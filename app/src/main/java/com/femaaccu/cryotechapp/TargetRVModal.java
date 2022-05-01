package com.femaaccu.cryotechapp;

public class TargetRVModal {
    String currencyID;
    String name;
    String image;
    double basePrice;
    double currentPriceTarget;
    double targetPrice;
    double progressrate;

    double changeRate;

    public TargetRVModal(String name, double basePrice,  double targetPrice) {
        this.name = name;
        this.basePrice = basePrice;
        this.targetPrice = targetPrice;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public double getCurrentPriceTarget() {
        return currentPriceTarget;
    }

    public double getTargetPrice() {
        return targetPrice;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setCurrentPriceTarget(double currentPriceTarget) {
        this.currentPriceTarget = currentPriceTarget;
    }

    public String getCurrencyID() {
        return currencyID;
    }

    public void setCurrencyID(String currencyID) {
        this.currencyID = currencyID;
    }

    public double getChangeRate() {
        return changeRate;
    }

    public void setChangeRate(double changeRate) {
        this.changeRate = changeRate;
    }

    public double getProgressrate() {
        return progressrate;
    }

    public void setProgressrate(double progressrate) {
        this.progressrate = progressrate;
    }

    public double getBasePrice() {
        return basePrice;
    }
}
