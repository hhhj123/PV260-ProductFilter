/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.pv260.productfilter;

import org.junit.Assert;
import org.junit.Test;
import static org.mockito.Mockito.*;

/**
 *
 * @author Simon
 */
public class AtLeastNOfFilterTest { 
    
    @Test(expected=IllegalArgumentException.class)
        public void testConstructorIllegalArgumentException1() {
        int n = 0;
        AtLeastNOfFilter atLeastNOfFilter = spy(new AtLeastNOfFilter(n, mock(Filter.class)));   
    }   
    
    @Test(expected=IllegalArgumentException.class)
        public void testConstructorIllegalArgumentException2() {
        int n = -1;
        AtLeastNOfFilter atLeastNOfFilter = new AtLeastNOfFilter(n, mock(Filter.class));   
    } 
        
    @Test(expected=IllegalArgumentException.class)
        public void testConstructorIllegalArgumentException3() {
        int n = Integer.MIN_VALUE;
        AtLeastNOfFilter atLeastNOfFilter = new AtLeastNOfFilter(n, mock(Filter.class));   
    }
    
    @Test(expected=FilterNeverSucceeds.class)
        public <T> void testConstructorFilterNeverSucceeds() {
        Filter<T> filterMock = mock(Filter.class);
        Filter[] arrayFilterMock = {filterMock, filterMock, filterMock, filterMock};
        int n = arrayFilterMock.length + 1;
        
        AtLeastNOfFilter atLeastNOfFilter = new AtLeastNOfFilter(n, arrayFilterMock);         
    }
      
    @Test
    public void testFilterPasses() {
        Filter filterMock = mock(Filter.class);
        Filter[] arrayFilterMock = {filterMock, filterMock, filterMock, filterMock};
        int n = arrayFilterMock.length;
        
        AtLeastNOfFilter atLeastNOfFilter = spy(new AtLeastNOfFilter(n, arrayFilterMock)); 
        
        when(filterMock.passes(any())).thenReturn(true);
        
        Assert.assertTrue(atLeastNOfFilter.passes(any()));  
    }
    
    @Test
    public void testFilterPasses2() {
        Filter filterMock = mock(Filter.class);
        Filter[] arrayFilterMock = {filterMock, filterMock, filterMock, filterMock};
        int n = arrayFilterMock.length - 1;
        
        AtLeastNOfFilter atLeastNOfFilter = spy(new AtLeastNOfFilter(n, arrayFilterMock)); 
        
        when(filterMock.passes(any())).thenReturn(true);
        
        Assert.assertTrue(atLeastNOfFilter.passes(any())); 
    }
    
    @Test
    public void testFilterFail() {
        Filter filterMock = mock(Filter.class);
        Filter[] arrayFilterMock = {filterMock, filterMock, filterMock, filterMock};
        int n = arrayFilterMock.length;
        
        AtLeastNOfFilter atLeastNOfFilter = spy(new AtLeastNOfFilter(n, arrayFilterMock)); 
        
        when(filterMock.passes(any())).thenReturn(false).thenReturn(true);
  
        Assert.assertFalse(atLeastNOfFilter.passes(any()));
    }
}
