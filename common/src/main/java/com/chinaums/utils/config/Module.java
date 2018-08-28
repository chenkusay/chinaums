package com.chinaums.utils.config;


import java.util.Properties;

public class Module
{
    private Properties properties;
    private String ID;
    private String name;
    private String IP;
    private int port;

    public String getID()
    {
        return this.ID;
    }
    public void setID(String ID) {
        this.ID = ID;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Properties getProperties() {
        return this.properties;
    }
    public void setProperties(Properties properties) {
        this.properties = properties;
    }
    public String getIP() {
        return this.IP;
    }
    public void setIP(String IP) {
        this.IP = IP;
    }
    public int getPort() {
        return this.port;
    }
    public void setPort(int port) {
        this.port = port;
    }

    public String getProperties(String key) {
        return this.properties.getProperty(key);
    }
}
