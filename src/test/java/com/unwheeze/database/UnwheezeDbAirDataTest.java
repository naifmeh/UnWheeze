package com.unwheeze.database;

import junit.framework.TestCase;
import org.junit.Test;

public class UnwheezeDbAirDataTest extends TestCase {

    @Test
    public void testGetAllDataFromCollection() {
        try {
            (new UnwheezeDbAirData()).getAllDataFromCollection();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
