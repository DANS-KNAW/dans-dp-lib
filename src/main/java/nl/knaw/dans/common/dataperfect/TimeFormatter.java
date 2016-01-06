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

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Formats number representing the number of seconds since midnight into a string representing that
 * time.
 *
 * @author Martin Braaksma
 * @author Jan van Mansum
 */
final class TimeFormatter
    extends OrderedFieldFormatter
{
    /**
     * Constructs a <tt>TimeFormatter</tt> that uses <tt>aPicture</tt> to create a string
     * representation of a given time value.
     *
     * @param picture a DataPerfect time format picture
     *
     *            <p>
     *            The time picture consists of:
     *            <ol>
     *            <li>a capital letter T
     *            <li>optionally the three letters: H, M and S in any order (default: "HMS")
     *            <li>one ore more groups of two 9 characters, separated by a colon
     *            </ol>
     *            The T indicates that the type of data formatted is a time. The H, M and S specify
     *            the order of the subsequent hour minute and second fields. The groups of 9's
     *            indicate where the numbers go.
     *            <p>
     *            Examples. The following examples use the time 11:59:59.
     *            <table border="1">
     *            <tr>
     *            <th align="left">Format</th>
     *            <th align="left">Result</th>
     *            </tr>
     *            <tr>
     *            <td>THMS99:99:99</td>
     *            <td>11:59:59</td>
     *            </tr>
     *            <tr>
     *            <td>THM99:99</td>
     *            <td>11:59</td>
     *            </tr>
     *            <tr>
     *            <td>TMS99:99</td>
     *            <td>59:59</td>
     *            </tr>
     *            </table>
     *
     * @param defaultTimeOrder the default time order
     */
    TimeFormatter(final String picture, final String defaultTimeOrder)
    {
        super("(T)", "([HMS]{0,3})", "(:)", "([Z9]+)", picture, defaultTimeOrder);
    }

    void setFields(final Object object, final int[] fields)
    {
        final Number secondsSinceMidnight = (Number) object;
        final Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        calendar.setTimeInMillis(secondsSinceMidnight.longValue() * 1000);

        setField(fields,
                 'H',
                 calendar.get(Calendar.HOUR));
        setField(fields,
                 'M',
                 calendar.get(Calendar.MINUTE));
        setField(fields,
                 'S',
                 calendar.get(Calendar.SECOND));
    }
}
