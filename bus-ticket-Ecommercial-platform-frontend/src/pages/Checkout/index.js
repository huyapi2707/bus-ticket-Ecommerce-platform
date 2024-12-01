import {useContext, useEffect, useState} from 'react';
import './styles.css';
import moment from 'moment';
import {
  AuthenticationContext,
  CartContext,
  LoadingContext,
} from '../../config/context';
import {apis, authenticatedApis, endpoints} from '../../config/apis';
import * as ultils from '../../config/utils';
import {toast} from 'react-toastify';
import TicketDetails from '../../components/TicketDetails/TicketDetails';
const Checkout = () => {
  const {user} = useContext(AuthenticationContext);
  const {setLoading} = useContext(LoadingContext);
  const {cart, cartDispatcher} = useContext(CartContext);
  const [paymentMethods, setPaymentMethods] = useState([]);
  const [selectedPaymentMethod, setSelectedPaymentMethod] = useState(0);
  const [tickets, setTickets] = useState([]);

  const fetchPaymentMethod = async () => {
    try {
      setLoading('flex');
      const response = await apis.get(endpoints['payment']['list']);
      const data = response['data'];
      setPaymentMethods(data);
      setSelectedPaymentMethod(data[0]['id']);
    } catch (ex) {
      console.error(ex);
    } finally {
      setLoading('none');
    }
  };

  const fetchCartDetails = async () => {
    try {
      setLoading('flex');
      const response = await apis.post(
        endpoints['ticket']['cart'],
        cart['data'],
      );
      setTickets(response['data']);
    } catch (ex) {
      console.log(ex);
    } finally {
      setLoading('none');
    }
  };

  useEffect(() => {
    fetchPaymentMethod();
  }, []);

  useEffect(() => {
    fetchCartDetails();
  }, [cart['key']]);

  const handleCheckout = async () => {
    try {
      setLoading('flex');

      const response = await authenticatedApis().post(
        endpoints['ticket'].create(selectedPaymentMethod),
        cart['data'],
      );

      const {paymentUrl} = response['data'];
      
      if (paymentUrl === null) {
        toast.success('Đơn hàng của bạn đã được xử lý thành công', {
          position: 'top-center',
          autoClose: 5000,
          closeOnClick: true,
          pauseOnHover: true,
          draggable: true,
          progress: undefined,
          theme: 'colored',
        });
      } else {
        window.location.replace(paymentUrl)
      }
    } catch (error) {
      toast.error('Đã xảy ra lỗi khi xử lý đơn hàng của bạn', {
        position: 'top-center',
        autoClose: 5000,
        closeOnClick: true,
        pauseOnHover: true,
        draggable: true,
        progress: undefined,
        theme: 'colored',
      });
      console.error(error);
    } finally {
      cartDispatcher({
        type: 'CLEAR_CART',
      });

      setLoading('none');
    }
  };

  return (
    <div className="container-fluid mt-5 ">
      <div className="row">
        <div className="col-md-5">
          <div className="shadow-sm p-3 mb-5 bg-body rounded">
            <h4>Thông tin của bạn</h4>
            <div className="form mt-4">
              <div className="mb-3">
                <label htmlFor="email" className="form-label">
                  Họ và tên lót
                </label>
                <input
                  type="email"
                  className="form-control"
                  id="email"
                  value={user['firstname']}
                  disabled
                  readOnly
                />
              </div>
              <div className="mb-3">
                <label htmlFor="lastname" className="form-label">
                  Tên
                </label>
                <input
                  type="text"
                  className="form-control"
                  id="lastname"
                  value={user['lastname']}
                  disabled
                  readOnly
                />
              </div>
              <div className="mb-3">
                <label htmlFor="email" className="form-label">
                  Email
                </label>
                <input
                  type="email"
                  className="form-control"
                  id="email"
                  value={user['email']}
                  disabled
                  readOnly
                />
              </div>
              <div className="mb-3">
                <label htmlFor="phone" className="form-label">
                  Số điện thoại
                </label>
                <input
                  type="email"
                  className="form-control"
                  id="phone"
                  value={user['phone']}
                  disabled
                  readOnly
                />
              </div>
              <div className="mb-3">
                <label htmlFor="paymentMethod" className="form-label">
                  Phương thức thanh toán
                </label>
                <select
                  onChange={(e) => setSelectedPaymentMethod(e.target.value)}
                  value={selectedPaymentMethod}
                  className="form-select"
                  id="paymentMethod"
                >
                  {paymentMethods.map((method) => (
                    <option value={method['id']} key={method['id']}>
                      {method['name']}
                    </option>
                  ))}
                </select>
              </div>
              <div className="mt-3 d-flex justify-content-center align-items-center">
                <button
                  disabled={tickets.length === 0 ? 'disabled' : null}
                  onClick={handleCheckout}
                  className=" btn btn-primary"
                >
                  Đặt vé
                </button>
              </div>
            </div>
          </div>
        </div>
        <div className="col-md-7">
          <div className="shadow-sm p-3 mb-5 bg-body rounded">
            <h4 className="fw-bold">Đơn hàng</h4>
            <table className="table">
              <thead>
                <tr>
                  <th>Mã chuyến</th>
                  <th>Tuyến</th>
                  <th>Điểm đón</th>
                  <th>Khởi hành lúc</th>
                  <th>Ghế</th>
                  <th>Khối lượng hành lý</th>
                  <th>Giá ghế</th>
                </tr>
              </thead>
              <tbody>
                {tickets.map((ticket, index) => {
                  return (
                    <tr className="customer-ticket" key={index}>
                      <td>
                        <p>{ticket['routeInfo']['name']}</p>
                      </td>
                      <td>
                        <p>
                          {ticket['routeInfo']['fromStation']['name']}
                          {' đến '}
                          {ticket['routeInfo']['toStation']['name']}
                        </p>
                      </td>
                      <td>{ticket['pickUpAddress']}</td>
                      <td>
                        {moment(ticket['tripInfo']['departAt']).format('LLL')}
                      </td>
                      <td>{ticket['seatInfo']['code']}</td>
                      <td>{ticket['luggage']}Kg</td>
                      <td>{ultils.formatToVND(ticket['seatPrice'])}</td>
                      <TicketDetails ticket={ticket} />
                    </tr>
                  );
                })}
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Checkout;
