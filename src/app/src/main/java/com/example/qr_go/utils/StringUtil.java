package com.example.qr_go.utils;

public class StringUtil {
    /**
     * image reference
     * @param qrid qrid
     * @param playerid playerid
     * @return  combined string reference
     * @athur Darius Fang
     */
    public String ImageQRRef(String qrid, String playerid){return String.format("GameQR/%s/%s.jpg", qrid, playerid);}

    /**
     * image reference
     * @param playerid playerid
     * @return string reference
     * @athur Darius Fang
     */
    public String ImagePlayerRef(String playerid){return String.format("Player/%s.jpg", playerid);}
}
