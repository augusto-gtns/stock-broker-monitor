package hbm.util.thread;

import java.util.concurrent.Callable;

public abstract class ExecutorImpl<Type> implements Callable<ExecutorResult<Type>> {
	
	private int index;
	
	public ExecutorImpl() {
		super();
	}

	@Override
	public ExecutorResult<Type> call() throws Exception {
		ExecutorResult<Type> result = new ExecutorResult<Type>(this.index);

		try {
			result.setResult(callExecutor());
		} catch (Exception e) {
			result.setException(e);
			result.setResult(null);
		}

		return result;
	}

	public abstract Type callExecutor() throws Exception;

	public abstract void loadResult(Type result) throws Exception;

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}


}
