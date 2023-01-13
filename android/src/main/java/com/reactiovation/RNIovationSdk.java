
package com.reactiovation;

import android.content.Context;
import android.widget.Toast;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.iovation.mobile.android.FraudForceConfiguration;
import com.iovation.mobile.android.FraudForceManager;


public class RNIovationSdk extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;

    public RNIovationSdk(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @Override
    public String getName() {
        return "RNIovationSdk";
    }

    @ReactMethod
    public void show(String text) {
        Context context = getReactApplicationContext();
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }


    @ReactMethod
    public void getBlackBoxString(String API_KEY, Callback callback) {

        if (API_KEY.isEmpty()) {
            //just return a message
            callback.invoke("API KEY can't be null or empty");
            return;
        }

        FraudForceConfiguration configuration = new FraudForceConfiguration.Builder()
                .subscriberKey(API_KEY).enableNetworkCalls(true) // Defaults to false if left out of configuration
                .build();
        FraudForceManager fraudForceManager = FraudForceManager.getInstance();
        Context context = getReactApplicationContext();
        fraudForceManager.initialize(configuration, context);
        FraudForceManager.getInstance().refresh(context);

        try {
            String blackBox = FraudForceManager.getInstance().getBlackbox(context);
            callback.invoke(blackBox); // Invoke the callback here
        } catch (Exception e) {
            callback.invoke(e.getMessage());
            // exception code here
        }
    }
}