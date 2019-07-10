package com.miracle.core.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.miracle.core.repository.HelloService;
import com.miracle.core.repository.impl.HelloRepositoryImpl;
import com.miracle.core.repository.impl.HelloServiceImpl;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;



//https://docs.spring.io/spring-boot/docs/2.1.2.RELEASE/reference/htmlsingle/#boot-features-testing-spring-boot-applications
@SpringBootTest
public class HelloServiceTest {

    @Mock
    HelloService helloService;
    
    @Mock
    HelloRepositoryImpl helloRepository;
    
    @InjectMocks  
    private HelloServiceImpl helloServiceImpl;
    
    @BeforeEach
    public void init() {
    	when(helloRepository.get()).thenReturn("Hello JUnit 5");
    	when(helloService.get()).thenReturn("Hello JUnit 5");
    	when(helloServiceImpl.get()).thenReturn("Hello JUnit 5");    	
    }

    @DisplayName("Test Spring @Autowired Integration")
    @Test
    void testGet() {
    	String returnValue = helloServiceImpl.get();    	
    	assertNotNull(returnValue);
        assertEquals("Hello JUnit 5",returnValue);
    }

}