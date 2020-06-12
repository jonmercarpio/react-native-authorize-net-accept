import { NativeModules } from "react-native";
const { AuthorizeNet } = NativeModules;

const AuthorizeNetEnv = {
  PRODUCTION: true,
  SANDBOX: false,
};

/**
 *
 * @param {Object}  data - Data for calling the authorize method bridge
 * @param {boolean} data.env - Enviroment PRODUCTION or SANDBOX
 * @param {Object}  data.cardValues - This object is for card request
 * @param {string}  data.cardValues.loginID -  Login id
 * @param {string}  data.cardValues.clientKey - Client Key
 * @param {string}  data.cardValues.cardNumber - Card Number
 * @param {string}  data.cardValues.cardCVV - Card CVV
 * @param {string}  data.cardValues.expirationYear - Card Expiration Year
 * @param {string}  data.cardValues.expirationMonth - Card Expiration Month
 * @param {string}  data.cardValues.zipCode - Card Zip Code
 * @param {string}  data.cardValues.cardHolderName - Card Holder Name
 * @returns {Promise} A promise with the result
 * - onSucess
 * <pre>
 * <code>
 *  {
 *    "status": "Success",
 *    "dataValue": "....",
 *    "dataDescriptor": "..."
 *  }
 * </code>
 * </pre>
 *
 * - onError
 * <pre>
 * <code>
 *  {
 *    "status": "Error",
 *    "code": "....",
 *    "text": "..."
 *  }
 * </code>
 * </pre>
 */
const getTokenWithRequestForCard = (data) => {
  const { env, cardValues } = data;
  return new Promise((resolve, reject) => {
    const isProduction = env === AuthorizeNetEnv.PRODUCTION;

    AuthorizeNet.getTokenWithRequestForCard(
      isProduction,
      cardValues,
      (success, data) => {
        if (success) {
          resolve(data);
        } else {
          reject(data);
        }
      }
    );
  });
};

export { getTokenWithRequestForCard, AuthorizeNetEnv };
export default AuthorizeNet;
