/**
 * Copyright 2005-2009 Noelios Technologies.
 * 
 * The contents of this file are subject to the terms of the following open
 * source licenses: LGPL 3.0 or LGPL 2.1 or CDDL 1.0 (the "Licenses"). You can
 * select the license that you prefer but you may not use this file except in
 * compliance with one of these Licenses.
 * 
 * You can obtain a copy of the LGPL 3.0 license at
 * http://www.gnu.org/licenses/lgpl-3.0.html
 * 
 * You can obtain a copy of the LGPL 2.1 license at
 * http://www.gnu.org/licenses/lgpl-2.1.html
 * 
 * You can obtain a copy of the CDDL 1.0 license at
 * http://www.sun.com/cddl/cddl.html
 * 
 * See the Licenses for the specific language governing permissions and
 * limitations under the Licenses.
 * 
 * Alternatively, you can obtain a royalty free commercial license with less
 * limitations, transferable or non-transferable, directly at
 * http://www.noelios.com/products/restlet-engine
 * 
 * Restlet is a registered trademark of Noelios Technologies.
 */

package org.restlet.engine.converter;

import java.util.List;

import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.UniformResource;

/**
 * Converter between the DOM API and Representation classes.
 * 
 * @author Jerome Louvel
 */
public class DomConverter extends ConverterHelper {

    @Override
    public List<Class<?>> getObjectClasses(Variant variant) {
        return null;
    }

    @Override
    public List<Variant> getVariants(Class<?> objectClass) {
        return null;
    }

    @Override
    public <T> T toObject(UniformResource resource,
            Representation representation, Class<T> objectClass) {
        return null;
    }

    @Override
    public Representation toRepresentation(UniformResource resource,
            Object object, Variant variant) {
        return null;
    }

}
