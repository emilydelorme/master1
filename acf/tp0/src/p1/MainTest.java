package p1;


import static org.junit.Assert.*;
import org.junit.Test;

import test0.Sequence;
import p1.SequenceImpl;

public class MainTest {
	Sequence s= new SequenceImpl();
	
	@Test
	public void test1() {		
		Integer[] t1 = new Integer[] { 1, 2, 3 };
		Integer[] t2 = new Integer[] { 1, 2, 3, 4};
		
		assertTrue(s.subSeq(t1,t2));
	}
	
	@Test
	public void test2() {
		System.out.println("2");
		Integer[] t1 = new Integer[] { 1, 2, 2, 3 };
		Integer[] t2 = new Integer[] { 2, 2, 1, 3, 4};
		
		assertTrue(s.subSeq(t1,t2));
	}
	
	@Test
	public void test3() {
		System.out.println("3");
		Integer[] t1 = new Integer[] { 1 };
		Integer[] t2 = new Integer[] { 1, 2, 3, 4};
		
		assertTrue(s.subSeq(t1,t2));
	}
	
	@Test
	public void test4() {
		System.out.println("4");
		Integer[] t1 = new Integer[] { 0, 2, 3 };
		Integer[] t2 = new Integer[] { 1, 2, 3, 4};
		
		assertTrue(s.subSeq(t1,t2));
	}
	
	@Test
	public void test5() {
		Integer[] t1 = new Integer[] { 1, 2, 3, 3, 3 };
		Integer[] t2 = new Integer[] { 1, 2, 3, 4};
		
		assertFalse(s.subSeq(t1,t2));
	}
	
	@Test
	public void test6() {
		Integer[] t1 = new Integer[] { 1, 2, 3, 3, 3 };
		Integer[] t2 = new Integer[] { 1, 2};
		
		assertFalse(s.subSeq(t1,t2));
	}

	@Test
	public void test7() {
		Integer[] t1 = new Integer[] { 1, 2, 3 };
		Integer[] t2 = new Integer[] { 1, 2};
		
		assertTrue(s.subSeq(t1,t2));
	}
	
	@Test
	public void test8() {
		Integer[] t1 = new Integer[] { 1, 2, 3, 3, 4 };
		Integer[] t2 = new Integer[] { 1, 2, 3, 4};
		
		assertTrue(s.subSeq(t1,t2));
	}
	
	@Test
	public void test9() {
		Integer[] t1 = new Integer[] { 1, 2, 3, 3};
		Integer[] t2 = new Integer[] { 1, 2, 3, 4};
		
		assertTrue(s.subSeq(t1,t2));
	}
	
	@Test
	public void test10() {
		Integer[] t1 = new Integer[] {};
		Integer[] t2 = new Integer[] {};
		
		assertTrue(s.subSeq(t1,t2));
	}
	
	@Test
	public void test11() {
		Integer[] t1 = new Integer[] {};
		Integer[] t2 = new Integer[] { 1 };
		
		assertTrue(s.subSeq(t1,t2));
	}
	
	@Test
	public void test12() {
		Integer[] t1 = new Integer[] { 1 };
		Integer[] t2 = new Integer[] {};
		
		assertTrue(s.subSeq(t1,t2));
	}
	
	@Test
	public void test13() {
		Integer[] t1 = new Integer[] { 1, 2 };
		Integer[] t2 = new Integer[] {};
		
		assertFalse(s.subSeq(t1,t2));
	}
	
	@Test
	public void test14() {
		Integer[] t1 = new Integer[] { 1, 2, 3, 4 };
		Integer[] t2 = new Integer[] { 1, 2, 3, 3, 4};
		
		assertTrue(s.subSeq(t1,t2));
	}
	
	@Test
	public void test15() {
		Integer[] t1 = new Integer[] { 1, 2, 3, 4 };
		Integer[] t2 = new Integer[] { 1, 2, 3, 3, 4};
		
		assertTrue(s.subSeq(t1,t2));
	}
	
	@Test
	public void test16() {
		Integer[] t1 = new Integer[] { 1, 2, 3, 4 };
		Integer[] t2 = new Integer[] { 2, 3, 4};
		
		assertTrue(s.subSeq(t1,t2));
	}
	
	@Test
	public void test17() {
		Integer[] t1 = new Integer[] { 1, 2, 3, 4 };
		Integer[] t2 = new Integer[] { 1, 3, 4};
		
		assertTrue(s.subSeq(t1,t2));
	}
}
