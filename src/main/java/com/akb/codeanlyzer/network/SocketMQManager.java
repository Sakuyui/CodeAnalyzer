package com.akb.codeanlyzer.network;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;


public class SocketMQManager {
    //key seq
    ConcurrentHashMap<Integer, SocketRequest> socketRequestConcurrentHashMap = new ConcurrentHashMap<>();

    private static AtomicInteger seq = new AtomicInteger(0);
    public int getNewSequenceNumber(){
        return seq.getAndIncrement() % Integer.MAX_VALUE;
    }
    public int createNewRequest(int reqType, String uid){
        int seq =  getNewSequenceNumber();
        socketRequestConcurrentHashMap.put(seq, new SocketRequest(reqType ,uid, seq));
        return seq;
    }
    public Object get(int seq, String key){
        if(socketRequestConcurrentHashMap.containsKey(seq)){
            return socketRequestConcurrentHashMap.get(seq).sessionData.get(key);
        }
        return null;
    }

    public void set(int seq, String key, Object val){
        if(socketRequestConcurrentHashMap.containsKey(seq)){
            socketRequestConcurrentHashMap.get(seq).sessionData.put(key, val);
        }
        return;
    }
    public void removeBySeq(int seq){
        if(socketRequestConcurrentHashMap.containsKey(seq))
            socketRequestConcurrentHashMap.remove(seq);
    }
    public void response(int seq, String content){
        if(socketRequestConcurrentHashMap.containsKey(seq)){
            SocketRequest socketRequest = socketRequestConcurrentHashMap.get(seq);
            socketRequest.response = content;
            socketRequest.hasResponse = true;
        }
    }

    public SocketRequest getResponse(int seq){
        if(socketRequestConcurrentHashMap.containsKey(seq)){
            SocketRequest sr = socketRequestConcurrentHashMap.get(seq);
            return  sr.hasResponse ? sr : null;
        }
        return null;
    }



}
