package p1;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;

import test0.Sequence;

public class SequenceImpl implements Sequence{



/**
 * @param l1
 * @param l2
 * @return true if list t1 is a sublist of list t2, where at most one element of t1 may not occur in t2
 */

	public boolean subSeq(Object[] t1, Object[] t2) {
		if(t1.length <2)
			return true;
		
		int i = 0;
		for (int j = 0; j < t1.length; j++) {
			i = 0;
			for (int j2 = 0; j2 < t2.length; j2++) {
				if(i==j)
					i++;
				if(i==t1.length)
					return true;
				if(t1[i] == t2[j2])
					i++;
			}
			if(j==i)
				i++;
			if(i >= t1.length)
				return true;
		}		
		return false;
		/*if (t1.length > t2.length && t1.length - t2.length > 1)
			return false;
		
		int nbError = 0;

		for (int i = 0; i < t1.length; i++) {
			if(nbError >= 1 && i+1 > t2.length
					&& i+1==t1.length 
					&& i+1 > t2.length
					&& t1[i]!=t2[i-1])
				return false;
			if(nbError >= 1
					&& i+1 < t2.length
					&& t1[i-1] != t2[i] 
					&& t1[i] != t2[i-1] 
					&& t1[i] != t2[i]
					) {
				return false;
			}			
			
			if((nbError == 0 && i < t2.length && t1[i] != t2[i])){
				System.out.println("err: " + i);
				nbError++;
			}		

		}
		
		return nbError <= 1;
		/*return IntStream.range(0, t1.length)
				.parallel()
                .filter(i -> Arrays.stream(t2, i, t2.length).noneMatch(t1[i]::equals))                
                .count() <= 1;
		*/
	} 


}
