//
//  AuthorizeNet.swift
//  AuthorizeNet
//
//  Created by Jonmer Carpio Dinó on 6/9/20.
//  Copyright © 2020 Facebook. All rights reserved.
//

import Foundation
import AuthorizeNetAccept

let LOGIN_ID: String = "loginID"
let CLIENT_KEY: String = "clientKey"
let CARD_NUMBER: String = "cardNumber"
let EXPIRATION_MONTH: String = "expirationMonth"
let EXPIRATION_YEAR: String  = "expirationYear"
let CARD_CVV: String = "cardCVV"
let ZIP_CODE: String = "zipCode"
let CARD_HOLDER_NAME: String  = "cardHolderName"

@objc(AuthorizeNet)
class AuthorizeNet: NSObject{
    
    func getOptionalValueForKey(values: NSDictionary, key: String) -> String? {
        return values.value(forKey: key) as? String
    }
    
    func getValueForKey(values: NSDictionary, key: String) -> String {
        if let value = values.value(forKey: key) as? String {
            return value
        }
        return String()
    }
    
    @objc(getTokenWithRequestForCard:cardValues:callback:)
    func getTokenWithRequestForCard(
        isProduction: Bool,
        cardValues: NSDictionary,
        callback: @escaping RCTResponseSenderBlock
    ) -> Void {
        
        let handler = AcceptSDKHandler(environment: isProduction ? AcceptSDKEnvironment.ENV_LIVE : AcceptSDKEnvironment.ENV_TEST)
        
        let request = AcceptSDKRequest()
        request.merchantAuthentication.name = getValueForKey(values: cardValues, key: LOGIN_ID)
        request.merchantAuthentication.clientKey = getOptionalValueForKey(values: cardValues, key: CLIENT_KEY)
        
        //Required
        request.securePaymentContainerRequest.webCheckOutDataType.token.cardNumber = getValueForKey(values: cardValues, key: CARD_NUMBER)
        request.securePaymentContainerRequest.webCheckOutDataType.token.expirationYear = getValueForKey(values: cardValues,key: EXPIRATION_YEAR)
        request.securePaymentContainerRequest.webCheckOutDataType.token.expirationMonth = getValueForKey(values: cardValues, key: EXPIRATION_MONTH)
        
        //Optionals
        request.securePaymentContainerRequest.webCheckOutDataType.token.cardCode = getOptionalValueForKey(values: cardValues, key: CARD_CVV)
        request.securePaymentContainerRequest.webCheckOutDataType.token.zip = getOptionalValueForKey(values: cardValues,key: ZIP_CODE)
        request.securePaymentContainerRequest.webCheckOutDataType.token.fullName = getOptionalValueForKey(values: cardValues, key: CARD_HOLDER_NAME)
        
        let successHandler = {
            (response: AcceptSDKTokenResponse) -> () in
            
            let opaqueData = response.getOpaqueData()
            
            let data = [
                "status": "Success",
                "dataValue": opaqueData.getDataValue(),
                "dataDescriptor": opaqueData.getDataDescriptor()
            ]
            
            callback([true, data])
        }
        
        let failureHandler = {
            (error: AcceptSDKErrorResponse) -> () in
            
            let message = error.getMessages().getMessages()[0];
            
            let data = [
                "status": error.getMessages().getResultCode(),
                "code": message.getCode(),
                "text": message.getText()
            ]
            
            callback([false, data])
        }
        
        
        handler!.getTokenWithRequest(
            request,
            successHandler: successHandler,
            failureHandler: failureHandler
        )
    }    
    
    @objc
    static func requiresMainQueueSetup() -> Bool {
        return true
    }
    
}
