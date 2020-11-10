package hbm.util.thread;

public class ExecutorResult<Type> {
	
	private final int index;
	private Type result;
	private Exception exception;

	public ExecutorResult(int index) {
		super();
		this.index = index;
	}

	public Type getResult() {
		return result;
	}

	public void setResult(Type result) {
		this.result = result;
	}

	public int getIndex() {
		return index;
	}

	public Exception getException() {
		return exception;
	}

	public void setException(Exception exception) {
		this.exception = exception;
	}

}
