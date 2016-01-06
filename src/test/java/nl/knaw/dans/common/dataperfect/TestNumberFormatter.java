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

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import org.junit.runner.RunWith;

import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;

@RunWith(value = Parameterized.class)
public class TestNumberFormatter
{
    private String expected;
    private String actual;

    public TestNumberFormatter(String expected, String actual)
    {
        this.expected = expected;
        this.actual = actual;
    }

    @Parameters
    public static Collection<Object[]> data()
    {
        return Arrays.asList(new Object[][]
                             {
                                 /*
        * N-format tests
        */
                                 {"1234", format("N9999", 1234) }, // 0
        { "2300", format("N9999", 23) }, // 1
        { "1230", format("N9999", 123) }, // 2
        { "0000", format("N9999", 0) }, // 3
        { "-1234", format("N-9999", 1234) }, // 4
        { "+1234", format("N+9999", 1234) }, // 5
        { "1234-", format("N9999-", 1234) }, // 6
        { "1234+", format("N9999+", 1234) }, // 7
        { "12345678912345", format("N99999999999999", 12345678912345L) }, // 8
        { "+12345678912345", format("N+99999999999999", 12345678912345L) }, // 9
        { "12-345", format("N99-999", 12345) }, // 10
        { "12,345", format("N99,999", 12345) }, // 11
        { "12,345.67", format("N99,999.99", 1234567) }, // 12
        { "ABCD1234", format("NABCD9999", 1234) }, // 13
        { "1234ABCD", format("N9999ABCD", 1234) }, // 14
                                                   // { "12 AB34", format("N99 AB34", 1234) }, // 15
        { "(123) 456-700", format("N(999) 999-999", 1234567) }, // 16
        { ",.1234", format("N,.9999", 1234) }, // 17
                                               /*
        * G and H-format tests.
        */
        { "1234", format("G9999", 1234) }, // 18
        { "0023", format("G9999", 23) }, // 19
        { "  23", format("GZZ99", 23) }, // 20
        { " 123", format("GZZ99", 123) }, // 21
        { "  00", format("GZZ99", 0) }, // 22
        { "  23", format("GZZZ9", 23) }, // 23
        { " 1234", format("G-9999", 1234) }, // 24
        { "1234 ", format("G9999-", 1234) }, // 25
        { "-0023", format("G-9999", -23) }, // 26
        { "  23-", format("GZZZ9-", -23) }, // 27
        { "  -23", format("G-ZZZ9", -23) }, // 28
        { "  +23", format("G+ZZZ9", 23) }, // 29
        { "   23", format("G-ZZZ9", 23) }, // 30
        { "0000", format("G9999", 0) }, // 31
        { " 000", format("GZ999", 0) }, // 32
        { "    ", format("GZZZZ", 0) }, // 33
        { "   23", format("GZ,Z99", 23) }, // 34
        { "10,123", format("GZZ,Z99", 10123) }, // 35
        { "1023", format("GZZ99", 1023) }, // 36
        { "1203", format("GZZ99", 1203) }, // 37
        { "0012", format("G9999", 12) }, // 38
        { "10000", format("GZZZ99", 10000) }, // 39
        { "0 ,001", format("G9Z,999", 1) }, // 40
        { "0 ,001-", format("G9Z,999-", -1) }, // 41
        { "-0 ,001", format("G-9Z,999", -1) }, // 42
        { "01,111", format("G9Z,999", 1111) }, // 43
        { "10,111", format("G9Z,999", 10111) }, // 44
        { "  10", format("GZZZ9", 10) }, // 45
        { "0    01", format("G9ZZ,Z99", 1) }, // 46
        { "-0    01", format("G-9ZZ,Z99", -1) }, // 47
        { "0 1,111-", format("G9ZZ,Z99-", -1111) }, // 48
        { "00  01", format("G99,Z99", 1) }, // 49
        { "0   12", format("G9Z,Z99", 12) }, // 50
        { "    01", format("GZZ,Z99", 1) }, // 51
        { " 1,111", format("GZZ,Z99", 1111) }, // 52
        { "  ,111", format("GZZ,999", 111) }, // 53
        { "10,000", format("GZZ,Z99", 10000) }, // 54
        { "(1234)", format("G(9999)", -1234) }, // 55
        { " 1234 ", format("G(9999)", 1234) }, // 56
        { "(  23)", format("G(ZZ99)", -23) }, // 57
        { "   23 ", format("G(ZZ99)", 23) }, // 58
        { "**01", format("G**99", 1) },
                                 { "**23", format("G**99", 23) },
                                 { "*123", format("G**99", 123) },
                                 { "1234", format("G**99", 1234) },
                                 { "**23", format("G***9", 23) },
                                 { "-**23", format("G-***9", -23) },
                                 { "-**23", format("G+***9", -23) },
                                 { "**23+", format("G***9+", 23) },
                                 { "*0023", format("G*9999", 23) },
                                 { "*  23", format("G*ZZ99", 23) },
                                 { "0*023", format("G9*999", 23) },
                                 { "01023", format("G9*999", 1023) },
                                 { "10023", format("G9*999", 10023) },
                                 { "0**03", format("G9**99", 3) },
                                 { "0*103", format("G9**99", 103) },
                                 { "01030", format("G9**99", 1030) },
                                 { "10000", format("G9**99", 10000) },
                                 { "0*,003", format("G9*,999", 3) },
                                 { "0*,023", format("G9*,999", 23) },
                                 { "0*,123", format("G9*,999", 123) },
                                 { "01,234", format("G9*,999", 1234) },
                                 { "10,234", format("G9*,999", 10234) },
                                 { "0***03", format("G9*,*99", 3) },
                                 { "0***34", format("G9*,*99", 34) },
                                 { "0**345", format("G9*,*99", 345) },
                                 { "03,456", format("G9*,*99", 3456) },
                                 { "30,000", format("G9*,*99", 30000) },
                                 
        { "****04", format("G**,*99", 4) },
                                 { " ****04", format("G-**,*99", 4) },
                                 { "-****04", format("G-**,*99", -4) },
                                 { "****04-", format("G**,*99-", -4) },
                                 { "****04-", format("G**,*99+", -4) },
                                 { "+****04", format("G+**,*99", 4) },
                                 { "****34", format("G**,*99", 34) },
                                 { "***234", format("G**,*99", 234) },
                                 { "*1,234", format("G**,*99", 1234) },
                                 { "10,234", format("G**,*99", 10234) },
                                 
        { "00**01", format("G99,*99", 1) },
                                 { "00**12", format("G99,*99", 12) },
                                 { "00*123", format("G99,*99", 123) },
                                 { "01,234", format("G99,*99", 1234) },
                                 { "10,234", format("G99,*99", 10234) },
                                 
        { "**23", format("H**99", 23) },
                                 { "**23", format("H***9", 23) },
                                 { "-**23", format("H-***9", -23) },
                                 
        { "+2300", format("G+9999", 2300) },
                                 { "-2300", format("G+9999", -2300) },
                                 { " 2300", format("G-9999", 2300) },
                                 { "-2300", format("G-9999", -2300) },
                                 { "  23-", format("GZZZ9-", -23) },
                                 { "  23 ", format("GZZZ9-", 23) },
                                 { "  +23", format("G+ZZZ9", 23) },
                                 { "+0023", format("G+9999", 23) },
                                 { "  -23", format("G+ZZZ9", -23) },
                                 { "  23+", format("GZZZ9+", 23) },
                                 { "  23-", format("GZZZ9+", -23) },
                                 { "1,234", format("G9,999", 1234) },
                                 { "1,234,567", format("G9,999,999", 1234567) },
                                 { "1,234,567,890", format("G9,999,999,999", 1234567890) },
                                 { "11,111,234,567,890", format("G99,999,999,999,999", 11111234567890L) },
                                 { "9,95", format("G9,99", 995) },
                                 { "19.995", format("G99.999", 19995) },
                                 { "119.99555", format("G999.99999", 11999555) },
                                 { " 1,234", format("G-9,999", 1234) },
                                 { "+1,234,567", format("G+9,999,999", 1234567) },
                                 { "-1,234,567,890", format("G+9,999,999,999", -1234567890) },
                                 { "*1,111,234,567,890", format("G*9,999,999,999,999", 1111234567890L) },
                                 { "9,95 ", format("G9,99-", 995) },
                                 { "19.995-", format("G99.999-", -19995) },
                                 { "-119.99555", format("G-999.99999", -11999555) },
                                 { "1.999,95", format("G,.9.999,99", 199995) },
                                 { "  9,000.00", format("GZZ9,999.99", 900000) },
                                 { "1,999.95", format("G9,999.99", 199995) },
                                 { "1,999,999.95", format("G9,999,999.99", 199999995) },
                                 { "01,999.95", format("G99,999.99", 199995) },
                                 { " 1,999.95", format("G.,Z9,999.99", 199995) },
                                 { " 1.999,95", format("G,.Z9.999,99", 199995) },
                                 { "  $01", format("G$ZZ99", 1) }, // should float
        { "  $12", format("G$ZZ99", 12) },
                                 { " $123", format("G$ZZ99", 123) },
                                 { "$1234", format("G$ZZ99", 1234) },
                                 { " $1234", format("G-$ZZ99", 1234) },
                                 { "-$1234", format("G-$ZZ99", -1234) },
                                 /*
        * Is literal but seen as sign. Is also a problem in the examples above.
        */

        // { "$-1234", format("G$-ZZ99", 1234)},
                                 {"($0012)", format("G($9999)", -12) },
                                 { "    $12.34", format("G-$Z,ZZ9.99", 1234) },
                                 { "   -$12.34", format("G+$Z,ZZ9.99", -1234) },
                                 { "   $12.34-", format("G$Z,ZZ9.99+", -1234) },
                             });
    }

    private static String format(String picture, Number number)
    {
        return new NumberFormatter(picture).format(number);
    }

    @Test
    public void testStandardCases()
    {
        assertEquals(expected, actual);
    }
}
