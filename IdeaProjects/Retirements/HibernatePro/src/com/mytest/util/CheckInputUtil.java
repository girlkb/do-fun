package com.mytest.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class CheckInputUtil {
    /**
     * 正则表达式限制用户输入
     * @param input
     *
     */
    public static boolean checkInput(String input){
        String reg = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$";
        Pattern pattern = Pattern.compile(reg);
        Matcher m = pattern.matcher(input);
        if(!m.matches()){ //匹配不到,說明輸入的不符合條件
            return false;
        }
        return true;
    }

    public static boolean checkUUID(String uuid){
        return true;
    }

}
