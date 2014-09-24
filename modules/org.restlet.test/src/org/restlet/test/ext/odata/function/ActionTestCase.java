package org.restlet.test.ext.odata.function;


import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.restlet.Component;
import org.restlet.Context;
import org.restlet.data.Protocol;
import org.restlet.ext.xml.format.FormatType;
import org.restlet.test.RestletTestCase;

/**
 * Test case for actions in Restlet.
 */
public class ActionTestCase extends RestletTestCase {

    /** Inner component. */
    private Component component = new Component();

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        component.getServers().add(Protocol.HTTP, 8111);
        component.getClients().add(Protocol.CLAP);
        component.getDefaultHost().attach("/Unit.svc",
                new UnitApplication());
        component.start();
    }

    @Override
    protected void tearDown() throws Exception {
        component.stop();
        component = null;
        super.tearDown();
    }
    
    /**
     * Test action atom.
     */
    public void testActionAtom() {
    	FunctionService service = new FunctionService();
  		testAction(service);
      }
    
    /**
     * Test action json.
     */
    public void testActionJson() {
    	FunctionService service = new FunctionService();
    	service.setFormatType(FormatType.JSONVERBOSE);
  		testAction(service);
      }

	private void testAction(FunctionService service) {
		List<Double> values = null;
  		try {
  			List<Double> doubleList = new ArrayList<Double>();
  			doubleList.add(240.0);
  			doubleList.add(450.0);
  			 values = service.convertDoubleArray("1", "2", doubleList, new Double(1D));  			
  		} catch (Exception e) {
			Context.getCurrentLogger().warning(
                    "Exception occurred while calling a function: " + e.getMessage());
  			Assert.fail();
  		} 	
  		assertNotNull(values);
  		assertTrue(values.size()>0);
  		assertEquals(20.0d, values.get(0));
  		assertEquals(65.6d, values.get(1));
	}

}
