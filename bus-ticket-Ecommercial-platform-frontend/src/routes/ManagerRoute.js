import {useContext} from 'react';

import {Navigate, useLocation} from 'react-router-dom';
import {AuthenticationContext} from '../config/context';
import LoadingPage from '../components/LoadingPage';

const ManagerRoute = ({children}) => {
  const {user} = useContext(AuthenticationContext);

  let {pathname} = useLocation();
  const accessToken = localStorage.getItem('accessToken');

  if (!user) {
    if (accessToken) {
      return <LoadingPage />;
    } else {
      return <Navigate to={'/login'} state={{from: pathname}} />;
    }
  } else {
    if (user['role'] === 'COMPANY_MANAGER') {
      return children;
    } else {
      return <Navigate to={'/create-company'} />;
    }
  }
};

export default ManagerRoute;
