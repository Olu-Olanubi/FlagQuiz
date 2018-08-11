package com.example.user.flagquiz;

public class FlagModel {
    private String flagCode, flagName;

    public FlagModel(String flagCode, String flagName){
        this.flagCode = flagCode;
        this.flagName = flagName;
    }

    public String getFlagCode(){
        return flagCode;
    }

    public String getFlagName(){
        return flagName;
    }
}
