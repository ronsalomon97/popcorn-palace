package com.att.tdp.popcorn_palace.exception;

public class DuplicateMovieTitleException extends RuntimeException {
    public DuplicateMovieTitleException(String title) {
        super("Movie with title '" + title + "' already exists");
    }
} 