/**
 * Copyright (C) 2009-2016 DANS - Data Archiving and Networked Services (info@dans.knaw.nl)
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
 * Abstract class providing methods common to the DateFormatter and TimeFormatter classes.
 *
 * @author Martin Braaksma
 */
abstract class OrderedFieldFormatter
    extends AbstractFormatter
{
    private static final String MODE_INDICATORS = "((?:::|;;).*)";
    private final Pattern patternPicture;
    private final String indicatorSubgroup;
    private final String[] separators = new String[4];
    private final int[] fieldLengths = new int[3];
    private final int[] fields = new int[3];
    private String fieldOrder;
    private int dataLength;

    public OrderedFieldFormatter(final String indicatorSubgroup, final String fieldOrderSubgroup,
                                 final String separatorSubgroup, final String numberSubgroup, final String picture,
                                 final String defaultFieldOrder)
    {
        super(picture);

        this.indicatorSubgroup = indicatorSubgroup;

        patternPicture = Pattern.compile(indicatorSubgroup + fieldOrderSubgroup + separatorSubgroup + "?"
                                         + numberSubgroup + "?" + separatorSubgroup + "?" + numberSubgroup + "?"
                                         + separatorSubgroup + "?" + numberSubgroup + "?" + MODE_INDICATORS + "?");

        getPictureValues(strippedPicture, defaultFieldOrder);
    }

    abstract void setFields(final Object object, final int[] fields);

    private void getPictureValues(final String picture, final String defaultFieldOrder)
    {
        final Matcher pictureMatcher = patternPicture.matcher(picture);

        if (! pictureMatcher.matches())
        {
            throw new IllegalArgumentException("Picture does not match expected pattern for type " + indicatorSubgroup
                                               + ": " + picture);
        }

        fieldOrder = pictureMatcher.group(2);

        if ("".equals(fieldOrder))
        {
            fieldOrder = defaultFieldOrder;

            /*
             * Subtract 1 character that identifies the field type
             */
            dataLength = picture.length() - 1;
        }
        else
        {
            dataLength = picture.length() - 1 - fieldOrder.length();
        }

        separators[0] = pictureMatcher.group(3);
        fieldLengths[0] = getFieldLength(pictureMatcher.group(4));
        separators[1] = pictureMatcher.group(5);
        fieldLengths[1] = getFieldLength(pictureMatcher.group(6));
        separators[2] = pictureMatcher.group(7);
        fieldLengths[2] = getFieldLength(pictureMatcher.group(8));
    }

    private static int getFieldLength(final String picturePart)
    {
        if (picturePart == null)
        {
            return -1;
        }

        return picturePart.length();
    }

    protected void setField(final int[] fields, final char fieldLetter, final int fieldValue)
    {
        int index = fieldOrder.indexOf(fieldLetter);

        if (index != -1)
        {
            fields[index] = fieldValue;
        }
    }

    private static String formatField(int fieldLength, int fieldValue)
    {
        if (fieldLength == 0)
        {
            return "";
        }

        String result = String.format("%0" + fieldLength + "d", fieldValue);

        return result.substring(result.length() - fieldLength);
    }

    public String format(final Object object)
    {
        if (! (object instanceof Number))
        {
            throw new IllegalArgumentException("Object to format must be a java.lang.Number");
        }

        setFields(object, fields);

        final StringBuilder buffer = new StringBuilder();
        buffer.append(separators[0] == null ? "" : separators[0]);
        buffer.append(formatField(fieldLengths[0], fields[0]));
        buffer.append(separators[1] == null ? "" : separators[1]);
        buffer.append(fieldLengths[1] == -1 ? "" : formatField(fieldLengths[1], fields[1]));
        buffer.append(separators[2] == null ? "" : separators[2]);
        buffer.append(fieldLengths[2] == -1 ? "" : formatField(fieldLengths[2], fields[2]));

        return buffer.toString();
    }

    public int getDataLength()
    {
        return dataLength;
    }
}
