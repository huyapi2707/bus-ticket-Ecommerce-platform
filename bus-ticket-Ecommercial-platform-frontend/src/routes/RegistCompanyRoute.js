import {useContext} from 'react';
import {AuthenticationContext} from '../config/context';
import {Navigate} from 'react-router-dom';

const RegistCompanyRoute = ({children}) => {
  const {user} = useContext(AuthenticationContext);

  if (user['role'] === 'COMPANY_MANAGER') {
    return <Navigate to="/manage_company" />;
  } else return children;
};

export default RegistCompanyRoute;
