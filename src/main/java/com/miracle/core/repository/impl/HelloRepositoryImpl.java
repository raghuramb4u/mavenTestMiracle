package com.miracle.core.repository.impl;

import org.springframework.stereotype.Repository;

import com.miracle.core.repository.HelloRepository;

@Repository
public class HelloRepositoryImpl implements HelloRepository {
    @Override
    public String get() {
        return "Hello JUnit 5";
    }
}
