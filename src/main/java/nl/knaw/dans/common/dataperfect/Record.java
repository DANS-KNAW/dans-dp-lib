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
