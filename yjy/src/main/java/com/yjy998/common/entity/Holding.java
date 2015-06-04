package com.yjy998.common.entity;

import java.io.Serializable;

public class Holding implements Serializable{
    public int enableAmount;
    public float costBalance;
    public String stockCode;
    public String homsFundAccount;
    public String stockName;
    public int sellAmount;
    public float marketValue;
    public int currentAmount;
    public String exchangeType;
    public int buyAmount;
    public String homsCombineId;

    //以下是获取合约详情
    public String currentCash;
    public String rank;
    public String profitRatio;
    public String contract_type;
    public String storageRatio;
    public String totalProfit;
    public String relatedContest;
    public String totalMarketValue;
    public String applyTime;
    public String accountFee;
    public String contractId;
    public String totalAsset;
    public String earningRate;

    /**
     * 获取浮动盈亏比例
     */
    public String getFloatRatio() {
        float rateValue = (marketValue - costBalance) / costBalance * 100;
        return String.format("%.2f%%", rateValue);
    }
}
