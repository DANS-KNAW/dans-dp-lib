/**
 * Copyright (C) 2009 DANS (info@dans.knaw.nl)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package nl.knaw.dans.common.dataperfect;


/**
 * Represents the data type of a field. The data type of a field is described in the first character
 * of the format picture. The only exception to this rule is the multi-line text field type which is
 * distinguishable from the single line text field type only because it contains the format picture
 * twice, e.g., "A25A2".
 *
 * @author Jan van Mansum
 */
public enum Type
{
    /**
     * Simple numeric type
     */
    N,
    /**
     * Extended numeric type
     */
    G, 
    /**
     * The same a G, except for printing features
     */
    H, 
    /**
     * Text field type
     */
    A, 
    /**
     * Upper case text field type
     */
    U, 
    /**
     * Multi-line text type
     *
     */
    AA, 
    /**
     * Multi-line upper case text type
     */
    UU, 
    /**
     * Multi-line mixed case text type
     */
    AU, 
    /**
     * Multi-line mixed case text type
     */
    UA, 
    /**
     * Date type
     */
    D, 
    /**
     * Time type
     */
    T, 
    /**
     * No Type
     */
    NONE;
}
