import {useContext, useEffect, useState} from 'react';
import './styles.css';
import {CartContext} from '../../config/context';
import {apis, endpoints} from '../../config/apis';
import {Link} from 'react-router-dom';
import moment from 'moment';
import * as ultils from '../../config/utils';
import {type} from '@testing-library/user-event/dist/type';

const Cart = () => {
  const {cart, cartDispatcher} = useContext(CartContext);
  const [tickets, setTickets] = useState([]);

  const handleDeleteCart = (tripId, seatId) => {
    cartDispatcher({
      type: 'DELETE_ITEM',
      payload: {
        tripId: tripId,
        seatId: seatId,
      },
    });
  };

  const handleClearCart = () => {
    cartDispatcher({
      type: 'CLEAR_CART',
    });
  };

  const fetchTickets = async () => {
    const response = await apis.post(endpoints['ticket']['cart'], cart['data']);
    if (response) {
      setTickets(response['data']);
    }
  };
  useEffect(() => {
    if (cart['data'].length > 0) {
      fetchTickets();
    } else {
      setTickets([]);
    }
  }, [cart['key']]);
  return (
    <div
      className="offcanvas offcanvas-end w-50"
      data-bs-scroll="true"
      tabIndex="-1"
      id="offcanvasWithBothOptions"
      aria-labelledby="offcanvasWithBothOptionsLabel"
    >
      <div className="offcanvas-header">
        <h5
          className="offcanvas-title mt-5  p-3"
          id="offcanvasWithBothOptionsLabel"
        >
          Giỏ vé
        </h5>
        <button
          type="button"
          className="btn-close text-reset"
          data-bs-dismiss="offcanvas"
          aria-label="Close"
        ></button>
      </div>
      <div className="offcanvas-body p-4">
        <table className="table">
          <thead>
            <tr>
              <th>Thông tin vé</th>
              <th>Thao tác</th>
            </tr>
          </thead>
          <tbody>
            {tickets.map((ticket, index) => {
              return (
                <tr key={index}>
                  <td>
                    <ul className="list-unstyled">
                      <li>
                        <Link className="nav-link">
                          <span className="fw-bold">Tên chuyến:</span>{' '}
                          {ticket['routeInfo']['name']}
                        </Link>
                      </li>
                      <li>
                        <Link className="nav-link">
                          <span className="fw-bold">Tên công ty:</span>{' '}
                          {ticket['routeInfo']['company']['name']}
                        </Link>
                      </li>
                      <li>
                        <span className="fw-bold">Bến đi:</span>{' '}
                        {ticket['routeInfo']['fromStation']['name']}
                      </li>
                      <li>
                        <span className="fw-bold">Bến đến:</span>{' '}
                        {ticket['routeInfo']['toStation']['name']}
                      </li>
                      <li>
                        <span className="fw-bold">Điểm đón:</span>{' '}
                        {ticket['pickUpAddress']}
                      </li>
                      <li>
                        <span className="fw-bold">Khởi hành lúc</span>:{' '}
                        {moment(ticket['tripInfo']['departAt']).format('LLL')}
                      </li>
                      <li>
                        <span className="fw-bold">Mã ghế:</span>{' '}
                        {ticket['seatInfo']['code']}
                      </li>
                      <li>
                        <span className="fw-bold">Khối lượng hành lý:</span>{' '}
                        {ticket['luggage']} kg
                      </li>
                      <li>
                        <span className="fw-bold">Giá vé:</span>{' '}
                        {ultils.formatToVND(ticket['routeInfo']['seatPrice'])}
                      </li>
                    </ul>
                  </td>

                  <td>
                    <button
                      onClick={() =>
                        handleDeleteCart(
                          ticket['tripInfo']['id'],
                          ticket['seatInfo']['id'],
                        )
                      }
                      className="btn btn-danger"
                    >
                      Xóa
                    </button>
                  </td>
                </tr>
              );
            })}
          </tbody>
        </table>
        <div className="d-flex justify-content-end">
          <Link to={'/checkout'} className="btn btn-primary mx-1">
            Thanh toán
          </Link>
          <button onClick={handleClearCart} className="btn btn-danger mx-1">
            Xóa tất cả
          </button>
        </div>
      </div>
    </div>
  );
};

export default Cart;
