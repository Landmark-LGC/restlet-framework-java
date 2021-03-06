package org.restlet.test.ext.odata.crud;

import org.junit.Assert;
import org.restlet.Component;
import org.restlet.Context;
import org.restlet.Response;
import org.restlet.data.Protocol;
import org.restlet.data.Status;
import org.restlet.ext.odata.Query;
import org.restlet.ext.xml.format.FormatType;
import org.restlet.test.RestletTestCase;
import org.restlet.test.ext.odata.model.Cafe;

/**
 * Test case for OData service for CUD operation on entities.
 * 
 */
public class ODataSimpleCafeCrudTestCase extends RestletTestCase {

	/** Inner component. */
	private Component component = new Component();

	/** OData service used for all tests. */
	@SuppressWarnings("unused")
	private CafeService service;

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		component.getServers().add(Protocol.HTTP, 8111);
		component.getClients().add(Protocol.CLAP);
		component.getDefaultHost().attach("/Cafe.svc",
				new CafeCrudApplication());
		component.start();

		service = new CafeService();
	}

	@Override
	protected void tearDown() throws Exception {
		component.stop();
		component = null;
		service = null;
		super.tearDown();
	}

	/**
	 * Test method for crud operation on simple entities.
	 */
	public void testCrudSimpleEntity() {
		CafeService service = new CafeService();
		doCrudOperation(service);
	}
	
	/**
	 * Test crud simple entity json.
	 */
	public void testCrudSimpleEntityJson() {
		CafeService service = new CafeService();
		service.setFormatType(FormatType.JSONVERBOSE);
		doCrudOperation(service);
	}

	/**
	 * Do crud operation.
	 *
	 * @param service the service
	 */
	private void doCrudOperation(CafeService service) {
		// create.
		Cafe cafe = new Cafe();
		cafe.setId("30");
		cafe.setName("TestName");
		cafe.setCity("TestCity");
		cafe.setZipCode(111111);
		try {
			service.addEntity(cafe);
		} catch (Exception e) {
			Context.getCurrentLogger().warning(
                    "Cannot add entity due to: " + e.getMessage());
			Assert.fail();
		}

		Query<Cafe> query = service.createCafeQuery("/Cafes");
		Cafe cafe1 = query.iterator().next();
		assertEquals("TestName", cafe1.getName());
		assertEquals("30", cafe1.getId());
		assertEquals(new Integer(111111), cafe1.getZipCode());
		Response latestResponse = query.getService().getLatestResponse();
		assertEquals(Status.SUCCESS_OK, latestResponse.getStatus());
		// // Update.
		cafe1.setName("TestName-update");

		try {
			service.updateEntity(cafe1);
		} catch (Exception e) {
			Context.getCurrentLogger().warning(
                    "Cannot update entity due to: " + e.getMessage());
			Assert.fail();
		}

		Query<Cafe> query3 = service.createCafeQuery("/Cafes('30')");

		Cafe cafe2 = query3.iterator().next();
		assertEquals("TestName-updated", cafe2.getName());
		latestResponse = query3.getService().getLatestResponse();
		assertEquals(Status.SUCCESS_OK, latestResponse.getStatus());
		// Delete
		try {
			service.deleteEntity(cafe2);
		} catch (Exception e) {
			Context.getCurrentLogger().warning(
                    "Cannot delete entity due to: " + e.getMessage());
			Assert.fail();
		}
		latestResponse = query3.getService().getLatestResponse();
		assertEquals(Status.SUCCESS_NO_CONTENT, latestResponse.getStatus());
	}

}
