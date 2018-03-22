package Cal_Lib;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class CalStack {
	
	private static final int LEFT_ASSOC = 0;
	private static final int RIGHT_ASSOC = 1;
	
	private static final Map<String, int[]> OPERATORS = new HashMap<String, int[]>();
	static {
		OPERATORS.put("+", new int[] {0, LEFT_ASSOC});
		OPERATORS.put("-", new int[] {0, LEFT_ASSOC});
		OPERATORS.put("*", new int[] {5, LEFT_ASSOC});
		OPERATORS.put("/", new int[] {5, LEFT_ASSOC});
		OPERATORS.put("^", new int[] {10, RIGHT_ASSOC});
	}
	
	public CalStack () {
		
	}
	
	public boolean isOperator(String token) {
		return OPERATORS.containsKey(token);
	}
	
	private boolean isAssociative(String token, int type) {
		if(!isOperator(token)) {
			throw new IllegalArgumentException("Invalid token: " + token);
		}
		if(OPERATORS.get(token)[1] == type) {
			return true;
		}
		return false;
	}
	
	private final int cmpPrecedence(String token1, String token2) {
		if(!isOperator(token1) || !isOperator(token2)) {
			throw new IllegalArgumentException("Invalid tokens: " + token1 + " " + token2);
		}
		return OPERATORS.get(token1)[0] - OPERATORS.get(token2)[0];
	}
	
	public String[] infixToRPN(String[] inputTokens) {
		ArrayList<String> out = new ArrayList<String>();
		Stack<String> stack = new Stack<String>();
		
		for(int i=0; i<inputTokens.length; i++) {
			if(inputTokens[i].equals("-") 
					&& (i == 0  || isOperator(inputTokens[i-1])) && (i+1 == inputTokens.length-1
					|| !isOperator(inputTokens[i+1])  )){
				String fuse = inputTokens[i] + inputTokens[i+1];
				out.add(fuse);
				if (!(inputTokens[i+1].equals("(")) || !(inputTokens[i+1].equals(")"))){
					i++;
				}
			}
			
			else if(isOperator(inputTokens[i])) {
				while(!stack.empty() && isOperator(stack.peek())) {
					if((isAssociative(inputTokens[i], LEFT_ASSOC) && cmpPrecedence(inputTokens[i], stack.peek()) <= 0 )
							|| (isAssociative(inputTokens[i], RIGHT_ASSOC) && cmpPrecedence(inputTokens[i], stack.peek()) < 0)) {
						out.add(stack.pop());
						continue;
					}
					break;
				}
				stack.push(inputTokens[i]);
			}
			
			else if (inputTokens[i].equals("(")) {
				stack.push(inputTokens[i]);
			}
			else if (inputTokens[i].equals(")")) {
				while (!stack.empty() && !stack.peek().equals("(")) {
					out.add(stack.pop());
				}
				stack.pop();
			}
			else if (inputTokens[i].equals("=")) {
				while (!stack.empty()) {
					out.add(stack.pop());
				}
			}
			else {
				out.add(inputTokens[i]);
			}
		}
		while (!stack.empty()) {
			out.add(stack.pop());
		}
		String[] output = new String[out.size()];
		return out.toArray(output);
	}
	
	public double evaluatePostfix(String[] output) {
		double sum = 0;
		String currentSum = "";
		Stack<String> eval = new Stack<String>();
		for(int i = 0; i < output.length; i++) {
			if(output[i].equals("+") || output[i].equals("-") 
					|| output[i].equals("*") || output[i].equals("/") 
					|| output[i].equals("^")){
				double val2 = Double.parseDouble(eval.pop());
				double val1 = Double.parseDouble(eval.pop());
				try {
					currentSum = Double.toString(calculateOp(val1, val2, output[i]));
				} catch (IOException e) {
					e.printStackTrace();
				}
				output[i] = currentSum;
			}
			
			eval.push(output[i]);
		}
		sum = Double.parseDouble(eval.pop());
		
		
		return sum;
	}
	
	public double calculateOp(double val1, double val2, String op) throws IOException {
		double partialSum = 0;
		switch (op) {
		case "+":
			partialSum = val1 + val2;
			break;
		case "-":
			partialSum = val1 - val2;
			break;
		case "*":
			partialSum= val1 * val2;
			break;
		case "/":
			partialSum= val1 / val2;
			break;
		case "^":
			partialSum = Math.pow(val1, val2);
			break;
		default:
			throw new IOException();
		}
			
		return partialSum;
	}
	
	
	
	
}
