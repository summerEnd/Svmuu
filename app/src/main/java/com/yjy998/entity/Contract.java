package com.yjy998.entity;


import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Contract implements Serializable {



    public String uid;
    public String verify_date;
    public String new_rate;
    public String contract_no;
    public String prod_id;
    public String cash_amount;
    public String apply_amount;
    public String fee_type;
    public String type;
    public String contract_type;
    public String verify_user;
    public String real_end_time;
    public String real_end_remark;
    public String id;
    public String rate;
    public String update_time;
    public String verify_status;
    public String end_time;
    public String trade_amount;
    public String start_time;
    public String add_time;
    public String quan_amount;

    public static String getType(String type) {
        if ("2".equals(type)) {
            return "T+N";
        } else if ("3".equals(type)) {
            return "T+9";
        }
        return null;
    }

}
