package org.restlet.test.ext.odata.streamcrud;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ConcurrentMap;

import org.restlet.Application;
import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.CharacterSet;
import org.restlet.data.LocalReference;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Protocol;
import org.restlet.data.Status;
import org.restlet.engine.header.HeaderConstants;
import org.restlet.representation.InputRepresentation;
import org.restlet.representation.Representation;
import org.restlet.routing.Router;
import org.restlet.util.NamedValue;
import org.restlet.util.Series;

/**
 * Sample application that simulates the CUD operation on MLE entities.
 * 
 */
public class CafeCrudApplication extends Application {

	private static class MyClapRestlet extends Restlet {
		String file;	

		public MyClapRestlet(Context context, String file, boolean updatable) {
			super(context);
			this.file = file;
		}

		@SuppressWarnings("unchecked")
		@Override
		public void handle(Request request, Response response) {
			if (Method.GET.equals(request.getMethod())) {
				
				String uri = "/"
						+ this.getClass().getPackage().getName()
								.replace(".", "/") + "/" + file;
				//check for request is for MLE 
				if (request.getOriginalRef().toString().contains("attachment")) {
					InputStream inputStream = null;
					String str = "TEST";
					// convert String into InputStream
					inputStream = new ByteArrayInputStream(str.getBytes());

					Representation result = new InputRepresentation(
							inputStream, MediaType.APPLICATION_OCTET_STREAM);
					response.setEntity(result);
					response.setStatus(Status.SUCCESS_OK);

				} else {
					Response r = getContext().getClientDispatcher().handle(
							new Request(Method.GET, LocalReference
									.createClapReference(
											LocalReference.CLAP_CLASS, uri
													+ ".xml")));
					response.setEntity(r.getEntity());
					response.setStatus(r.getStatus());
				}
			} else if (Method.POST.equals(request.getMethod())) {
				// read request header to get slug header and also to get merge
				// request
				String rep = null;
				try {
					rep = request.getEntity().getText();
				} catch (IOException e) {
					response.setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
				}
				if (null != rep && !rep.isEmpty()) {
					response.setStatus(Status.SUCCESS_OK);
				}
			} else if (Method.DELETE.equals(request.getMethod())) {
				response.setStatus(Status.SUCCESS_NO_CONTENT);
			} else if(Method.PUT.equals(request.getMethod())){
				response.setStatus(Status.SUCCESS_OK);
			}
		}
	}

	@Override
	public Restlet createInboundRoot() {
		getMetadataService().setDefaultCharacterSet(CharacterSet.ISO_8859_1);
		getConnectorService().getClientProtocols().add(Protocol.CLAP);
		Router router = new Router(getContext());

		router.attach("/$metadata", new MyClapRestlet(getContext(), "metadata",
				true));
		router.attach("/Cafes", new MyClapRestlet(getContext(), "cafes", true));
		router.attach("/Cafes('30')", new MyClapRestlet(getContext(), "cafes_Updated",
				true));
		router.attach("/Cafes('30')/attachment", new MyClapRestlet(getContext(),
				"streamData", true));
		return router;
	}

}
