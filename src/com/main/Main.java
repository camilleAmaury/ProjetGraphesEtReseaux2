package com.main;

import com.classes.Util;

public class Main {

    public static void main(String[] args) {
        try{
            // java programme.java -a "JeuDeDonnee"
            Util.ResolveBinIm(args[0], false);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
