import {Link} from 'react-router-dom';

const RouteItem = ({value}) => {
  return (
    <Link
      to={`/route/${value['id']}`}
      state={{route: value}}
      className="nav-link grid-item border"
    >
      <div className="image-container">
        <h6>{value['name']}</h6>
      </div>
      <div className="mt-3">
        <ul className="nav d-flex flex-column">
          <li className="nav-item">
            <p className="text-primary route-name">
              {value['fromStation']['name']} - {value['toStation']['name']}
            </p>
          </li>
          <li className="nav-item">Công ty: {value['company']['name']}</li>
          <li className="nav-item">
            Giá vé:{' '}
            <span className="text-danger fw-bold">{value['seatPrice']}</span>
          </li>
        </ul>
      </div>
    </Link>
  );
};

export default RouteItem;
