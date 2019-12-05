package com.main;

import com.classes.Util;

public class Main {

    public static void main(String[] args) {
        try{
            // java programme.java -a "JeuDeDonnee"
            long begin_time = System.nanoTime();
            Util.ResolveBinIm("JeuDeDonnees"/*args[0]*/, "list"/*args[1]*/,  false);
            long end_time = System.nanoTime();
            System.out.println((end_time - begin_time) / 100000  + " ms");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
