import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;

public class Calculator24 {
	public static final int NUM_CARDS = 4; //normally we use 4 cards
	public static final String[] operators = {"+", "-", "*", "/" };
	
	public static void main(String[] args) {
		LinkedList<Integer> numbers = new LinkedList<Integer>();
		Scanner in = new Scanner(System.in);
		System.out.print("Enter your set of cards or type quit to exit. ");
		String input = "";
		while(!(input = in.nextLine()).equals("quit")) {
			if (!process(input, numbers)) {
				System.out.println("Your set of cards was invalid.");
			} else { //at this point numbers should contain the card values
				ArrayList<LinkedList<Integer>> permutations = findPermutations(numbers);
//|				for(LinkedList<Integer> perm: permutations) {
//|					System.out.println(perm);
//|				}
				LinkedList<ArrayList<String>> solutions = new LinkedList<ArrayList<String>>();
				for(LinkedList<Integer> permutation: permutations) {
					for(String op1: operators) {
						for(String op2:operators) {
							for(String op3: operators) { //ugly way of hard-coding the answers; working on the recursive method to make the calculator work for more than just sets of 4 numbers
								ArrayList<String> arr1 = new ArrayList<String>();
								arr1.add(Integer.toString(permutation.get(0)));
								arr1.add(Integer.toString(permutation.get(1)));
								arr1.add(Integer.toString(permutation.get(2)));
								arr1.add(Integer.toString(permutation.get(3)));
								arr1.add(op1);
								arr1.add(op2);
								arr1.add(op3);
								
								ArrayList<String> arr2 = new ArrayList<String>();
								arr2.add(Integer.toString(permutation.get(0)));
								arr2.add(Integer.toString(permutation.get(1)));
								arr2.add(op1);
								arr2.add(Integer.toString(permutation.get(2)));
								arr2.add(Integer.toString(permutation.get(3)));
								arr2.add(op2);
								arr2.add(op3);
								
								if((calculate(arr1) - 24) <= 0.00001) {
									solutions.add(arr1); 
								} else if ((calculate(arr2) - 24) <= 0.00001) {
									solutions.add(arr2);
								}
							}
						}
					}
					
				}
				//do stuff with the number set
				for(ArrayList<String> sol:solutions) {
					System.out.println(sol);
				}
			}
			numbers.clear();
			System.out.print("Enter your set of cards or type quit to exit. ");
		}
		in.close();
	}
	
	//return true if the set of cards was valid
	public static boolean process(String str, LinkedList<Integer> list) {
		String[] strSet = str.split(" ");
		for (int i = 0; i < NUM_CARDS; i++) {
			try {
				list.add(Integer.parseInt(strSet[i]));
			} catch (NumberFormatException e) {
				return false;
			} catch (ArrayIndexOutOfBoundsException e) {
				return false;
			}
		}
		return true;
	}
	
	public static double calculate(ArrayList<String> calculation) {
		Stack<Double> numbers = new Stack<Double>();
		Queue<Character> operations = new LinkedList<Character>();
		for(String str : calculation) {
			if (str.contains("+") || str.contains("-") || str.contains("*") || str.contains("/")) {
				operations.add(str.charAt(0));
			} else {
				numbers.push((double)Integer.parseInt(str)); //our cards are all integers
			}
			if(operations.isEmpty()) {
				continue; //do not try to do anything when there are no operations
			}
			switch (operations.poll()) { //note that as operations will always have only 0 or 1 elements, we could use a single temp char instead of the queue
			case '+':
				numbers.push(numbers.pop() + numbers.pop());
				break;
			case '-':
				double tempS = numbers.pop(); //need to subtract the right number from the left number
				numbers.push(numbers.pop() - tempS);
				break;
			case '*':
				numbers.push(numbers.pop() * numbers.pop());
				break;
			case '/':
				double tempD = numbers.pop(); //need to divide the right number from the left number
				if (tempD == 0) {
					return 0; //cannot divide by zero, and in this case we are only interested in the result equaling 24 or not; I'm also unsure of how compilers deal with dividing by 0 so no try-catching
				}
				numbers.push(numbers.pop() / tempD);
				break;
			default:
				break;
			}
		}
		return numbers.pop();
	}
	
	public static ArrayList<LinkedList<Integer>> findPermutations(LinkedList<Integer> perm) {
		if(perm.size() == 1) {
			ArrayList<LinkedList<Integer>> base = new ArrayList<LinkedList<Integer>>();
			base.add(perm);
			return base;
		}
		int head = perm.get(0);
		LinkedList<Integer> tail = subList(perm, 1);
		ArrayList<LinkedList<Integer>> subCombos = findPermutations(tail);
		ArrayList<LinkedList<Integer>> curCombos = new ArrayList<LinkedList<Integer>>();
		int subComboSize = subCombos.get(0).size();
		for(int i = 0; i < subCombos.size(); i++) { //for each pre-existing combo
			for(int j = 0; j <= subComboSize; j++) { //append head to each index
				curCombos.add(subList(subCombos.get(i), 0));
				curCombos.get(curCombos.size()-1).add(j, head);
				if(j < subComboSize && curCombos.get(curCombos.size()-1).get(j+1) == head) {
					break; //break out of inner loop to avoid calculating redundant combos
				}
			}
		}
		return curCombos;
	}
	
	public static LinkedList<Integer> subList(LinkedList<Integer> list, int index) {
		LinkedList<Integer> subList = new LinkedList<Integer>();
		while(index < list.size()) {
			subList.add(list.get(index));
			index++;
		}
		return subList;
		
	}
}
