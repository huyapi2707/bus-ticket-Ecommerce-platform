import {useContext, useState} from 'react';
import './styles.css';
import {useEffect} from 'react';
import {LoadingContext, AuthenticationContext} from '../../config/context';
import {apis, authenticatedApis, endpoints} from '../../config/apis';
import moment from 'moment';
import * as utils from '../../config/utils';

const CustomerTicket = () => {
  const [tickets, setTickets] = useState([]);
  const {setLoading} = useContext(LoadingContext);
  const {user} = useContext(AuthenticationContext);

  const fetchTickets = async () => {
    try {
      setLoading('flex');

      const response = await authenticatedApis().get(
        endpoints['user'].tickets(user['id']),
      );
      setTickets(response['data']);
    } catch (ex) {
      console.error(ex);
    } finally {
      setLoading('none');
    }
  };
  useEffect(() => {
    fetchTickets();
  }, []);

  const renderActionBtn = (ticket, index) => {
    if (ticket['paidAt']) {
      return (
        <button
          onClick={() => {
            handldeComment(
              ticket['ticketId'],
              ticket['routeInfo']['company']['id'],
            );
          }}
          className="btn btn-primary"
        >
          Đánh giá
        </button>
      );
    } else {
      if (
        new Date(ticket['tripInfo']['departAt']) < new Date() &&
        ticket['paymentMethod']['name'] !== 'CASH'
      ) {
        return (
          <button disabled={true} className="btn btn-danger">
            Hủy vé
          </button>
        );
      }
      return (
        <button
          className="btn btn-danger"
          onClick={() => handleDeleteTicket(index, ticket['ticketId'])}
        >
          Hủy vé
        </button>
      );
    }
  };
  const handleDeleteTicket = async (index, id) => {
    if (window.confirm(`Bạn có chắc hủy vé? Mã vé: ${id}`)) {
      try {
        setLoading('flex');

        const response = await authenticatedApis().delete(
          endpoints['ticket'].delete(id),
        );

        if (response['status'] === 204) {
          const newArrTickets = tickets;
          newArrTickets[id] = response['data'];
          setTickets(newArrTickets);
        }
      } catch (ex) {
        console.error(ex);
      } finally {
        setLoading('none');
      }
    }
  };
  const handldeComment = (ticketId, companyId) => {};

  return (
    <div className="container-fluid mt-5 shadow p-3 mb-5 bg-body rounded ">
      <div className="row">
        <h3>Vé xe của bạn</h3>
      </div>
      <div className="row">
        <table className="table table-hover ">
          <thead className="ticket-table-header">
            <tr>
              <th>Mã vé</th>
              <th>Mã chuyến</th>
              <th>Công ty phụ trách</th>
              <th>Giá vé</th>
              <th>Bến khởi hành</th>
              <th>Bến đến</th>
              <th>Mã ghế</th>
              <th>Khởi hành lúc</th>
              <th>Khối lượng hành lý</th>
              <th>Phương thức thanh toán</th>
              <th>Đã thanh toán</th>
              <th>Hành động</th>
            </tr>
          </thead>
          <tbody className="ticket-table-body">
            {tickets.map((ticket, index) => (
              <tr key={ticket['id']}>
                <td>{ticket['id']}</td>
                <td>{ticket['routeInfo']['name']}</td>
                <td>{ticket['routeInfo']['company']['name']}</td>
                <td>{utils.formatToVND(ticket['seatPrice'])}</td>
                <td>{ticket['routeInfo']['fromStation']['name']}</td>
                <td>{ticket['routeInfo']['toStation']['name']}</td>
                <td>{ticket['seatInfo']['code']}</td>
                <td>{moment(ticket['tripInfo']['departAt']).format('LLL')}</td>
                <td>{ticket['luggage']}</td>
                <td>{ticket['paymentMethod']['name']}</td>
                <td>
                  {ticket['paidAt']
                    ? moment(ticket['paidAt']).format('LLL')
                    : 'Chưa thanh toán'}
                </td>
                <td>{renderActionBtn(ticket, index)}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default CustomerTicket;
