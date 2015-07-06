package com.svmuu.common.entity;

import java.io.Serializable;


public class History implements Serializable {
    public String name="";

    @Override
    public boolean equals(Object o) {

        if (o==null||!(o instanceof History)){
            return false;
        }

        return name.equals(((History) o).name);
    }
}
