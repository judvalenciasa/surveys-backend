package com.surveys.surveys.servicesimpl;

import com.surveys.surveys.model.Analytics;
import com.surveys.surveys.repository.AnalyticsRepository;
import com.surveys.surveys.services.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 * Implementación del servicio de análisis que proporciona
 * la lógica de negocio para el análisis estadístico de encuestas.
 *
 * @author Juan David Valencia
 * @version 1.0
 * @since 2025-07-22
 * @see AnalyticsService
 * @see Analytics
 */
@Service
public class AnalyticsServiceImpl implements AnalyticsService {

    /** Repositorio para operaciones con la base de datos */
    @Autowired
    private AnalyticsRepository analyticsRepository;

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException si el análisis es null
     */
    @Override
    public Analytics saveAnalytics(Analytics analytics) {
        if (analytics == null) {
            throw new IllegalArgumentException("El análisis no puede ser null");
        }
        return analyticsRepository.save(analytics);
    }

    /** {@inheritDoc} */
    @Override
    public Optional<Analytics> getAnalyticsById(String id) {
        return analyticsRepository.findById(id);
    }

    /** {@inheritDoc} */
    @Override
    public Optional<Analytics> getAnalyticsBySurveyId(String surveyId) {
        return analyticsRepository.findBySurveyId(surveyId);
    }

    /** {@inheritDoc} */
    @Override
    public List<Analytics> getHighPerformingSurveys(double minRate) {
        return analyticsRepository.findByCompletionRateGreaterThan(minRate);
    }

    /** {@inheritDoc} */
    @Override
    public Optional<Analytics> compareVersions(String currentVersionId, 
                                             String previousVersionId) {
        return analyticsRepository.findBySurveyId(currentVersionId)
            .filter(analytics -> 
                previousVersionId.equals(
                    analytics.getVersionComparison().getPreviousVersionId()
                )
            );
    }
}
