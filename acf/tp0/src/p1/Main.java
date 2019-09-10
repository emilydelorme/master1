package p1;

import test0.Sequence;
import test0.Tester;


public class Main {
	public static void main(String[] args) {
		Sequence s= new SequenceImpl();
		Tester t = new Tester(s);
		t.go();
	}
}
 