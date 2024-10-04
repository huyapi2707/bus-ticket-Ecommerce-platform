import {useContext, useEffect} from 'react';
import {Outlet} from 'react-router-dom';
import {AuthenticationContext, CompanyContext} from '../../config/context';
import {authenticatedApis, endpoints} from '../../config/apis';
import ManageCompanyControl from '../../pages/ManageCompanyControl';
const ManageCompanyLayout = () => {
  const {company, setCompany} = useContext(CompanyContext);
  const {user} = useContext(AuthenticationContext);
  const fetchCompany = async () => {
    const requestUrl = endpoints['user'].company(user['id']);
    const response = await authenticatedApis().get(requestUrl);
    if (response) {
      setCompany(response['data']);
    }
  };
  useEffect(() => {
    if (!company) {
      fetchCompany();
    }
  }, []);
  return (
    company && (
      <>
        <ManageCompanyControl />
        <Outlet />;
      </>
    )
  );
};
export default ManageCompanyLayout;
