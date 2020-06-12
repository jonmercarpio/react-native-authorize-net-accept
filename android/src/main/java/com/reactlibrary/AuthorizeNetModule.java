package com.reactlibrary;

import android.util.Log;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;

import net.authorize.acceptsdk.AcceptSDKApiClient;
import net.authorize.acceptsdk.datamodel.merchant.ClientKeyBasedMerchantAuthentication;
import net.authorize.acceptsdk.datamodel.transaction.CardData;
import net.authorize.acceptsdk.datamodel.transaction.EncryptTransactionObject;
import net.authorize.acceptsdk.datamodel.transaction.TransactionObject;
import net.authorize.acceptsdk.datamodel.transaction.TransactionType;
import net.authorize.acceptsdk.datamodel.transaction.callbacks.EncryptTransactionCallback;
import net.authorize.acceptsdk.datamodel.transaction.response.EncryptTransactionResponse;
import net.authorize.acceptsdk.datamodel.transaction.response.ErrorTransactionResponse;

import java.util.HashMap;
import java.util.Map;

public class AuthorizeNetModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;
    static String LOGIN_ID = "loginID";
    static String CLIENT_KEY = "clientKey";
    static String CARD_NUMBER = "cardNumber";
    static String EXPIRATION_MONTH = "expirationMonth";
    static String EXPIRATION_YEAR = "expirationYear";
    static String CARD_HOLDER_NAME = "cardHolderName";
    static String ZIP_CODE = "zipCode";
    static String CARD_CVV = "cardCVV";
    private AcceptSDKApiClient apiClient;


    public AuthorizeNetModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @Override
    public String getName() {
        return "AuthorizeNet";
    }

    @ReactMethod
    public void getTokenWithRequestForCard(boolean isProduction, ReadableMap cardValue, final Callback callback) {

        try {

            apiClient = new AcceptSDKApiClient.Builder(reactContext, getEnvironment(isProduction))
                    .connectionTimeout(5000)
                    .build();

            EncryptTransactionObject transactionObject = TransactionObject
                    .createTransactionObject(TransactionType.SDK_TRANSACTION_ENCRYPTION)
                    .cardData(prepareCardDataFromFields(cardValue))
                    .merchantAuthentication(prepareMerchantAuthentication(cardValue))
                    .build();


            apiClient.getTokenWithRequest(transactionObject, new EncryptTransactionCallback() {
                @Override
                public void onErrorReceived(ErrorTransactionResponse error) {
                    WritableMap callbackResponse = Arguments.createMap();

                    callbackResponse.putString("status", "Error");
                    callbackResponse.putString("code", error.getFirstErrorMessage().getMessageCode());
                    callbackResponse.putString("text", error.getFirstErrorMessage().getMessageText());

                    callback.invoke(false, callbackResponse);
                }

                @Override
                public void onEncryptionFinished(EncryptTransactionResponse response) {
                    WritableMap callbackResponse = Arguments.createMap();

                    callbackResponse.putString("status", "Success");
                    callbackResponse.putString("dataValue", response.getDataValue());
                    callbackResponse.putString("dataDescriptor", response.getDataDescriptor());

                    callback.invoke(true, callbackResponse);

                }
            });

        } catch (Exception ex) {

            WritableMap callbackResponse = Arguments.createMap();

            callbackResponse.putString("status", "Error");
            callbackResponse.putString("code", ex.getClass().getCanonicalName());
            callbackResponse.putString("text", ex.getMessage());

            System.out.println(ex);

            callback.invoke(false, callbackResponse);
        }
    }

    private AcceptSDKApiClient.Environment getEnvironment(boolean isProduction) {
        return isProduction ?
                AcceptSDKApiClient.Environment.PRODUCTION :
                AcceptSDKApiClient.Environment.SANDBOX;
    }

    private ClientKeyBasedMerchantAuthentication prepareMerchantAuthentication(ReadableMap cardValue) {

        ClientKeyBasedMerchantAuthentication merchantAuthentication = ClientKeyBasedMerchantAuthentication
                .createMerchantAuthentication(
                        cardValue.hasKey(LOGIN_ID) ? cardValue.getString(LOGIN_ID) : null,
                        cardValue.hasKey(CLIENT_KEY) ? cardValue.getString(CLIENT_KEY) : null
                );

        return merchantAuthentication;
    }

    private CardData prepareCardDataFromFields(ReadableMap cardValue) {

        CardData.Builder builder = new CardData.Builder(
                cardValue.getString(CARD_NUMBER),
                cardValue.getString(EXPIRATION_MONTH),
                cardValue.getString(EXPIRATION_YEAR));

        if (cardValue.hasKey(CARD_HOLDER_NAME)) {
            builder.cardHolderName(cardValue.getString(CARD_HOLDER_NAME));
        }

        if (cardValue.hasKey(ZIP_CODE)) {
            builder.zipCode(cardValue.getString(ZIP_CODE));
        }

        if (cardValue.hasKey(CARD_CVV)) {
            builder.cvvCode(cardValue.getString(CARD_CVV));
        }

        return builder.build();
    }
}
