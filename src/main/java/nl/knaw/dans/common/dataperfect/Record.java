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

import java.util.HashMap;
import java.util.Map;

/**
 * A record in a panel. This is basically a map from a field name or number to its value in the
 * record. In DataPerfect every field has a number, but name is an optional attribute. Even so, it
 * is often more convenient to look up a value based on the field name.
 * <p>
 * All values can be retrieved as <tt>String</tt>s. The string returned is the representation of the
 * value as produced by the DataPerfect user interface, i.e. after applying the format.
 * <p>
 * Numeric, date and time values can also be retrieved as <tt>Number</tt>s. The number is, however,
 * always the integral value stored. Even fixed-point fractional numbers are returned as the
 * integral that is formed by leaving out the decimal point. Dates are stored as the number of days
 * after 1 March 1900. Times are stored as number of seconds after midnight.
 *
 * @author Jan van Mansum
 */
public class Record
{
    private final Map<String, Integer> nameToNumberMap = new HashMap<String, Integer>();
    private final Map<Integer, Object> numberToValueMap = new HashMap<Integer, Object>();
    private final Map<Integer, Field> numberToFieldMap = new HashMap<Integer, Field>();

    /**
     * Constructs a Record object.
     *
     * @param fieldToValueMap map from {@link Field} objects to value objects
     */
    Record(final Map<Field, Object> fieldToValueMap)
    {
        for (final Field field : fieldToValueMap.keySet())
        {
            numberToValueMap.put(field.getNumber(),
                                 fieldToValueMap.get(field));
            numberToFieldMap.put(field.getNumber(),
                                 field);

            if (field.getName() != null)
            {
                nameToNumberMap.put(field.getName(),
                                    field.getNumber());
            }
        }
    }

    /**
     * Returns the formatted value in this record belonging to the specified field name.
     *
     * @param fieldName the name of the field to select
     *
     * @return the value
     * @throws NoSuchRecordFieldException if the field name does not exist
     */
    public String getValueAsString(final String fieldName)
                            throws NoSuchRecordFieldException
    {
        if (! nameToNumberMap.containsKey(fieldName))
        {
            throw new NoSuchRecordFieldException("The field " + fieldName + " does not exist");
        }

        return getValueAsString(nameToNumberMap.get(fieldName));
    }

    /**
     * Returns the formatted value in this record of the specified field.
     *
     * @param fieldNumber the field number for which to return the value
     *
     * @return the value
     * @throws NoSuchRecordFieldException if the field number does not exist
     */
    public String getValueAsString(final int fieldNumber)
                            throws NoSuchRecordFieldException
    {
        if (! numberToValueMap.containsKey(fieldNumber))
        {
            throw new NoSuchRecordFieldException("Field " + fieldNumber + " does not exist");
        }

        final Object fieldValue = numberToValueMap.get(fieldNumber);

        if (fieldValue == null)
        {
            return null;
        }

        final Field field = numberToFieldMap.get(fieldNumber);

        return field.getFormatter().format(fieldValue);
    }

    /**
     * Returns the value as a number. This is only possible for numeric, date and time fields.
     *
     * @param fieldName the field name for which to return the value
     *
     * @return the value
     * @throws NoSuchRecordFieldException if the field name does not exist
     */
    public Number getValueAsNumber(final String fieldName)
                            throws NoSuchRecordFieldException
    {
        if (! nameToNumberMap.containsKey(fieldName))
        {
            throw new NoSuchRecordFieldException("The field " + fieldName + " does not exist");
        }

        return getValueAsNumber(nameToNumberMap.get(fieldName));
    }

    /**
     * Returns the value as a number. This is only possible for numeric, date and time fields.
     *
     * If the field is a date field the method returns the number of days since 1 March 1900. The
     * precision of the date field may actually be less than a day.
     *
     * @param fieldNumber the field number for which to return the value
     *
     * @return the value
     * @throws NoSuchRecordFieldException if the field number does not exist
     * @throws ClassCastException if the fieldNumber does not refer to a numeric field
     */
    public Number getValueAsNumber(final int fieldNumber)
                            throws NoSuchRecordFieldException
    {
        if (! numberToValueMap.containsKey(fieldNumber))
        {
            throw new NoSuchRecordFieldException("Field " + fieldNumber + " does not exist");
        }

        final Object fieldValue = numberToValueMap.get(fieldNumber);

        return (Number) fieldValue;
    }
}
