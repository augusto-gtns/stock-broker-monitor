package hbm.util.thread;

public abstract class ExecutorVoidImpl extends ExecutorImpl<Void> {
	
	public abstract void callVoidExecutor() throws Exception;
	
	@Override
	public Void callExecutor() throws Exception {
		callVoidExecutor();
		return null;
	}
	
	@Override
	public void loadResult(Void result) throws Exception {
		//void impl
	}
}
