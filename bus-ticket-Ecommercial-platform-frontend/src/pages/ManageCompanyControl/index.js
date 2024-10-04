import './styles.css';
import {Link} from 'react-router-dom';
const ManageCompanyControl = () => {
  return (
    <div className="container shadow">
      <div className="row">
        <ul className="list-group list-group-horizontal p-3">
          <li className="list-group-item">
            <div>
              <Link
                className="btn btn-primary"
                to={'/manage_company/chat_support'}
              >
                Hỗ trợ khách hàng
              </Link>
            </div>
          </li>
        </ul>
      </div>
    </div>
  );
};
export default ManageCompanyControl;
