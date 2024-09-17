package edu.pitt.cs;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RentACatIntegrationTest {

	RentACat r; // Object to test
	Cat c1; // First cat object
	Cat c2; // Second cat object
	Cat c3; // Third cat object

	ByteArrayOutputStream out; // Output stream for testing system output
	PrintStream stdout; // Print stream to hold the original stdout stream
	String newline = System.lineSeparator(); // Platform-independent newline for assertions

	@Before
	public void setUp() throws Exception {
		// Initialize RentACat and cats
		r = RentACat.createInstance(InstanceType.IMPL);

		// Create actual CatImpl objects for integration tests
		c1 = Cat.createInstance(InstanceType.IMPL, 1, "Jennyanydots");
		c2 = Cat.createInstance(InstanceType.IMPL, 2, "Old Deuteronomy");
		c3 = Cat.createInstance(InstanceType.IMPL, 3, "Mistoffelees");

		// Redirect system output for testing
		stdout = System.out;
		out = new ByteArrayOutputStream();
		System.setOut(new PrintStream(out));
	}

	@After
	public void tearDown() throws Exception {
		// Restore the original system output
		System.setOut(stdout);

		// Clear references (not strictly necessary)
		r = null;
		c1 = null;
		c2 = null;
		c3 = null;
	}

	@Test
	public void testGetCatNullNumCats0() throws Exception {
		// Use reflection to access private getCat method
		java.lang.reflect.Method method = r.getClass().getDeclaredMethod("getCat", int.class);
		method.setAccessible(true);

		// Test with no cats added
		Cat result = (Cat) method.invoke(r, 2);

		// Ensure that getCat(2) returns null and prints "Invalid cat ID."
		assertNull(result);
		assertEquals("Invalid cat ID." + newline, out.toString());
	}

	@Test
	public void testGetCatNumCats3() throws Exception {
		// Add cats to RentACat
		r.addCat(c1);
		r.addCat(c2);
		r.addCat(c3);

		// Use reflection to access private getCat method
		java.lang.reflect.Method method = r.getClass().getDeclaredMethod("getCat", int.class);
		method.setAccessible(true);

		// Test that getCat(2) returns the correct cat
		Cat result = (Cat) method.invoke(r, 2);
		assertNotNull(result);
		assertEquals(2, result.getId());
	}

	@Test
	public void testListCatsNumCats0() {
		// Test listing cats when there are no cats
		assertEquals("", r.listCats());
	}

	@Test
	public void testListCatsNumCats3() {
		// Add cats to RentACat
		r.addCat(c1);
		r.addCat(c2);
		r.addCat(c3);

		// Test listing cats when three cats are present
		String expected = "ID 1. Jennyanydots\nID 2. Old Deuteronomy\nID 3. Mistoffelees\n";
		assertEquals(expected, r.listCats());
	}

	@Test
	public void testRenameFailureNumCats0() {
		// Test renaming a cat when no cats exist
		assertFalse(r.renameCat(2, "Garfield"));
		assertEquals("Invalid cat ID." + newline, out.toString());
	}

	@Test
	public void testRenameNumCat3() {
		// Add cats to RentACat
		r.addCat(c1);
		r.addCat(c2);
		r.addCat(c3);

		// Test renaming cat with ID 2
		assertTrue(r.renameCat(2, "Garfield"));
		assertEquals("Garfield", c2.getName());
	}

	@Test
	public void testRentCatNumCats3() {
		// Add cats to RentACat
		r.addCat(c1);
		r.addCat(c2);
		r.addCat(c3);

		// Test renting cat with ID 2
		assertTrue(r.rentCat(2));
		assertTrue(c2.getRented());
		assertEquals("Old Deuteronomy has been rented." + newline, out.toString());
	}

	@Test
	public void testRentCatFailureNumCats3() {
		// Add cats to RentACat
		r.addCat(c1);
		r.addCat(c2);
		r.addCat(c3);

		// Rent the cat with ID 2 first
		r.rentCat(2);
		out.reset();

		// Test renting the already rented cat with ID 2
		assertFalse(r.rentCat(2));
		assertEquals("Sorry, Old Deuteronomy is not here!" + newline, out.toString());
	}

	@Test
	public void testReturnCatNumCats3() {
		// Add cats to RentACat
		r.addCat(c1);
		r.addCat(c2);
		r.addCat(c3);

		// Rent and then return the cat with ID 2
		r.rentCat(2);
		out.reset();

		// Test returning the rented cat
		assertTrue(r.returnCat(2));
		assertEquals("Welcome back, Old Deuteronomy!" + newline, out.toString());
	}

	@Test
	public void testReturnFailureCatNumCats3() {
		// Add cats to RentACat
		r.addCat(c1);
		r.addCat(c2);
		r.addCat(c3);

		// Test returning a non-rented cat
		assertFalse(r.returnCat(2));
		assertEquals("Old Deuteronomy is already here!" + newline, out.toString());
	}
}
