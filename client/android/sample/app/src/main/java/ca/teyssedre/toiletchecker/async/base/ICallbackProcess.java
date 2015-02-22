package ca.teyssedre.toiletchecker.async.base;

/**
 * Created by darky on 15-02-04,
 * at 22:12.
 */
public interface ICallbackProcess {
    void OnAsyncProcessResult(ProcessType registerProcess, Object result);
}
