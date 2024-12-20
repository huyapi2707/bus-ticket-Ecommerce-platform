import './styles.css';
import '../Login/styles.css';
import {useContext, useState} from 'react';
import * as validator from '../../config/validator';
import {LoadingContext, AuthenticationContext} from '../../config/context';
import {apis, endpoints} from '../../config/apis';
import {Link, useNavigate} from 'react-router-dom';
import { Alert } from 'react-bootstrap';
const Register = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [rePassword, setRePassword] = useState('');
  const [email, setEmail] = useState('');
  const [isCompanyManager, setIsCompanyManager] = useState(false);
  const {setLoading} = useContext(LoadingContext);
  const {setUser} = useContext(AuthenticationContext);
  const navigator = useNavigate();
  const [error, setError] = useState({
    variant: '',
    show: false,
    message: ''
  })
  const validate = () => {
    if (password !== rePassword) {
      return 'Mật khẩu không khớp';
    }
    const msgs = [];
    msgs.push(validator.validateEmail(email));
    msgs.push(validator.validateUsername(username));
    msgs.push(validator.validatePassword(password));
    for (let msg of msgs) {
      if (msg) return msg;
    }
  };
  const callRegister = async () => {
    const validateMsg = validate();

    if (validateMsg) {
      setError({
        variant: 'danger',
        show: true,
        message: validateMsg
      })
      return;
    } else {
      try {
        setLoading('flex');
        const response = await apis
          .post(endpoints['auth']['register'], {
            username: username,
            password: password,
            email: email,
            role: isCompanyManager ? 'COMPANY_MANAGER' : 'CUSTOMER',
          })
          .catch((errorResponse) => {
            setError({
              variant: 'danger',
              show: true,
              message: errorResponse['response']['data']
            })
          });
        if (response.status === 201) {
          localStorage.setItem('accessToken', response.data.accessToken);
          setUser(response.data['userDetails']);
          navigator('/');
        }
      } catch (error) {
        console.log(error);
      } finally {
        setLoading('none');
      }
    }
  };
  return (
    <div className="row" style={{height: '100vh'}}>
      <div className="col-md-6">
        <div className="container" style={{height: '100%'}}>
          <div
            className="row align-items-center justify-content-center"
            style={{height: '100%'}}
          >
            <div className="col-md-7">
              <Alert variant={error['variant']} show={error['show']} onClose={() => setError({show: false})} dismissible>
                <p>{error['message']}</p>
              </Alert>
              <div>
                <h3>Đăng ký</h3>
                <p className="text-muted">Chào mừng đến với OU BUS</p>
              </div>

              <form>
                <div className="mb-3">
                  <label htmlFor="username" className="form-label">
                    Tên người dùng
                  </label>
                  <input
                    className="form-control form-control-lg"
                    id="username"
                    name="username"
                    type="text"
                    aria-describedby="username"
                    value={username}
                    onChange={(e) => setUsername(e.target.value)}
                  ></input>
                </div>
                <div className="mb-3">
                  <label htmlFor="email" className="form-label">
                    Email
                  </label>
                  <input
                    className="form-control form-control-lg"
                    id="email"
                    name="email"
                    type="email"
                    aria-describedby="email"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                  ></input>
                </div>
                <div className="mb-3">
                  <label htmlFor="password" className="form-label">
                    Mật khẩu
                  </label>
                  <input
                    className="form-control form-control-lg"
                    id="password"
                    name="password"
                    type="password"
                    aria-describedby="password"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                  ></input>
                </div>
                <div className="mb-3">
                  <label htmlFor="re-password" className="form-label">
                    Xác nhận mật khẩu
                  </label>
                  <input
                    className="form-control form-control-lg"
                    id="re-password"
                    name="re-password"
                    type="password"
                    aria-describedby="re-password"
                    value={rePassword}
                    onChange={(e) => setRePassword(e.target.value)}
                  ></input>
                </div>
                <div className="form-check mb-5">
                  <input
                    className="form-check-input"
                    type="checkbox"
                    checked={isCompanyManager}
                    onChange={(e) => {
                      setIsCompanyManager(
                        (isCompanyManager) => !isCompanyManager,
                      );
                    }}
                    id="isCompanyManager"
                  />
                  <label
                    className="form-check-label"
                    htmlFor="isCompanyManager"
                  >
                    Bạn muốn trở thành đối tác?
                  </label>
                  <p className="text-muted" style={{fontSize: '0.9rem'}}>
                    Bạn sẽ đăng ký tài khoản với vai trò quản lý công ty
                  </p>
                </div>
                <button
                  onClick={callRegister}
                  type="button"
                  className="btn btn-primary btn-lg"
                  style={{width: '100%'}}
                >
                  Đăng ký
                </button>
                <Link
                  to="/login"
                  className="d-block text-center my-4 text-muted btn"
                >
                  Bạn đã có tài khoản?
                </Link>
              </form>
            </div>
          </div>
        </div>
      </div>
      <div className="col-md-6 rightImage"></div>
    </div>
  );
};
export default Register;
