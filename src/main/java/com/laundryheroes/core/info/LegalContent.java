package com.laundryheroes.core.info;
public class LegalContent {

    private String version;
    private String lastUpdated;
    private String content;

    public LegalContent(String version, String lastUpdated, String content) {
        this.version = version;
        this.lastUpdated = lastUpdated;
        this.content = content;
    }

    public String getVersion() { return version; }
    public String getLastUpdated() { return lastUpdated; }
    public String getContent() { return content; }
}
