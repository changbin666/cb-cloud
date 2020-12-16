package com.cb.cloud.auth.common;

public class OAuth2Properties {
    private String clientid;
    private String secret;
    private int  tokenValidityInSeconds;

    public String getClientid() {
        return clientid;
    }
    public void setClientid(String clientid) {
        this.clientid = clientid;
    }
    public String getSecret() {
        return secret;
    }
    public void setSecret(String secret) {
        this.secret = secret;
    }
    public int getTokenValidityInSeconds() {
        return tokenValidityInSeconds;
    }
    public void setTokenValidityInSeconds(int tokenValidityInSeconds) {
        this.tokenValidityInSeconds = tokenValidityInSeconds;
    }
}
