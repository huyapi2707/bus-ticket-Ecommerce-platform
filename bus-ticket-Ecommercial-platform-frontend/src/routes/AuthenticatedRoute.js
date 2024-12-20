import {useContext} from 'react';

import {Navigate, useLocation} from 'react-router-dom';
import {AuthenticationContext} from '../config/context';
import LoadingPage from '../pages/LoadingPage';

const AuthenticatedRoute = ({children}) => {
  const {user} = useContext(AuthenticationContext);

  let {pathname} = useLocation();
  const accessToken = localStorage.getItem('accessToken');
  if (!user) {
    if (accessToken) {
      return <LoadingPage />;
    } else {
      return <Navigate to={{pathname: '/login'}} state={{from: pathname}} />;
    }
  } else {
    return children;
  }
};

export default AuthenticatedRoute;
