/**
 * Copyright 2005-2009 Noelios Technologies.
 * 
 * The contents of this file are subject to the terms of one of the following
 * open source licenses: LGPL 3.0 or LGPL 2.1 or CDDL 1.0 or EPL 1.0 (the
 * "Licenses"). You can select the license that you prefer but you may not use
 * this file except in compliance with one of these Licenses.
 * 
 * You can obtain a copy of the LGPL 3.0 license at
 * http://www.opensource.org/licenses/lgpl-3.0.html
 * 
 * You can obtain a copy of the LGPL 2.1 license at
 * http://www.opensource.org/licenses/lgpl-2.1.php
 * 
 * You can obtain a copy of the CDDL 1.0 license at
 * http://www.opensource.org/licenses/cddl1.php
 * 
 * You can obtain a copy of the EPL 1.0 license at
 * http://www.opensource.org/licenses/eclipse-1.0.php
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
 * Converter between Representations and regular Java objects.
 * 
 * @author Jerome Louvel
 */
public abstract class ConverterHelper {

    /**
     * Returns the list of object classes that can be converted from a given
     * variant.
     * 
     * @param variant
     *            The source variant.
     * @return The list of object class that can be converted.
     */
    public abstract List<Class<?>> getObjectClasses(Variant variant);

    /**
     * Returns the list of variants that can be converted from a given object
     * class.
     * 
     * @param objectClass
     *            The source object class.
     * @return The list of variants that can be converted.
     */
    public abstract List<Variant> getVariants(Class<?> objectClass);

    /**
     * Converts a Representation into a regular Java object.
     * 
     * @param <T>
     *            The expected class of the Java object.
     * @param representation
     *            The source representation to convert.
     * @param targetClass
     *            The expected class of the Java object.
     * @param resource
     *            The calling resource.
     * @return The converted Java object.
     */
    public abstract <T> T toObject(Representation representation,
            Class<T> targetClass, UniformResource resource);

    /**
     * Converts a regular Java object into a Representation.
     * 
     * @param object
     *            The source object to convert.
     * @param targetVariant
     *            The expected representation metadata.
     * @param resource
     *            The calling resource.
     * @return The converted representation.
     */
    public abstract Representation toRepresentation(Object object,
            Variant targetVariant, UniformResource resource);

}
