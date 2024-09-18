package edu.pitt.cs;

import java.util.ArrayList;
import java.util.Scanner;

public class RentACatImpl implements RentACat {

    private ArrayList<Cat> cats = new ArrayList<Cat>();
    String newline = System.lineSeparator();

    public boolean returnCat(int id) {
        Cat cat = getCat(id);
        if (cat != null && cat.getRented()) {
            cat.returnCat();
            System.out.println("Welcome back, " + cat.getName() + "!");
            return true;
        } else {
            System.out.println(cat != null ? (cat.getName() + " is already here!") : "Invalid cat ID.");
            return false;
        }
    }

    public boolean rentCat(int id) {
        Cat cat = getCat(id);
        if (cat != null && !cat.getRented()) {
            cat.rentCat();
            System.out.println(cat.getName() + " has been rented.");
            return true;
        } else {
            System.out.println(cat != null ? ("Sorry, " + cat.getName() + " is not here!") : "Invalid cat ID.");
            return false;
        }
    }

    public boolean renameCat(int id, String name) {
        Cat cat = getCat(id);
        if (cat != null) {
            cat.renameCat(name);
            System.out.println("Cat ID " + id + " renamed to " + name);
            return true;
        } else {
            return false;
        }
    }

    public String listCats() {
        StringBuilder rentableCats = new StringBuilder();
        for (Cat cat : cats) {
            if (!cat.getRented()) {
                rentableCats.append(cat.toString()).append("\n");
            }
        }
        return rentableCats.length() > 0 ? rentableCats.toString() : "";
    }

    private Cat getCat(int id) {
        for (Cat cat : cats) {
            if (cat.getId() == id) {
                return cat;
            }
        }
        System.out.println("Invalid cat ID.");
        return null;
    }

    public void addCat(Cat c) {
        cats.add(c);
    }
	/**
	 * Main method
	 * 
	 * @param args - IGNORED, kept for compatibility
	 */
	public static void main(String[] args) {
		RentACat rc = new RentACatImpl();

		rc.addCat(new CatImpl(1, "Jennyanydots"));
		rc.addCat(new CatImpl(2, "Old Deuteronomy"));
		rc.addCat(new CatImpl(3, "Mistoffelees"));

		Scanner sc = new Scanner(System.in);

		int option;
		boolean keepGoing = true;

		while (keepGoing) {
			System.out.print("Option [1,2,3,4,5] > ");
			try {
				option = sc.nextInt();
				switch (option) {
					case 1:
						System.out.println("Cats for Rent");
						System.out.print(rc.listCats());
						break;
					case 2:
						System.out.print("Rent which cat? > ");
						try {
							int catIdToRent = sc.nextInt();
							rc.rentCat(catIdToRent);
						} catch (Exception ex) {
							System.out.println("Invalid cat ID.");
							sc.next();
							break;
						}
						break;
					case 3:
						System.out.print("Return which cat? > ");
						try {
							int catIdToReturn = sc.nextInt();
							rc.returnCat(catIdToReturn);
						} catch (Exception ex) {
							System.out.println("Invalid cat ID.");
							sc.next();
							break;
						}
						break;
					case 4:
						System.out.print("Rename which cat? > ");
						try {
							int catIdToRename = sc.nextInt();
							sc.nextLine(); // to flush the previous line
							System.out.print("What is the new name? > ");
							String newName = sc.nextLine();
							rc.renameCat(catIdToRename, newName);
						} catch (Exception ex) {
							System.out.println("Invalid cat ID.");
							sc.next();
							break;
						}
						break;
					case 5:
						keepGoing = false;
						break;
					default:
						throw new NumberFormatException();
				}
			} catch (Exception nfex) {
				System.err.println("Please enter 1, 2, 3, 4 or 5");
				System.err.println("1. See list of cats for rent");
				System.err.println("2. Rent a cat to a customer");
				System.err.println("3. Return a cat from a customer");
				System.err.println("4. Rename a cat");
				System.err.println("5. Quit");
				// Clear out the non-int in the scanner
				sc.next();
			}
		}

		System.out.println("Closing up shop for the day!");

		sc.close();
	}
}
