package com.surveys.surveys.model;

/**
 * Configuración visual y de marca para encuestas.
 * 
 * @author Juan David Valencia
 * @version 1.0
 * @since 2025-07-22
 */
public class Branding {
    private String logoUrl;
    private String primaryColor;
    private String font;

    public String getLogoUrl() { return logoUrl; }
    public void setLogoUrl(String logoUrl) { this.logoUrl = logoUrl; }
    
    public String getPrimaryColor() { return primaryColor; }
    public void setPrimaryColor(String primaryColor) { this.primaryColor = primaryColor; }
    
    public String getFont() { return font; }
    public void setFont(String font) { this.font = font; }
} 