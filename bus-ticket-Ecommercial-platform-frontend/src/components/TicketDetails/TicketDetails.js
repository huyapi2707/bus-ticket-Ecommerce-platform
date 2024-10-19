import moment from 'moment';
import * as utils from '../../config/utils';
import './styles.css';
const TicketDetails = ({ticket}) => {
  return (
    <div className="ticket-details shadow-lg p-2">
      <ul className="list-group list-group-flush">
        {ticket['id'] && (
          <li className="list-group-item">
            <span className="fw-bold">Mã vé</span>: {ticket['id']}
          </li>
        )}
        <li className="list-group-item">
          <span className="fw-bold">Công ty</span>:{' '}
          {ticket['routeInfo']['company']['name']}
        </li>
        <li className="list-group-item">
          <span className="fw-bold">Mã chuyến</span>:{' '}
          {ticket['routeInfo']['name']}
        </li>
        <li className="list-group-item">
          <span className="fw-bold">Bến khởi hành</span>:{' '}
          {ticket['routeInfo']['fromStation']['name']}
        </li>
        <li className="list-group-item">
          <span className="fw-bold">Bến đến</span>:{' '}
          {ticket['routeInfo']['toStation']['name']}
        </li>
        <li className="list-group-item">
          <span className="fw-bold">Điểm đón</span>: {ticket['pickUpAddress']}
        </li>
        <li className="list-group-item">
          <span className="fw-bold">Khởi hành lúc</span>{' '}
          {moment(ticket['tripInfo']['departAt']).format('LLL')}
        </li>
        <li className="list-group-item">
          <span className="fw-bold">Mã ghế</span>: {ticket['seatInfo']['code']}
        </li>
        <li className="list-group-item">
          <span className="fw-bold">Khối lượng hành lý</span>:{' '}
          {ticket['luggage']}
        </li>
        <li className="list-group-item">
          <span className="fw-bold">Giá vé:</span>{' '}
          {utils.formatToVND(ticket['seatPrice'])}
        </li>
        {ticket['paymentMethod'] && (
          <li className="list-group-item">
            <span className="fw-bold">Phương thức thanh toán</span>:{' '}
            {ticket['paymentMethod']['name']}
          </li>
        )}
        {ticket['status'] && (
          <li className="list-group-item">
            <span className="fw-bold">Trạng thái</span>:{' '}
            {ticket['status']['name']}
          </li>
        )}
      </ul>
    </div>
  );
};
export default TicketDetails;
