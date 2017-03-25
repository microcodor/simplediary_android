package com.wxdroid.simplediary.entity.list;

import com.google.gson.Gson;
import com.wxdroid.simplediary.entity.Token;

import java.util.ArrayList;

/**
 * Created by jinchun on 16/5/18.
 */
public class TokenList {
    public ArrayList<Token> tokenList = new ArrayList<Token>();
    public int total_number;
    public String current_uid;


    public static TokenList parse(String jsonString) {
        Gson gson = new Gson();
        TokenList tokenList = gson.fromJson(jsonString, TokenList.class);
        if (tokenList == null) {
            
        }
        return tokenList;
    }

}
