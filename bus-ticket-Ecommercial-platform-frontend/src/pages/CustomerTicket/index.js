import React, {useContext, useState, useRef} from 'react';
import './styles.css';
import {useEffect} from 'react';
import {LoadingContext, AuthenticationContext} from '../../config/context';
import {authenticatedApis, endpoints} from '../../config/apis';
import moment from 'moment';
import * as utils from '../../config/utils';
import {toast} from 'react-toastify';
import TicketDetails from '../../components/TicketDetails/TicketDetails';
const statusInfo = {
  ALL: {
    title: 'Tất cả',
    color: 'white',
  },
  UNPAID: {
    title: 'Chưa thanh toán',
    color: '#008000',
  },
  COMPLETED: {
    title: 'Hoàn thành',
    color: '#2E5A88',
  },
  ABSENTED: {
    title: 'Vắng mặt',
    color: '#000000',
  },
  REFUNDED: {
    title: 'Đã hoàn thành',
    color: '#0D6EFD',
  },
  PAID: {
    title: 'Đã thanh toán',
    color: '#FFA500',
  },
  CANCELED: {
    title: 'Đã hủy',
    color: '#DC3545',
  },
};
const CustomerTicket = () => {
  const [tickets, setTickets] = useState([]);
  const {setLoading} = useContext(LoadingContext);
  const {user} = useContext(AuthenticationContext);
  const [pageTotal, setPageTotal] = useState(0);
  const [page, setPage] = useState(1);
  const [ticketStatus, setTicketStatus] = useState('ALL');
  const isTicketStatusChanged = useRef(false);
  const renderStatusClassname = (statusName) => {
    return statusInfo[statusName]['color'];
  };

  const fetchTickets = async () => {
    try {
      setLoading('flex');
      let endpoint =
        endpoints['user'].tickets(user['id']) + '?' + `page=${page}`;
      if (ticketStatus !== 'ALL') {
        endpoint += `&status=${ticketStatus}`;
      }
      const response = await authenticatedApis().get(endpoint);
      if (response) {
        setTickets(response['data']['results']);
        setPageTotal(response['data']['totalPage']);
      }
    } catch (ex) {
      console.error(ex);
    } finally {
      setLoading('none');
    }
  };

  const handleChangeTicketStatus = (value) => {
    isTicketStatusChanged.current = true;
    setTicketStatus(value);
  };

  useEffect(() => {
    if (isTicketStatusChanged.current && page === 1) {
      isTicketStatusChanged.current = false;
      fetchTickets();
    } else if (isTicketStatusChanged.current && page !== 1) {
      setPage(1);
      isTicketStatusChanged.current = false;
    } else {
      fetchTickets();
    }
  }, [page, ticketStatus]);

  const renderActionBtn = (ticket, index) => {
    if (ticket['status']['name'] === 'UNPAID') {
      return (
        <button
          onClick={() => handleDeleteTicket(index, ticket['id'])}
          className="btn btn-danger"
        >
          Hủy vé
        </button>
      );
    }
    if (ticket['status']['name'] === 'COMPLETED') {
      return <button className="btn btn-primary">Đánh giá</button>;
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
          newArrTickets[index]['status']['name'] = 'CANCELED';
          toast.success('Bạn đã hủy vé thành công', {
            position: 'top-center',
            autoClose: 5000,
            closeOnClick: true,
            pauseOnHover: true,
            draggable: true,
            progress: undefined,
            theme: 'colored',
          });
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
      <div className="d-flex justify-content-around mb-3">
        {Object.keys(statusInfo).map((key) => {
          return (
            <div className="d-flex align-items-center" key={key}>
              <input
                checked={ticketStatus === key}
                onChange={(e) => handleChangeTicketStatus(e.target.value)}
                id={key}
                value={key}
                type="radio"
              ></input>
              <label htmlFor={key} className="mx-2">
                {statusInfo[key]['title']}
              </label>
              <div
                className="ticket-status mx-2"
                style={{
                  backgroundColor: renderStatusClassname(key),
                }}
              ></div>
            </div>
          );
        })}
      </div>
      <div className="row">
        <table className="table table-hover ">
          <thead className="ticket-table-header">
            <tr>
              <th>Mã vé</th>
              <th>Mã chuyến</th>
              <th>Bến khởi hành</th>
              <th>Bến đến</th>
              <th>Khởi hành lúc</th>
              <th>Mã ghế</th>
              <th>Giá vé</th>
              <th>Trạng thái</th>
              <th>Hành động</th>
            </tr>
          </thead>
          <tbody className="ticket-table-body">
            {tickets.map((ticket, index) => (
              <tr className="customer-ticket" key={ticket['id']}>
                <td>{ticket['id']}</td>
                <td>{ticket['routeInfo']['name']}</td>
                <td>{ticket['routeInfo']['fromStation']['name']}</td>
                <td>{ticket['routeInfo']['toStation']['name']}</td>
                <td>{moment(ticket['tripInfo']['departAt']).format('LLL')}</td>
                <td>{ticket['seatInfo']['code']}</td>
                <td>{utils.formatToVND(ticket['seatPrice'])}</td>
                <td>
                  <div
                    className="ticket-status"
                    style={{
                      backgroundColor: renderStatusClassname(
                        ticket['status']['name'],
                      ),
                    }}
                  ></div>
                </td>
                <td>{renderActionBtn(ticket, index)}</td>
                <TicketDetails ticket={ticket} />
              </tr>
            ))}
          </tbody>
        </table>
      </div>
      <div className="row mt-5 " aria-label="page">
        <ul className="pagination d-flex justify-content-center">
          <li className="page-item">
            <button
              className="page-link"
              onClick={() => {
                if (page > 1) {
                  setPage((page) => page - 1);
                }
              }}
              href="#"
              aria-label="Previous"
            >
              <span aria-hidden="true">&laquo;</span>
            </button>
          </li>
          {[...Array(pageTotal)].map((value, index) => {
            return (
              <li key={index} className="page-item">
                <button
                  onClick={() => {
                    setPage(index + 1);
                  }}
                  className={[
                    'page-link',
                    page === index + 1 ? 'active' : null,
                  ].join(' ')}
                >
                  {index + 1}
                </button>
              </li>
            );
          })}
          <li className="page-item">
            <button
              className="page-link"
              onClick={() => {
                if (page < pageTotal) {
                  setPage((page) => page + 1);
                }
              }}
              href="#"
              aria-label="Previous"
            >
              <span aria-hidden="true">&raquo;</span>
            </button>
          </li>
        </ul>
      </div>
    </div>
  );
};

export default CustomerTicket;
