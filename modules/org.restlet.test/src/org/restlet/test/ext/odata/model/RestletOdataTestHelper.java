package org.restlet.test.ext.odata.model;

import java.util.List;

import org.restlet.Request;
import org.restlet.data.MediaType;
import org.restlet.data.Preference;

/**
 * The Class RestletOdataTestHelper.
 */
public class RestletOdataTestHelper {
	/**
	 * Gets the media type.
	 *
	 * @param request the request
	 * @return the media type
	 */
	public static String getMediaType(Request request) {
		List<Preference<MediaType>> acceptedMediaTypes = request.getClientInfo().getAcceptedMediaTypes();
		for (Preference<MediaType> preference : acceptedMediaTypes) {
			MediaType mediaType = preference.getMetadata();
			String mediaTypeName = mediaType.getName().trim();
			mediaTypeName=mediaTypeName.replaceAll("\\s", "");
			if(mediaTypeName.equalsIgnoreCase(MediaType.APPLICATION_JSONVERBOSE.getName())){
				return ".json";
			}
			if(mediaTypeName.equalsIgnoreCase(MediaType.APPLICATION_ATOM.getName())){
				return ".xml";
			}
		}
		return  null;
	}
}
