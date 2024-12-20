import {useContext, useEffect, useState} from 'react';
import './styles.css';
import {AuthenticationContext, LoadingContext} from '../../config/context';
import {Link} from 'react-router-dom';
import {apis, endpoints} from '../../config/apis';

const Navbar = () => {
  const {user, setUser} = useContext(AuthenticationContext);
  const {setLoading} = useContext(LoadingContext);
  const [companies, setCompanies] = useState([]);

  const fetchCompanies = async () => {
    try {
      setLoading('flex');
      const response = await apis.get(endpoints.company['list']);
      if (response) {
        setCompanies(response.data);
      }
    } catch (ex) {
      console.error(ex);
    } finally {
      setLoading('none');
    }
  };

  useEffect(() => {
    fetchCompanies();
  }, []);

  return (
    <nav className="container mt-2 shadow-sm">
      <div className="row">
        <div className="col-md-5 d-flex align-middle">
          <Link
            className="text-decoration-none text-reset d-flex align-items-center"
            to="/"
          >
            <div className="logo">
              <img src="/images/logo.png" alt="logo" />
            </div>
            <p className="fs-4 fw-bold">DỊCH VỤ VÉ XE</p>
          </Link>
        </div>
        <div className="col-md-4 py-5">
          <ul className="navbar-nav d-flex flex-row justify-content-evenly py-2">
            <li className="nav-item">
              <Link className="nav-link fs-5 text-uppercase">Công ty</Link>
              <ul className="navbar-nav ul-child ul-child-company">
                {companies.map((c) => {
                  return (
                    <li key={c.id} className="nav-item">
                      <Link
                        to={`/company/${c['id']}`}
                        className="nav-link fs-6"
                      >
                        {c.name}
                      </Link>
                    </li>
                  );
                })}
              </ul>
            </li>
            {user && user.role === 'CUSTOMER' && (
              <li className="nav-item">
                <Link
                  to="/regist_company"
                  className="nav-link fs-5 text-uppercase"
                >
                  Tham gia
                </Link>
              </li>
            )}
            <li className="nav-item">
              <Link className="nav-link fs-5 text-uppercase" to="#">
                Giới thiệu
              </Link>
            </li>
          </ul>
        </div>
        <div className="col-md-3 d-flex py-5">
          {user ? (
            <>
              <div className="nav-item">
                <div className="d-flex align-items-center">
                  <img
                    className="w-25 h-25 rounded-circle border mx-3"
                    src={user.avatar}
                    alt="avatar"
                  />
                  <p className="m-0">{user.username}</p>
                </div>
                <ul className="navbar-nav ul-child-user">
                  <li>
                    <Link to={'/profile'} className="nav-link">
                      Thông tin của bạn
                    </Link>
                  </li>
                  <li>
                    <Link to={'/ticket'} className="nav-link">
                      Vé xe đã mua
                    </Link>
                  </li>
                  {user['role'] === 'COMPANY_MANAGER' && (
                    <li>
                      <Link className="nav-link" to="/manage_company">
                        Quản lý công ty
                      </Link>
                    </li>
                  )}
                  <li>
                    <button
                      onClick={() => {
                        localStorage.removeItem('accessToken');
                        setUser(null);
                      }}
                      className="btn btn-danger"
                    >
                      Đăng xuất
                    </button>
                  </li>
                </ul>
              </div>
            </>
          ) : (
            <>
              <div className="me-5 w-50">
                <Link
                  to="/login"
                  type="button"
                  className="btn btn-primary w-100"
                >
                  Đăng nhập
                </Link>
              </div>
              <div className="w-50">
                <Link
                  to="/register"
                  type="button"
                  className="btn btn-light w-100"
                >
                  Đăng ký
                </Link>
              </div>
            </>
          )}
        </div>
      </div>
    </nav>
  );
};

export default Navbar;
