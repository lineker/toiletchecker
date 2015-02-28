package ca.teyssedre.toiletchecker.async;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;

import ca.teyssedre.toiletchecker.async.base.ICallbackProcess;
import ca.teyssedre.toiletchecker.async.base.ProcessType;
import ca.teyssedre.toiletchecker.utils.Constantes;

/**
 * Created by darky on 15-02-04,
 * at 22:05.
 */
public class RegistrationGcmProcess extends AsyncTask<String,String,String>{

    private Activity _mActivity;
    private Context _mContext;
    private GoogleCloudMessaging _gcm;

    public RegistrationGcmProcess(Activity activity) {
        if(activity == null || !(activity instanceof ICallbackProcess)){
            throw new RuntimeException();
        }
        this._mActivity = activity;
        this._mContext = activity.getApplicationContext();
        this._gcm = GoogleCloudMessaging.getInstance(_mContext);
    }

    @Override
    protected String doInBackground(String... params) {
        if(_mContext != null){
            try {
                return _gcm.register(Constantes.SENDER_ID);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if(_mActivity != null){
            ((ICallbackProcess)_mActivity).OnAsyncProcessResult(ProcessType.REGISTER_PROCESS,s);
        }
    }
}
