package nl.knaw.dans.common.dataperfect;

import org.junit.*;
import static org.junit.Assert.*;
public class TestNumberFormatterDataLength
{
    @Test
    public void testDataLength()
    {
        NumberFormatter nf = new NumberFormatter("G9999");
        assertEquals(4,
                     nf.getDataLength());

        nf = new NumberFormatter("G.,9999");
        assertEquals(4,
                     nf.getDataLength());

        nf = new NumberFormatter("G$9999");
        assertEquals(5,
                     nf.getDataLength());

        nf = new NumberFormatter("G+9999");
        assertEquals(5,
                     nf.getDataLength());

        nf = new NumberFormatter("G9999-");
        assertEquals(5,
                     nf.getDataLength());

        nf = new NumberFormatter("G+$9999");
        assertEquals(6,
                     nf.getDataLength());

        nf = new NumberFormatter("HABCD9999");
        assertEquals(8,
                     nf.getDataLength());

        nf = new NumberFormatter("H9999ABCD");
        assertEquals(8,
                     nf.getDataLength());

        nf = new NumberFormatter("H99,999.99");
        assertEquals(9,
                     nf.getDataLength());

        nf = new NumberFormatter("H$99,999.99+");
        assertEquals(11,
                     nf.getDataLength());
    }
}
