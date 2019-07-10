package com.miracle;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SamepleTestCases {
	
	@Mock
	List<String> mockList; 
	
	@Test
	public void whenNotUseMockAnnotation_thenCorrect() {
			     
	    mockList.add("one");
	    Mockito.verify(mockList).add("one");
	    assertEquals(0, mockList.size());
	 
	    Mockito.when(mockList.size()).thenReturn(100);
	    assertEquals(100, mockList.size());
	}

}
