import Cart from '../components/Cart';
import {Routes, Route, BrowserRouter} from 'react-router-dom';
import Grid from '../components/Grid';
import RouteInfo from '../pages/RouteInfo';
import AuthenticatedRoute from './AuthenticatedRoute';
import ManagerRoute from './ManagerRoute';
import Checkout from '../pages/Checkout';
import Login from '../pages/Login';
import Register from '../pages/Register';
import Home from '../pages/Home';
import {endpoints} from '../config/apis';
import Profile from '../pages/Profile';
import CompanyInfo from '../pages/CompanyInfo';
import CustomerTicket from '../pages/CustomerTicket';
import ManagerChatSupport from '../pages/ManagerChatSupport';
import ManageCompanyLayout from '../components/ManageCompanyLayout';
import RegistCompanyRoute from './RegistCompanyRoute';
import RegistCompany from '../pages/RegistCompany';
import GoogleLoginCallBack from '../pages/GoogleLoginCallBack';
import VnPayPaymentCallBack from '../pages/VnPayPaymentCallBack';
const AppRouter = () => {
  return (
    <BrowserRouter>
      <Cart />
      <Routes>
        <Route path="/" element={<Home />}>
          <Route
            index={true}
            element={
              <>
                <Grid
                  title="Tuyến xe"
                  dataEndpoint={endpoints.route['list']}
                  breadcrumb={['Trang chủ', 'Tuyến xe']}
                />
                <Grid
                  title="Công ty"
                  dataEndpoint={endpoints.company['list']}
                  breadcrumb={['Trang chủ', 'Công ty']}
                />
              </>
            }
          />
          <Route path="/route/:id" element={<RouteInfo />} />
          <Route path="/company/:id" element={<CompanyInfo />} />
          <Route
            path="/checkout"
            element={
              <AuthenticatedRoute>
                <Checkout />
              </AuthenticatedRoute>
            }
          />
          <Route
            path="/ticket"
            element={
              <AuthenticatedRoute>
                <CustomerTicket />
              </AuthenticatedRoute>
            }
          />
          <Route
            path="/profile"
            element={
              <AuthenticatedRoute>
                <Profile />
              </AuthenticatedRoute>
            }
          />
          <Route
            path="regist_company"
            element={
              <AuthenticatedRoute>
                <RegistCompanyRoute>
                  <RegistCompany />
                </RegistCompanyRoute>
              </AuthenticatedRoute>
            }
          />
        </Route>
        <Route
          path="/manage_company"
          element={
            <ManagerRoute>
              <ManageCompanyLayout />
            </ManagerRoute>
          }
        >
          <Route
            path="/manage_company/chat_support"
            element={<ManagerChatSupport />}
          />
        </Route>

        <Route path="/login" element={<Login />} />
        <Route path='/login/oauth2/google' element={<GoogleLoginCallBack/>}/>
        <Route path="/register" element={<Register />} />
        <Route path="/vnpay-payment-result" element={<AuthenticatedRoute>
              <VnPayPaymentCallBack/>
        </AuthenticatedRoute>} />
      </Routes>
    </BrowserRouter>
  );
};

export default AppRouter;
