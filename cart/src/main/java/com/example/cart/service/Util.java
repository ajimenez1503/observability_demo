package com.example.cart.service;

import java.math.BigDecimal;
import java.math.RoundingMode;



public class Util {
    public static double round(double value) {
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
