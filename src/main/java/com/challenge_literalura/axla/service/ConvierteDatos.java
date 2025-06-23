package com.challenge_literalura.axla.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class ConvierteDatos {
    private ObjectMapper objectMapper = new ObjectMapper();
    public <T> T obtenerDatos(String json, Class<T> clase){
        try {
            return objectMapper.readValue(json, clase);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error al convertir JSON", e);
        }
    }
}
