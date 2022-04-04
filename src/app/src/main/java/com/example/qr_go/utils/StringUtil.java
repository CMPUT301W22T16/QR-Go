package com.example.qr_go.utils;

public class StringUtil {
    public String ImageQRRef(String qrid, String playerid){return String.format("GameQR/%s/%s.jpg", qrid, playerid);}
    public String ImagePlayerRef(String playerid){return String.format("Player/%s.jpg", playerid);}
}
