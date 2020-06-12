// AuthorizeNet.m
#import <React/RCTBridgeModule.h>

@interface RCT_EXTERN_MODULE(AuthorizeNet, NSObject)

RCT_EXTERN_METHOD(
                  getTokenWithRequestForCard: (BOOL *)isProduction
                  cardValues: (NSDictionary *)cardValues
                  callback: (RCTResponseSenderBlock)callback
                  )

@end
