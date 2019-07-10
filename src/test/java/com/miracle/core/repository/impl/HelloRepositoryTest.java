package com.miracle.core.repository.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.miracle.core.repository.HelloRepository;
import com.miracle.core.repository.HelloService;
import com.miracle.core.repository.impl.HelloRepositoryImpl;
import com.miracle.core.repository.impl.HelloServiceImpl;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;



//https://docs.spring.io/spring-boot/docs/2.1.2.RELEASE/reference/htmlsingle/#boot-features-testing-spring-boot-applications
@SpringBootTest
public class HelloRepositoryTest {
    
    @Mock
    HelloRepository helloRepository;
    
    @InjectMocks  
    private HelloRepositoryImpl helloRepositoryImpl;
    
    @InjectMocks  
    private HelloServiceImpl  helloServiceImpl;
    
    @BeforeEach
    public void init() {
    	when(helloRepository.get()).thenReturn("Hello JUnit 5");
    	//when(helloRepositoryImpl.get()).thenReturn("Hello JUnit 5");
    }
    
    @Test
    void testGet() {
    	String returnValue = helloRepositoryImpl.get();
    	String returnValue1 = helloServiceImpl.get();     	
    	assertNotNull(returnValue);
        assertEquals("Hello JUnit 5",returnValue);
        assertNotNull(returnValue1);
        assertEquals("Hello JUnit 5",returnValue1);
    }

}