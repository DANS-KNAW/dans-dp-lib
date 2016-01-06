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
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import nl.knaw.dans.common.dataperfect.DataPerfectLibException;
import nl.knaw.dans.common.dataperfect.Database;
import nl.knaw.dans.common.dataperfect.Field;
import nl.knaw.dans.common.dataperfect.Panel;
import nl.knaw.dans.common.dataperfect.Record;
import nl.knaw.dans.common.dataperfect.Type;

/**
 * This program shows how you can use DANS DataPerfect Library. This program exports a DataPerfect
 * database to an SQL script that can be read by MySQL to create a database with the same tables and
 * data.
 * 
 * Please note that this program has not been thoroughly tested. However it could be the basis for a
 * more elaborate export program.
 * 
 * To compile this program, open a command prompt and go to the directory of this file, then
 * execute:
 * 
 * javac -cp dans-dp-lib.jar Dp2MySqlExport.java
 * 
 * Of course dans-dp-lib.jar must be present in the same directory, or you must adjust the path in
 * the command line.
 * 
 * @author Martin Braaksma
 * @author Jan van Mansum
 * 
 */
public class Dp2MySqlExport
{
    public static void main(String[] args) throws IOException, DataPerfectLibException
    {
        if (args.length < 2)
        {
            throw new IllegalArgumentException(
                    "Usage: Dp2MySqlExport <structure file> <output file>");
        }

        final Database database = new Database(new File(args[0]));
        final File sqlScript = new File(args[1]);
        final PrintWriter writer = new PrintWriter(new FileWriter(sqlScript));

        try
        {
            createDatabase(args[0], writer);
            database.open();
            final List<Panel> panels = database.getPanels();
            for (final Panel panel : panels)
            {
                final String tableName = createTable(panel, writer);
                fillTable(tableName, panel, writer);
            }
        }
        finally
        {
            writer.close();
            database.close();
        }
    }

    private static void createDatabase(final String databasePath, final PrintWriter writer)
            throws IOException
    {
        final String databaseName =
                databasePath.substring(databasePath.lastIndexOf("/") + 1, databasePath
                        .lastIndexOf("."));
        writer.println("CREATE DATABASE " + databaseName + ";");
        writer.println("USE " + databaseName + ";");
    }

    private static String createTable(final Panel panel, final PrintWriter writer)
            throws IOException
    {
        /*
         * A MySQL table identifier cannot contain a dot.
         */
        final String tableName = panel.getFile().getName().replace(".", "");
        writer.print("CREATE TABLE ");
        writer.print(tableName);
        writer.print("(");

        final List<Field> fields = panel.getFields();
        final Iterator<Field> fieldIterator = fields.iterator();

        while (fieldIterator.hasNext())
        {
            final Field field = fieldIterator.next();

            writer.print(new ColumnDefinition(field).toString());

            if (fieldIterator.hasNext())
            {
                writer.print(", ");
            }
        }

        writer.print(");");
        writer.println();

        return tableName;
    }

    private static void fillTable(final String tableName, final Panel panel,
            final PrintWriter writer) throws IOException
    {
        final Iterator<Record> recordIterator = panel.recordIterator();
        final List<Field> fields = panel.getFields();

        while (recordIterator.hasNext())
        {
            Record record = recordIterator.next();
            String values = "";

            final Iterator<Field> fieldIterator = fields.iterator();
            while (fieldIterator.hasNext())
            {
                final Field field = fieldIterator.next();
                if (field.getType() == Type.D)
                {
                    String date = getDate(record.getValueAsNumber(field.getNumber()).intValue());

                    values += "'" + date + "'";

                }
                else if (field.getType() == Type.T)
                {
                    String time = getTime(record.getValueAsNumber(field.getNumber()).intValue());

                    values += "'" + time + "'";
                }
                else
                {
                    values +=
                            "'" + escapeSingleQuotes(record.getValueAsString(field.getNumber()))
                                    + "'";
                }

                if (fieldIterator.hasNext())
                {
                    values += ", ";
                }
            }

            writer.println("INSERT INTO " + tableName + " VALUES (" + values + ");");
        }
    }

    private static String getTime(final int numberOfSecondsSinceMidnight)
    {
        final Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, numberOfSecondsSinceMidnight);

        return calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE) + ":"
                + calendar.get(Calendar.SECOND);
    }

    private static String getDate(final int numberOfDaysSinceDPDateOffset)
    {
        final Calendar calendar = Calendar.getInstance();
        calendar.set(1900, 2, 1);
        calendar.add(Calendar.DAY_OF_MONTH, numberOfDaysSinceDPDateOffset);

        return calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-"
                + calendar.get(Calendar.DAY_OF_MONTH);
    }

    private static String escapeSingleQuotes(final String string)
    {
        return string.replaceAll("'", "\\'");
    }
}

class ColumnDefinition
{
    private final String name;
    private final String type;

    ColumnDefinition(final Field field)
    {
        if (field.getName() != null)
        {
            name = field.getName();
        }
        else
        {
            name = "" + field.getNumber();
        }

        if (field.getType() == Type.D)
        {
            type = "DATE";
        }
        else if (field.getType() == Type.T)
        {
            type = "TIME";
        }
        else
        {
            /*
             * Arbitrary length
             */
            type = "VARCHAR(254)";
        }
    }

    public String getName()
    {
        return name;
    }

    public String getType()
    {
        return type;
    }

    @Override
    public String toString()
    {
        return name + " " + type;
    }
}