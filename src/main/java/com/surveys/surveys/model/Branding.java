package com.surveys.surveys.model;

/**
 * Representa la configuración visual y de marca de una encuesta.
 * Esta clase contiene los elementos necesarios para personalizar
 * la apariencia de una encuesta, incluyendo logo, colores y tipografía.
 *
 * @author Juan David Valencia
 * @version 1.0
 * @since 2025-07-22
 */
public class Branding {
    /** URL del logotipo que se mostrará en la encuesta */
    private String logoUrl;
    
    /** Color principal en formato hexadecimal (ej: "#FF9900") */
    private String primaryColor;
    
    /** Nombre de la fuente tipográfica a utilizar */
    private String font;

    /**
     * Obtiene la URL del logotipo.
     * @return la URL del logotipo como String
     */
    public String getLogoUrl() { return logoUrl; }

    /**
     * Establece la URL del logotipo.
     * @param logoUrl la URL del logotipo a establecer
     */
    public void setLogoUrl(String logoUrl) { this.logoUrl = logoUrl; }
    
    /**
     * Obtiene el color principal.
     * @return el color en formato hexadecimal
     */
    public String getPrimaryColor() { return primaryColor; }

    /**
     * Establece el color principal.
     * @param primaryColor el color en formato hexadecimal (ej: "#FF9900")
     */
    public void setPrimaryColor(String primaryColor) { this.primaryColor = primaryColor; }
    
    /**
     * Obtiene el nombre de la fuente.
     * @return el nombre de la fuente tipográfica
     */
    public String getFont() { return font; }

    /**
     * Establece la fuente tipográfica.
     * @param font el nombre de la fuente a utilizar
     */
    public void setFont(String font) { this.font = font; }
} 