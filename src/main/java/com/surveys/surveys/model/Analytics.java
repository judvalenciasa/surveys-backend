package com.surveys.surveys.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;
import java.util.Map;

/**
 * Representa los análisis estadísticos de una encuesta.
 * Esta clase almacena métricas detalladas sobre el rendimiento y resultados
 * de una encuesta, incluyendo tasas de finalización, tiempos promedio y
 * distribución de respuestas.
 *
 * <p>La estructura incluye:
 * <ul>
 *   <li>Visión general de la encuesta</li>
 *   <li>Análisis por pregunta</li>
 *   <li>Comparación con versiones anteriores</li>
 * </ul>
 *
 * @author Juan David Valencia
 * @version 1.0
 * @since 2025-07-22
 */
@Document(collection = "analytics")
public class Analytics {
    
    @Id
    private String id;
    private String surveyId;
    private Overview overview;
    private List<QuestionAnalytic> questionAnalytics;
    private VersionComparison versionComparison;

    /**
     * Representa la visión general de las métricas de la encuesta.
     */
    public static class Overview {
        private double completionRate;
        private int averageTimeMinutes;
        private List<String> abandonmentPoints;

        // Getters y Setters
        public double getCompletionRate() { return completionRate; }
        public void setCompletionRate(double completionRate) { 
            this.completionRate = completionRate; 
        }

        public int getAverageTimeMinutes() { return averageTimeMinutes; }
        public void setAverageTimeMinutes(int averageTimeMinutes) { 
            this.averageTimeMinutes = averageTimeMinutes; 
        }

        public List<String> getAbandonmentPoints() { return abandonmentPoints; }
        public void setAbandonmentPoints(List<String> abandonmentPoints) { 
            this.abandonmentPoints = abandonmentPoints; 
        }
    }

    /**
     * Representa el análisis estadístico de una pregunta específica.
     */
    public static class QuestionAnalytic {
        private String questionId;
        private Map<String, Integer> distribution;

        public String getQuestionId() { return questionId; }
        public void setQuestionId(String questionId) { 
            this.questionId = questionId; 
        }

        public Map<String, Integer> getDistribution() { return distribution; }
        public void setDistribution(Map<String, Integer> distribution) { 
            this.distribution = distribution; 
        }
    }

    /**
     * Representa la comparación con una versión anterior de la encuesta.
     */
    public static class VersionComparison {
        private String previousVersionId;
        private Map<String, String> changes;

        public String getPreviousVersionId() { return previousVersionId; }
        public void setPreviousVersionId(String previousVersionId) { 
            this.previousVersionId = previousVersionId; 
        }

        public Map<String, String> getChanges() { return changes; }
        public void setChanges(Map<String, String> changes) { 
            this.changes = changes; 
        }
    }

    // Constructor por defecto
    public Analytics() {}

    // Getters y Setters principales
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getSurveyId() { return surveyId; }
    public void setSurveyId(String surveyId) { this.surveyId = surveyId; }

    public Overview getOverview() { return overview; }
    public void setOverview(Overview overview) { this.overview = overview; }

    public List<QuestionAnalytic> getQuestionAnalytics() { return questionAnalytics; }
    public void setQuestionAnalytics(List<QuestionAnalytic> questionAnalytics) { 
        this.questionAnalytics = questionAnalytics; 
    }

    public VersionComparison getVersionComparison() { return versionComparison; }
    public void setVersionComparison(VersionComparison versionComparison) { 
        this.versionComparison = versionComparison; 
    }
}
