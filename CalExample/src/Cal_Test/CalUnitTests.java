package Cal_Test;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.io.IOException;

import Cal_Lib.Calculator;

class CalUnitTests {
	Calculator calTest;

	@BeforeClass
	public void setUpBeforeClass() throws Exception {
		calTest = new Calculator();
	}

	@Test
	public void setUp() throws Exception {
		assert(calTest != null);
	}
	
	@Test
	public void calculatorActiveCheck() {
		assert(calTest.active == true);
	}
	
	@Test
	public void calculatorCalStackInstance() {
		assert(calTest.c != null);
	}
	
	@Test
	public void calculatorInputInterface() {
		assert(calTest.scan != null);
	}
	
	@Test
	public void calstackOpeartorList() {
		assert(calTest.c.isOperator("+") == true);
		assert(calTest.c.isOperator("-") == true);
		assert(calTest.c.isOperator("*") == true);
		assert(calTest.c.isOperator("/") == true);
		assert(calTest.c.isOperator("^") == true);
	}
	
	
	
	@Test
	public void convertLinetoArray() {
		String input = "3+ 5 - -3 - (5 + 9 )";
		
		String[] inputArr = {"3","+","5","-","-","3","-","(","5","+","9",")"};
				
		assertEquals(inputArr, calTest.readInput(calTest.currentVal, input));
	}
	
	@Test
	public void prefix2Postfix() {
		String[] preFix = calTest.readInput(0,"4*5^2--6+(3/2)=+1");
				
		String[] postFix = {"4","5","2","^","*","-6","-","3","2","/","+","1","+"};
		
		assertEquals(postFix, calTest.c.infixToRPN(preFix));
	}
	
	@Test
	public void evaluateOperators() throws IOException {
		assertEquals(9, (int)calTest.c.calculateOp(4, 5, "+"));
		assertEquals(-1, (int)calTest.c.calculateOp(4, 5, "-"));
		assertEquals(20, (int)calTest.c.calculateOp(4, 5, "*"));
		assertEquals(20, (int)calTest.c.calculateOp(-4, -5, "*"));
		assertEquals(-20, (int)calTest.c.calculateOp(4, -5, "*"));
		assertEquals(0.8, calTest.c.calculateOp(4, 5, "/"));
		assertEquals(-0.8, calTest.c.calculateOp(-4, 5, "/"));
		assertEquals(0.8, calTest.c.calculateOp(-4, -5, "/"));
		assertEquals(1024, (int)calTest.c.calculateOp(4, 5, "^"));
		assertEquals(-1024, (int)calTest.c.calculateOp(-4, 5, "^"));
		assertEquals(0.0009765625, calTest.c.calculateOp(4, -5, "^"));
	}
	
	@Test
	public void evaluateInvalidOperators() {
		try {
			assertTrue( (int)calTest.c.calculateOp(4, 5, "") == 9);
		} catch (IOException e){
			System.out.println("Incorrect or lack or operator.");
		}
	}
	
	@Test
	public void evaluateEquation() {
		String[] preFixEquation = calTest.readInput(calTest.currentVal,"4*5^2--6+(3/2)=+1");
		
		String[] postFixEquation = calTest.c.infixToRPN(preFixEquation);
		
		assertEquals(108.5, calTest.c.evaluatePostfix(postFixEquation));
	}
	
	@Test
	public void evaluateEdgeEquations() {
		String[] edgecase1 = calTest.readInput(calTest.currentVal,"-2-5");
		
		String[] postFixEquation1 = calTest.c.infixToRPN(edgecase1);
		
		assertEquals(-7, (int)calTest.c.evaluatePostfix(postFixEquation1));
		
		String[]  edgecase2 = calTest.readInput(calTest.currentVal,"2--5");
		
		String[] postFixEquation2 = calTest.c.infixToRPN(edgecase2);
		
		assertEquals(7, (int)calTest.c.evaluatePostfix(postFixEquation2));
		
		assertEquals(3, (int)calTest.c.evaluatePostfix(calTest.c.infixToRPN(calTest.readInput(0, "3"))));
		
		assertEquals(3, (int)calTest.c.evaluatePostfix(calTest.c.infixToRPN(calTest.readInput(9, "3"))));
		
		try {
			assertEquals(3, (int)calTest.c.evaluatePostfix(calTest.c.infixToRPN(calTest.readInput(9, "((((+-322"))));
		} catch (NumberFormatException e) {
			
		}
	}
	
	@Test 
	public void clearCurrentSum() {
		calTest.currentVal = 5;
		assertEquals(5, (int)calTest.currentVal);
		calTest.clearCurrentSum();
		assertEquals(0, (int)calTest.currentVal);
		calTest.currentVal = -5;
		assertEquals(-5, (int)calTest.currentVal);
		calTest.clearCurrentSum();
		assertEquals(0, (int)calTest.currentVal);
		calTest.currentVal = 3.67;
		assertEquals(3.67, calTest.currentVal);
		calTest.clearCurrentSum();
		assertEquals(0, (int)calTest.currentVal);
	}
	
	@Test 
	public void calculatorCurrentTotal(){
		assertEquals(0, (int)calTest.currentVal);
	}
	
	@AfterClass
	public void tearDownAfterClass() throws Exception {
		calTest.quitCal();
		assert(calTest.active == false);
	}


}
