import {createContext} from 'react';

const LoadingContext = createContext('none');
const AuthenticationContext = createContext(null);
const CartContext = createContext(null);
const CompanyContext = createContext(null);

const cartReducer = (currentState, action) => {
  switch (action.type) {
    case 'ADD_TO_CART': {
      const newData = action['payload'];
      const removedItemCart = currentState['data'].filter((item) => {
        return newData.find(
          (newItem) =>
            newItem['tripId'] !== item['tripId'] &&
            newItem['seatInfo']['id'] !== item['seatInfo']['id'],
        );
      });
      return {
        key: new Date().getTime(),
        data: [...removedItemCart, ...newData],
      };
    }
    case 'DELETE_ITEM': {
      const payload = action['payload'];
      const newArrCart = currentState['data'].filter((item) => {
        if (
          item['tripId'] == payload['tripId'] &&
          item['seatInfo']['id'] == payload['seatId']
        ) {
          return false;
        }
        return true;
      });

      return {
        key: new Date().getTime(),
        data: newArrCart,
      };
    }
    case 'CLEAR_CART': {
      return {
        key: new Date().getTime(),
        data: [],
      };
    }
  }
};

export {
  CompanyContext,
  LoadingContext,
  AuthenticationContext,
  CartContext,
  cartReducer,
};
