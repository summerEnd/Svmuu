package com.yjy998.common.entity;

import com.yjy998.R;

public class Entrust {


    public String entrustPrice;
    public String stockCode;
    public String homsFundAccount;
    public String businessBalance;
    public String stockName;
    public String exchangeType;
    public String withdrawAmount;
    public String entrustDirection;
    public String homsCombineId;
    public int entrustStatus;
    public String batchNo;
    public String businessAmount;
    public String entrustTime;
    public String entrustAmount;
    public String entrustNo;

    public int getEntrustStatus() {
        int status;
        switch (entrustStatus) {
            case 9:
                status = R.string.canceledOrder;
                break;

            case 7:
                status = R.string.deal_done;
                break;

            default:
                status = R.string.entrusting;
        }
        return status;
    }
}
