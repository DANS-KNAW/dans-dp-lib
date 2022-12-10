package nl.knaw.dans.common.dataperfect;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TestStringFormatter
{
    @Test
    public void dataLength()
    {
        StringFormatter sf = new StringFormatter("A25");
        assertEquals(25,
                     sf.getDataLength());

        sf = new StringFormatter("U5");
        assertEquals(5,
                     sf.getDataLength());

        sf = new StringFormatter("A25A3");
        assertEquals(32000,
                     sf.getDataLength());
    }
}
