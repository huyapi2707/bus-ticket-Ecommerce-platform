import {Link} from 'react-router-dom';

const CompanyItem = ({value}) => {
  return (
    <Link to={`/company/${value['id']}`} className="nav-link grid-item border">
      <div className="image-container">
        <img src={value['avatar']}></img>
      </div>
      <div className="mt-3">
        <h6>{value['name']}</h6>
      </div>
    </Link>
  );
};

export default CompanyItem;
