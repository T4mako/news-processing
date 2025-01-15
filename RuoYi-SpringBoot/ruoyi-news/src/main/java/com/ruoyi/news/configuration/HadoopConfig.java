package com.ruoyi.news.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "hadoop")
public class HadoopConfig {
    private String hdfsUrl;
    private String hdfsDir;
    private String localDir;

    public String getHdfsUrl() {
        return hdfsUrl;
    }

    public void setHdfsUrl(String hdfsUrl) {
        this.hdfsUrl = hdfsUrl;
    }

    public String getHdfsDir() {
        return hdfsDir;
    }

    public void setHdfsDir(String hdfsDir) {
        this.hdfsDir = hdfsDir;
    }

    public String getLocalDir() {
        return localDir;
    }

    public void setLocalDir(String localDir) {
        this.localDir = localDir;
    }
}