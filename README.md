# react-native-authorize-net-accept

## Getting started

`$ npm install react-native-authorize-net-accept --save`

### Mostly automatic installation

`$ react-native link react-native-authorize-net-accept`

## Usage
```javascript
import {
  AuthorizeNetEnv,
  getTokenWithRequestForCard,
} from 'react-native-authorize-net-accept';

const promise = getTokenWithRequestForCard({
    env: AuthorizeNetEnv.SANDBOX, //AuthorizeNetEnv.PRODUCTION
    cardValues: {
        loginID: '...',
        clientKey: '...',
        cardNumber: '4007000000027',
        cardCVV: '0000',
        expirationYear: 'YY',
        expirationMonth: 'MM',
        zipCode: '...', //Optional
        cardHolderName: '...' //Optional
    },
});

```
