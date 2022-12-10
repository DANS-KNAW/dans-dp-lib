package site.resources;

/*
 * Copyright 2009 Data Archiving and Networked Services (DANS), Netherlands.
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
import nl.knaw.dans.common.dataperfect.NoSuchRecordFieldException;
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
public class Dp2Pg_Literat
{
    public static void main(String[] args) throws IOException, DataPerfectLibException, NoSuchRecordFieldException
    {
        if (args.length < 2)
        {
            throw new IllegalArgumentException(
                    "Usage: Dp2MySqlExport <structure file> <output file>");
        }

        final Database database = new Database(new File(args[0]));
        //database.setCharset("ISO-8859-1");
        //String form = database.getCharsetName();
        final File sqlScript = new File(args[1]);
        final PrintWriter writer = new PrintWriter(new FileWriter(sqlScript));

        try
        {
            createDatabase(args[0], writer);
            database.open();
            int tableCount=0;
            final List<Panel> panels = database.getPanels();
            for (final Panel panel : panels)
            {
                final String tableName = createTable(panel, writer, tableCount++);
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
        // writer.println("CREATE DATABASE " + databaseName + ";"); // brauche ich Daniel erstmal nicht
        // writer.println("USE " + databaseName + ";");
    }

    private static String createTable(final Panel panel, final PrintWriter writer, int tc)
            throws IOException
    {
        /*
         * A MySQL table identifier cannot contain a dot.
         */
        final String tableName = panel.getFile().getName().replace(".", "");
        writer.print("CREATE TABLE ");
        writer.print(tableName);
        writer.print("(");
        String[] columnsBooks = {"category", "book_id", "author", "title", "month", "year_read", 
        					"source", "medial_id", "exzerpt", "verlag", "auflage", "jahr",
        					"isbn", "keywords", "abstract"};
        String[] columnsOrd = {"book_id", "keywords"};

        final List<Field> fields = panel.getFields();
        final Iterator<Field> fieldIterator = fields.iterator();

		if (tc > 2) // letzte Tabelle
			for (int i = 0; i < 2; i++) {
				writer.print(columnsOrd[i] + " varchar(254)");
				if (i < 1)
					writer.print(", "); // nach letzter Spalte kein Komma mehr
			}
		else if (tc > 0)
			while (fieldIterator.hasNext()) {
				final Field field = fieldIterator.next();

				writer.print(new ColumnDefinition1(field).toString());

				if (fieldIterator.hasNext()) {
					writer.print(", ");
				}
			}
		else // nur für 1. Tabelle meine columns verwenden table -> LITERATBOO
			for (int i = 0; i < 15; i++) {
				writer.print(columnsBooks[i] + " varchar(254)");
				if (i < 14)
					writer.print(", "); // nach letzter Spalte kein Komma mehr
			}

        writer.print(");");
        writer.println();

        return tableName;
    }

    private static void fillTable(final String tableName, final Panel panel,
            final PrintWriter writer) throws IOException, NoSuchRecordFieldException
    {
        final Iterator<Record> recordIterator = panel.recordIterator();
        final List<Field> fields = panel.getFields(); int rowCounter = 0;

        while (recordIterator.hasNext())
        {
            Record record = recordIterator.next();
            String values = ""; int colCounter = 0;
            rowCounter++;

            final Iterator<Field> fieldIterator = fields.iterator(); 
            while (fieldIterator.hasNext())
            {
            	colCounter++;
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
                	String helper1 = escapeSingleQuotesAndShorten(record.getValueAsString(field.getNumber()));
                	String helper2 = helper1.replaceAll("'", " "); // wenn im Text Hochkomma vorkommt, raus damit
                	String helper3 = helper2.replaceAll("-\n", ""); // Zeilenende Trennungen im Text löschen
                	String helper = replaceGermanUmlauts (helper3);
                	if (rowCounter>137) // ein paar Einträge fehlen - ab Buch ID 142 wechseln leider Y & Z mal wieder
                		helper = correctCharY (helper3);  // muss mit Windows11 auf neuem Laptop zusammenhängen
                	
                	if (tableName.equals("LITERATBOO")) {
						if (colCounter == 2)
							if (rowCounter < 10)
								helper = helper3.replaceAll("0", ""); // einstellige IDs falsch 100 >> 1
							else if (rowCounter < 97)
								helper = helper3.substring(0, 2); // 2-stellige IDs falsch 110 >> 11, 3x IDs fehlen vor 100

						if (colCounter < 13) // Daniel Spezial für ISBM Nummer 4 Zahlen >> ISBN xxx-xxxx-xxxx-x als String
							values += "'" + helper + "'";
						else if (colCounter < 14)
							values += "'" + helper;
						else if (colCounter < 16)
							values += "-" + helper;
						else if (colCounter < 17) {
							values += "-" + helper + "'";
							values = values.replaceAll("---", ""); // leere ISBN Felder wieder löschen
						} else
							values += "'" + helper + "'"; // normaler Ablauf alle normalen Felder landen hier
					}
                	else if (tableName.equals("LITERATORD")) {
						if (colCounter == 1)
							if (rowCounter < 5)
								helper = helper3.replaceAll("0", ""); // einstellige IDs falsch 100 >> 1
							else if (rowCounter < 7)
								helper = helper3.substring(0, 2); // 100 >> 10 
							else if (rowCounter < 9)
								helper = helper3.replaceAll("0", ""); // einstellige IDs falsch 100 >> 1
							else if (rowCounter < 68)
								helper = helper3.substring(0, 2); // 2-stellige IDs falsch 110 >> 11, 3x IDs fehlen vor 100
							else if (rowCounter < 69);
							else if (rowCounter < 70)
								helper = helper3.substring(0, 2); // 2-stellige IDs falsch 110 >> 11, 3x IDs fehlen vor 100
							else if (rowCounter < 79);
							else if (rowCounter < 80)
								helper = helper3.substring(0, 2); // 2-stellige IDs falsch 110 >> 11, 3x IDs fehlen vor 100
							else if (rowCounter < 81);
							else if (rowCounter < 88)
								helper = helper3.substring(0, 2); // 2-stellige IDs falsch 110 >> 11, 3x IDs fehlen vor 100
							else if (rowCounter < 89);
							else if (rowCounter < 94)
								helper = helper3.substring(0, 2); // 2-stellige IDs falsch 110 >> 11, 3x IDs fehlen vor 100

							values += "'" + helper + "'"; // normaler Ablauf alle normalen Felder landen hier
					}
					else values += "'" + helper + "'"; // alle anderen Tabellen landen hier
                }

                if (fieldIterator.hasNext())
                {
                	if ( (colCounter < 13) || (colCounter > 15) )  // kein Komma in der ISBN
                		values += ", "; 						// zum Glück haben andere Tabellen grad nicht so viel SPalten
                }
            }

            writer.println("INSERT INTO " + tableName + " VALUES (" + values + ");");
        }
    }

    private static String replaceGermanUmlauts(String text_in) {
        String s = text_in;

        final byte iso [][] = new byte[][] { 
        		{ (byte)0xc3, (byte)0xa1,},
        		{ (byte)0xef, (byte)0xbf, (byte)0xbd,},
        		{ (byte)0xc5, (byte)0xa1,},
        		{ (byte)0xe2, (byte)0x80, (byte)0x9e,},
        		{ (byte)0xc5, (byte)0xbd,},
        		{ (byte)0xe2, (byte)0x80, (byte)0x9d,},
        		{ (byte)0xc3, (byte)0xa1,}, // großes Ö fehlt mir noch
        };
        final String utf [] = new String[] { "ß", "ü", "Ü", "ä", "Ä", "ö", "Ö" };
        
        for (int i=0; i < 6; i++)
        	s = s.replaceAll(new String(iso[i]), utf[i]);

        return s;
    }
    
    private static String correctCharY(String text_in) {
        String s = text_in;

        final byte bad [][] = new byte[][] { 
        		{ (byte)0x79, },
        };
        s = s.replaceAll(new String(bad[0]), "z");

        return s;
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

    private static String escapeSingleQuotesAndShorten (final String string)
    {
        String s1 = string.trim();
    	return s1.replaceAll("'", "\\'");
    }
}

class ColumnDefinition1
{
    private final String name;
    private final String type;

    ColumnDefinition1(final Field field)
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