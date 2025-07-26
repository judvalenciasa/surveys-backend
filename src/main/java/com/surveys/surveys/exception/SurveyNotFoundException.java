package com.surveys.surveys.exception;

public class SurveyNotFoundException extends RuntimeException {
    private final String errorCode;
    
    public SurveyNotFoundException(Long surveyId) {
        super(ErrorCodes.SURVEY_NOT_FOUND + ": Survey with ID " + surveyId + " not found");
        this.errorCode = ErrorCodes.SURVEY_NOT_FOUND;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
} 