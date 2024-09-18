package edu.pitt.cs;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import static org.junit.Assert.*;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RentACatUnitTest {

    RentACat r;
    Cat c1, c2, c3;
    ByteArrayOutputStream out;
    PrintStream stdout;
    String newline = System.lineSeparator();

    @Before
    public void setUp() throws Exception {
        r = RentACat.createInstance(InstanceType.IMPL);

        // Set up mocked Cat instances
        c1 = Mockito.mock(Cat.class);
        when(c1.getId()).thenReturn(1);
        when(c1.getName()).thenReturn("Jennyanydots");
        when(c1.toString()).thenReturn("ID 1. Jennyanydots");
        when(c1.getRented()).thenReturn(false);

        c2 = Mockito.mock(Cat.class);
        when(c2.getId()).thenReturn(2);
        when(c2.getName()).thenReturn("Old Deuteronomy");
        when(c2.toString()).thenReturn("ID 2. Old Deuteronomy");
        when(c2.getRented()).thenReturn(false);

        c3 = Mockito.mock(Cat.class);
        when(c3.getId()).thenReturn(3);
        when(c3.getName()).thenReturn("Mistoffelees");
        when(c3.toString()).thenReturn("ID 3. Mistoffelees");
        when(c3.getRented()).thenReturn(false);

        out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
    }

    @After
    public void tearDown() throws Exception {
        System.setOut(stdout);
        r = null;
        c1 = null;
        c2 = null;
        c3 = null;
    }

    @Test
    public void testGetCatNullNumCats0() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        java.lang.reflect.Method method = r.getClass().getDeclaredMethod("getCat", int.class);
        method.setAccessible(true);
        Cat result = (Cat) method.invoke(r, 2);

        assertNull(result);
        assertEquals("Invalid cat ID." + newline, out.toString());
    }

    @Test
    public void testGetCatNumCats3() throws Exception {
        r.addCat(c1);
        r.addCat(c2);
        r.addCat(c3);

        java.lang.reflect.Method method = r.getClass().getDeclaredMethod("getCat", int.class);
        method.setAccessible(true);
        Cat result = (Cat) method.invoke(r, 2);

        assertNotNull(result);
        assertEquals(2, result.getId());
    }

    @Test
    public void testListCatsNumCats0() {
        assertEquals("", r.listCats());
    }

    @Test
    public void testListCatsNumCats3() {
        r.addCat(c1);
        r.addCat(c2);
        r.addCat(c3);

        String expected = "ID 1. Jennyanydots\nID 2. Old Deuteronomy\nID 3. Mistoffelees\n";
        assertEquals(expected, r.listCats());
    }

    @Test
    public void testRenameFailureNumCats0() {
        assertFalse(r.renameCat(2, "Garfield"));
        assertEquals("Invalid cat ID." , out.toString().trim());
    }

    @Test
    public void testRenameNumCat3() {
        r.addCat(c1);
        r.addCat(c2);
        r.addCat(c3);

        when(c2.getName()).thenReturn("Garfield");
        assertTrue(r.renameCat(2, "Garfield"));
        verify(c2).renameCat("Garfield");
        assertEquals("Garfield", c2.getName());
    }

    @Test
    public void testRentCatNumCats3() {
        r.addCat(c1);
        r.addCat(c2);
        r.addCat(c3);

        when(c2.getRented()).thenReturn(true); // Simulate renting behavior
        assertTrue(r.rentCat(2));
        assertTrue(c2.getRented());
        assertEquals("Old Deuteronomy has been rented." + newline, out.toString());
    }

    @Test
    public void testRentCatFailureNumCats3() {
        r.addCat(c1);
        r.addCat(c2);
        r.addCat(c3);

        when(c2.getRented()).thenReturn(true);
        r.rentCat(2);

        out.reset();
        assertFalse(r.rentCat(2));
        assertEquals("Sorry, Old Deuteronomy is not here!" + newline, out.toString());
    }

    @Test
    public void testReturnCatNumCats3() {
        r.addCat(c1);
        r.addCat(c2);
        r.addCat(c3);

        when(c2.getRented()).thenReturn(true);
        r.rentCat(2);
        out.reset();

        when(c2.getRented()).thenReturn(false);
        assertTrue(r.returnCat(2));
        assertEquals("Welcome back, Old Deuteronomy!" + newline, out.toString());
    }

    @Test
    public void testReturnFailureCatNumCats3() {
        r.addCat(c1);
        r.addCat(c2);
        r.addCat(c3);

        assertFalse(r.returnCat(2));
        assertEquals("Old Deuteronomy is already here!" + newline, out.toString());
    }
}
