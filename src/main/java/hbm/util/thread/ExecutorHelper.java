package hbm.util.thread;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import hbm.util.ExceptionUtil;

public class ExecutorHelper {
	
	public static void execute(String name, ExecutorImpl<?>... callables) {
		ExecutorFactory factory = new ExecutorFactory();

		factory.executor = Executors.newCachedThreadPool(new ThreadFactory() {
			@Override
			public Thread newThread(Runnable r) {
				return new Thread(r, name);
			}
		});

		factory.execute(callables);
	}

	public static class ExecutorFactory {

		final Logger log = LoggerFactory.getLogger(ExecutorFactory.class);

		private ExecutorService executor;

		@SuppressWarnings({ "rawtypes", "unchecked" })
		private void execute(ExecutorImpl<?>... callables) {

			CompletionService<ExecutorResult> completionService = new ExecutorCompletionService<>(executor);

			for (int i = 0; i < callables.length; i++) {
				ExecutorImpl callable = callables[i];
				callable.setIndex(i);

				try {
					completionService.submit(callable);
				} catch (Exception e) {
					log.error(e.getMessage());
				}
			}

			for (int i = 0; i < callables.length; i++) {
				try {
					Future<ExecutorResult> future = completionService.take();
					ExecutorResult executorCompletionResult = future.get();

					ExecutorImpl callable = findCallableById(Arrays.asList(callables),
							executorCompletionResult.getIndex());
					
					if (executorCompletionResult.getException() == null )
						callable.loadResult(executorCompletionResult.getResult());
					else
						log.error(ExceptionUtil.getStackTraceMinified(executorCompletionResult.getException()));

				} catch (Exception e) {
					log.error(e.getMessage());
				}
			}
		}

		private ExecutorImpl<?> findCallableById(List<ExecutorImpl<?>> callables, int id) {
			for (ExecutorImpl<?> callable : callables)
				if (callable.getIndex() == id)
					return callable;

			return null;
		}
	}
}
