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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Do-nothing formatter that takes a <code>String</code> and returns it unchanged.
 *
 * @author Jan van Mansum
 */
class StringFormatter
    extends AbstractFormatter
{
    private static final int MAX_LENGTH_TEXTFIELD = 32000;
    private static final int MULTILINE_A_FIELD_GROUP = 2;
    private static final int MULTILINE_U_FIELD_GROUP = 4;
    private static final Pattern patternPicture = Pattern.compile("(A\\d\\d?([AU]\\d\\d?)?)|(U\\d\\d?([UA]\\d\\d?)?)");
    private final int dataLength;

    StringFormatter(final String picture)
    {
        super(picture);

        Matcher matcher = patternPicture.matcher(strippedPicture);

        if (! matcher.matches())
        {
            throw new IllegalArgumentException("Picture does not match expected pattern for type alphanumeric: "
                                               + picture);
        }

        if (isMultilineTextField(matcher))
        {
            dataLength = MAX_LENGTH_TEXTFIELD;

            return;
        }

        dataLength = Integer.parseInt(strippedPicture.substring(1));
    }

    private static boolean isMultilineTextField(final Matcher matcher)
    {
        return matcher.group(MULTILINE_A_FIELD_GROUP) != null || matcher.group(MULTILINE_U_FIELD_GROUP) != null;
    }

    public String format(final Object object)
    {
        return (String) object;
    }

    public int getDataLength()
    {
        return dataLength;
    }
}
