package util;

public abstract class Sys {
	static public void abort(String errorMessage){
		System.err.println(errorMessage);
		System.exit(1);
	}
}
