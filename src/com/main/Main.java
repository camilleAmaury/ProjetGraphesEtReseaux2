package com.main;

import com.classes.Util;

public class Main {

    public static void main(String[] args) {
        try{
            long begin_time = System.nanoTime();
            Util.ResolveBinIm(args[0], args[1],  false);
            long end_time = System.nanoTime();
            System.out.println((end_time - begin_time) / 1000000  + " ms");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
