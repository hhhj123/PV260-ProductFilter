/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.pv260.productfilter;

import static cz.muni.fi.pv260.productfilter.Controller.TAG_CONTROLLER;
import static java.util.Arrays.asList;
import java.util.Collection;
import org.junit.Test;
import static org.mockito.Mockito.*;


public class ControllerTest {
    private Collection<Product> anyProductCollection() {
        return anyCollection();
    }
    
    @Test
    public void testControllerProductsToOutput() {
        Input inputMock = mock(Input.class);
        Output outputMock = mock(Output.class);
        Filter filterMock = mock(Filter.class);
        Product productMock1 = mock(Product.class);
        Product productMock2 = mock(Product.class);
        
        Controller controller = new Controller(inputMock, outputMock, mock(Logger.class));
        when(filterMock.passes(productMock1)).thenReturn(false);
        when(filterMock.passes(productMock2)).thenReturn(true);
        
        try {
            when(inputMock.obtainProducts()).thenReturn(asList(productMock1, productMock2));
        } catch(Exception e) {
            
        }
        
        controller.select(filterMock);

        verify(outputMock).postSelectedProducts(asList(productMock2));    
    }
    
    @Test
    public void testControllerLogMessage() {
        Input inputMock = mock(Input.class);
        Logger loggerMock = mock(Logger.class);
        Filter filterMock = mock(Filter.class);
        Product productMock = mock(Product.class);
        
        Controller controller = new Controller(inputMock, mock(Output.class), loggerMock);
        when(filterMock.passes(productMock)).thenReturn(true);
        
        try {
            when(inputMock.obtainProducts()).thenReturn(asList(productMock));
        } catch(Exception e) {
            
        }
        
        controller.select(filterMock);

        verify(loggerMock).setLevel("INFO");
        verify(loggerMock).log(TAG_CONTROLLER,
                           "Successfully selected 1 out of 1 available products.");
    }
   
    @Test
    public void testControllerLogExceptionFromObtainProduct() {
        Input inputMock = mock(Input.class);
        Logger loggerMock = mock(Logger.class);
        Exception exception = new ObtainFailedException();
        
        Controller controller = new Controller(inputMock, mock(Output.class), loggerMock);
      
        
        try {
           when(inputMock.obtainProducts()).thenThrow(exception); 
        } catch (Exception e) {
            
        }

        controller.select(mock(Filter.class));
        
        verify(loggerMock).setLevel("ERROR");
        verify(loggerMock).log(TAG_CONTROLLER, "Filter procedure failed with exception: " + exception);
    }
    
    @Test
    public void testControllerNothingToOutputExceptionFromObtainProduct() {
        Input inputMock = mock(Input.class);
        Output outputMock = mock(Output.class);
        
        Controller controller = new Controller(inputMock, outputMock, mock(Logger.class));
     
        try {
           when(inputMock.obtainProducts()).thenThrow(new ObtainFailedException()); 
        } catch (Exception e) {
            
        }

        controller.select(mock(Filter.class));
        
        verify(outputMock, never()).postSelectedProducts(anyProductCollection());
    }
}
