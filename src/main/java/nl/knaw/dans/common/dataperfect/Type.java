/*
 * Copyright 2009-2016 Data Archiving and Networked Services (DANS), Netherlands.
 *
 * This file is part of DANS DataPerfect Library.
 *
 * DANS DataPerfect Library is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version.
 *
 * DANS DataPerfect Library is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with DANS DataPerfect
 * Library. If not, see <http://www.gnu.org/licenses/>.
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
