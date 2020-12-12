package com.ilecreurer.drools.samples.sample2;

import java.util.Date;

public class Test {

    public static void main(String[] args) {
        try {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyyMMdd HHmmss.SSSZ");
            String s = "20201129 112901.001+0100";
            Date d = sdf.parse(s);
            System.out.println(sdf.format(d));
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
