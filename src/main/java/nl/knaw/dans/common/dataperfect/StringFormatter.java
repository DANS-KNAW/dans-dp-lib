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
