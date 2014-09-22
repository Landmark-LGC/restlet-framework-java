package org.restlet.test.ext.odata.function;

import java.util.ArrayList;
import java.util.List;

import org.restlet.data.Parameter;
import org.restlet.ext.odata.Service;
import org.restlet.ext.odata.internal.FunctionContentHandler;
import org.restlet.util.Series;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Generated for the WCF Data Services extension for the
 * Restlet framework.<br> 
 */
public class FunctionService extends Service {

	/**
	 * Constructor. 
	 */
	public FunctionService() {
		super("http://localhost:8111/Unit.svc");
	}

	public FunctionService(FunctionContentHandler functionContentHandler) {
		super("http://localhost:8111/Unit.svc");
	}

	public Nextval_t nextval(String tableName) {
		Nextval_t nextval = null;
		Series<Parameter> parameters = new Series<Parameter>(Parameter.class);
		Parameter paramtableName = new Parameter();
		paramtableName.setName("tableName");
		paramtableName.setValue(tableName.toString());
		parameters.add(paramtableName);
		nextval = (Nextval_t) invokeFunction("nextval", parameters, Nextval_t.class, nextval);
		return nextval;
	}

	@SuppressWarnings("unchecked")
	public List<java.lang.Double> convertDoubleArray(String from, String to,
			List<java.lang.Double> value, Double nullValue) {
		List<java.lang.Double> convertDoubleArray = new ArrayList<java.lang.Double>();
		Series<Parameter> parameters = new Series<Parameter>(Parameter.class);
		Parameter paramfrom = new Parameter();
		paramfrom.setName("from");
		paramfrom.setValue(from.toString());
		parameters.add(paramfrom);
		Parameter paramto = new Parameter();
		paramto.setName("to");
		paramto.setValue(to.toString());
		parameters.add(paramto);
		Parameter paramvalue = new Parameter();
		paramvalue.setName("value");
		Gson gson = new GsonBuilder().serializeNulls()
				.serializeSpecialFloatingPointValues().create();
		String jsonValue = gson.toJson(value);
		paramvalue.setValue(jsonValue);
		parameters.add(paramvalue);
		Parameter paramnullValue = new Parameter();
		paramnullValue.setName("nullValue");
		paramnullValue.setValue(nullValue.toString());
		parameters.add(paramnullValue);
		convertDoubleArray = (List<java.lang.Double>) invokeFunction("convertDoubleArray", parameters, java.lang.Double.class, convertDoubleArray);
		return convertDoubleArray;
	}
}
