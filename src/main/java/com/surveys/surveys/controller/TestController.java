package com.surveys.surveys.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.bson.Document;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @Autowired
    private MongoTemplate mongoTemplate;

    @GetMapping("/mongo")
    public String testMongo() {
        try {
            Document testDoc = new Document();
            testDoc.put("mensaje", "Prueba de conexión");
            testDoc.put("fecha", new java.util.Date());
            
            mongoTemplate.insert(testDoc, "test_collection");
            
            // Intentar leer el documento recién insertado
            Document found = mongoTemplate.findOne(
                new org.springframework.data.mongodb.core.query.Query(),
                Document.class,
                "test_collection"
            );
            
            return "Documento insertado y leído correctamente: " + found.toJson();
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}
