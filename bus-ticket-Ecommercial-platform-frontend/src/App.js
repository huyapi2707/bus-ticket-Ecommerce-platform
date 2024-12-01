import './App.css';
import {
  LoadingContext,
  AuthenticationContext,
  cartReducer,
  CartContext,
  CompanyContext,
} from './config/context';

import {useEffect, useReducer, useRef, useState} from 'react';
import Loading from './components/Loading';
import {ToastContainer} from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import {authenticatedApis, endpoints} from './config/apis';
import AppRouter from './routes';


function App() {
  const [loading, setLoading] = useState('none');
  const [user, setUser] = useState(null);
  const [cart, cartDispatcher] = useReducer(cartReducer, {
    key: '',
    data: [],
  });
  const [company, setCompany] = useState(null);
  const fetchUserInfor = async () => {
    try {
      setLoading('flex');
      const response = await authenticatedApis().get(endpoints.user['self']);
      if (response) {
        setUser(response['data']);
      } else {
        localStorage.removeItem('accessToken');
      }
    } catch (ex) {
      localStorage.removeItem('accessToken');
    } finally {
      setLoading('none');
    }
  };
  useEffect(() => {
    const accessToken = localStorage.getItem('accessToken');
    if (accessToken && !user) {
      fetchUserInfor();
    }
  }, []);

  useEffect(() => {
    const localStorageItem = localStorage.getItem('cart');
    if (localStorageItem && cart['key'] === '' && localStorageItem !== '') {
      try {
        const temp = JSON.parse(localStorageItem);
        if (temp.length > 0) {
          cartDispatcher({
            type: 'ADD_TO_CART',
            payload: temp,
          });
        }
      } catch (ex) {
        console.error(ex);
      }
    } else {
      localStorage.setItem('cart', JSON.stringify(cart['data']));
    }
  }, [cart['key']]);
  return (
    <div className="container-fluid">
 
        <AuthenticationContext.Provider value={{user, setUser}}>
          <LoadingContext.Provider value={{loading, setLoading}}>
            <CartContext.Provider value={{cart, cartDispatcher}}>
              <CompanyContext.Provider value={{company, setCompany}}>
                <Loading />
                <ToastContainer />
                <AppRouter />
              </CompanyContext.Provider>
            </CartContext.Provider>
          </LoadingContext.Provider>
        </AuthenticationContext.Provider>
    </div>
  );
}

export default App;
