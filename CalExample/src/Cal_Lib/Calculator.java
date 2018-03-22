package Cal_Lib;

import java.util.EmptyStackException;
import java.util.Scanner;

public class Calculator{
	
	public boolean active;
	public double currentVal;
	
	final String clear = "c";
	final String quit = "q";
	final String help = "h";
	final String printSum = "p";
	
	public CalStack c;
	public Scanner scan;
	
	public Calculator() {
		active = true;
		c = new CalStack();
		scan = new Scanner(System.in);
		System.out.println("Calculator is active. Enter your equations.");
		currentVal = 0;
		
	}
	
	public static void main(String[] args) {
		Calculator cal = new Calculator();
		cal.active();
	}
	
	public void active() {
		OUTER:
		while(active) {
			
			while(scan.hasNext()) {
				
				String input = scan.nextLine();	
				
				if(input.toLowerCase().contains(quit)) {
					active = quitCal();
					System.out.println("Shutting down calculator");
					break OUTER;
				}
				
				else if(input.toLowerCase().contains(printSum)) {
					printCurrentSum();
				}
				
				else if(input.toLowerCase().contains(clear)) {
					clearCurrentSum();
					System.out.println("Current Sum Cleared.");
				}
				
				else if(input.toLowerCase().contains(help)) {
					System.out.println(helpInfo());
				}
				
				else {
				
					String[] line = readInput(currentVal, input.trim());
					
					String[] outPut = c.infixToRPN(line);
					
					try {
						currentVal = c.evaluatePostfix(outPut);
						printCurrentSum();
						
					} catch (NumberFormatException e) {
						System.out.println("That equation is not supported.");
						System.out.println("Press Help or h for addtional info");
					}
					  catch (EmptyStackException e) {
						System.out.println("There is no operator between current sum and input.");
						System.out.println("Press Help or h for addtional info");
					}
				}
			}
		}
		scan.close();
	}
	
	
	public String[] readInput(double currentVal, String input) {
		String inputLine = "";
		if(currentVal != 0.0 && (input.substring(0,1).equals("+")
				||input.substring(0,1).equals("-")||input.substring(0,1).equals("*")
				||input.substring(0,1).equals("(")||input.substring(0,1).equals(")")
				||input.substring(0,1).equals("/")||input.substring(0,1).equals("^")
				||input.substring(0,1).equals("="))) {
			inputLine = currentVal + input;
		}
		else {
			inputLine = input;
		}
		
		inputLine = inputLine.replaceAll("\\s*", "");
		
		return inputLine.split("(?=\\d*\\.?\\d*)$|(?<=\\d*\\.?\\d*?)$|(?=[()=^*/+-])|(?<=[()=^*/+-])");
	}
	

	public boolean quitCal() {
		active = false;
		return active;
	}
	
	public String helpInfo() {
		String detailDoc = "Supported Operators: \n"
				+ "+ : Add 2 (Ex: 3 + 5 = 8) \n"
				+ "- : Subtract (Ex: 3 - 5 = -2) \n"
				+ "* : Mulutiply(Ex: 3 * 5 = 15) \n"
				+ "/ : Divide (Ex: 3 / 5 = 0.6) \n"
				+ "^ : Exponent (Ex: 3 ^ 5 = 243) \n"
				+ "() : Parentheses (Ex: (3 + 5) * 2 -> (15) * 2 = 30) \n"
				+ "= : Evaluate (Ex: 3 + 5 = * 2 -> 15 * 2 = 30) \n\n"
				+ "Clear or c: Clear current sum. \n"
				+ "Quit or q: Quit Calculator. \n"
				+ "Print or p: Print current Sum";
		
		return detailDoc;
	}
	
	public void printCurrentSum() {
		if (currentVal%1 == 0) {
			System.out.println("Current Sum: " + (int)currentVal);
		}
		else {
			System.out.println("Current Sum: " + currentVal);
		}
	}
	
	public void clearCurrentSum() {
		currentVal = 0;
	}


}
