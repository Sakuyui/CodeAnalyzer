package com.akb.codeanlyzer.network;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SocketRequest {
    public int reqType;
    public String uid;
    public int seq;
    public String response = "";
    public boolean hasResponse = false;
    public Map<String, Object> sessionData = new ConcurrentHashMap<>();
    public SocketRequest(int reqType, String uid, int seq){
        this.reqType = reqType;
        this.uid = uid;
        this.seq = seq;
    }


}
