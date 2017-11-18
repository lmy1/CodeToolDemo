package demo;

import java.util.Arrays;

public class Test1 {
	public static void main(String[] args) {
		Long[] arr= {1L,4L,7L,9L};
		String arrs = Arrays.toString(arr);
		String substring = arrs.substring(1, arrs.length()-1);
		System.out.println(substring);
	}
}
