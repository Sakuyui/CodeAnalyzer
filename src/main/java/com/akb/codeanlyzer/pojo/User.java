package com.akb.codeanlyzer.pojo;

public class User {
    private String userName;
    private String pwd;
    private String uIcon;
    private String nickName;
    private String uri;
    private String random;
    private String hashPwd;

    public String getHashPwd() {
        return hashPwd;
    }

    public void setHashPwd(String hashPwd) {
        this.hashPwd = hashPwd;
    }

    public String getRandom() {
        return random;
    }

    public void setRandom(String random) {
        this.random = random;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getUri() {
        return uri;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUserName() {
        return userName;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getuIcon() {
        return uIcon;
    }

    public void setuIcon(String uIcon) {
        this.uIcon = uIcon;
    }
}
