package com.yjy998.common.entity;


import java.io.Serializable;
import java.util.ArrayList;

public class ContractDetail implements Serializable{
    public String contract_type;

    public String currentCash;
    public String profitRatio;
    public String storageRatio;
    public String totalProfit;
    public String relatedContest;
    public String totalMarketValue;
    public String applyTime;
    public String accountFee;
    public String contractId;
    public String totalAsset;

    public ArrayList<Holding> holdings;
    public Contract contract;
}
