/**
 * Copyright 2005-2014 Restlet
 * 
 * The contents of this file are subject to the terms of one of the following
 * open source licenses: Apache 2.0 or LGPL 3.0 or LGPL 2.1 or CDDL 1.0 or EPL
 * 1.0 (the "Licenses"). You can select the license that you prefer but you may
 * not use this file except in compliance with one of these Licenses.
 * 
 * You can obtain a copy of the Apache 2.0 license at
 * http://www.opensource.org/licenses/apache-2.0
 * 
 * You can obtain a copy of the LGPL 3.0 license at
 * http://www.opensource.org/licenses/lgpl-3.0
 * 
 * You can obtain a copy of the LGPL 2.1 license at
 * http://www.opensource.org/licenses/lgpl-2.1
 * 
 * You can obtain a copy of the CDDL 1.0 license at
 * http://www.opensource.org/licenses/cddl1
 * 
 * You can obtain a copy of the EPL 1.0 license at
 * http://www.opensource.org/licenses/eclipse-1.0
 * 
 * See the Licenses for the specific language governing permissions and
 * limitations under the Licenses.
 * 
 * Alternatively, you can obtain a royalty free commercial license with less
 * limitations, transferable or non-transferable, directly at
 * http://www.restlet.com/products/restlet-framework
 * 
 * Restlet is a registered trademark of Restlet
 */

package org.restlet.test.data;

import org.restlet.data.Language;
import org.restlet.test.RestletTestCase;

/**
 * Test {@link org.restlet.data.Language}.
 * 
 * @author Jerome Louvel
 */
public class LanguageTestCase extends RestletTestCase {

    /**
     * Testing {@link Language#valueOf(String)}
     */
    public void testValueOf() {
        assertSame(Language.FRENCH_FRANCE, Language.valueOf("fr-fr"));
        assertSame(Language.ALL, Language.valueOf("*"));
    }

    public void testUnmodifiable() {
        try {
            Language.FRENCH_FRANCE.getSubTags().add("foo");
            fail("The subtags shouldn't be modifiable");
        } catch (UnsupportedOperationException uoe) {
            // As expected
        }
    }
}
