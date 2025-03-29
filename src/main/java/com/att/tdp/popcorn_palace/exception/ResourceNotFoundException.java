package com.att.tdp.popcorn_palace.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resourceType, String identifier, String value) {
        super(String.format("%s not found with %s: %s", resourceType, identifier, value));
    }
} 