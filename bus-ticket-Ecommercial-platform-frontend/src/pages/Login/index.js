import {useContext, useState} from 'react';
import './styles.css';
import GoogleButton from 'react-google-button'
import {LoadingContext, AuthenticationContext} from '../../config/context';
import {apis, endpoints} from '../../config/apis';
import { Alert } from 'react-bootstrap';
import {Link, useLocation, useNavigate} from 'react-router-dom';
import * as validator from '../../config/validator';
const Login = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState({
    variant: '',
    show: false,
    message: ''
  })
  const {setLoading} = useContext(LoadingContext);
  const {setUser} = useContext(AuthenticationContext);
  const location = useLocation();
  const {from} = location['state'] || {from: '/'};
  const navigator = useNavigate();
  const validate = () => {
    const msgs = [];
    msgs.push(validator.validateUsername(username));
    msgs.push(validator.validatePassword(password));
    for (let msg of msgs) {
      if (msg) return msg;
    }
  };
  const callLogin = async () => {
    const validateMsg = validate();
    if (validateMsg) {
      setError({
        variant: 'danger',
        show: true,
        message: validateMsg
      })
      return;
    }

    try {
      setLoading('flex');
      const response = await apis
        .post(endpoints['auth']['authenticate'], {
          username: username,
          password: password,
        })
        .catch((errorResponse) => {
          setError({
            variant: 'danger',
            show: true,
            message: errorResponse['response']['data']
          })
        });
      const data = response.data;

      localStorage.setItem('accessToken', data['accessToken']);
      setUser(data['userDetails']);
      navigator(from);
    } catch (error) {
    } finally {
      setLoading('none');
    }
  };
  const handleLoginWithGoogle = async () => {
      const state = new Date().getMilliseconds();
      localStorage.setItem("oauth2State", state)
      try {
        const response = await apis.get(endpoints['auth'].createGoogleLoginUrl(state))
        if (response['data']) {
          const loginUrl = response['data']
          window.location.replace(loginUrl)
        }
      } catch (error) {
        localStorage.removeItem("oauth2State")
        console.log(error)
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
                <h3>Đăng nhập</h3>
                <p className="text-muted">Chào mừng đến OU BUS</p>
              </div>

              <form>
                <div className="mb-3">
                  <label htmlFor="username" className="form-label">
                    Tên tài khoản
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
                <div className="d-flex justify-content-between mb-3">
                  <p className="text-decoration-underline text-muted btn">
                    Quên mật khẩu?
                  </p>
                  <Link
                    to="/register"
                    className="text-decoration-underline text-muted btn"
                  >
                    Bạn chưa có tài khoản?
                  </Link>
                </div>
                <button
                  onClick={callLogin}
                  type="button"
                  className="btn btn-primary btn-lg"
                  style={{width: '100%'}}
                >
                  Đăng nhập
                </button>
                <span className="d-block text-center my-4 text-muted">
                  -- hoặc --
                </span>
                <GoogleButton label="Đăng nhập với Google" onClick={handleLoginWithGoogle}/>
              </form>
            </div>
          </div>
        </div>
      </div>
      <div className="col-md-6 rightImage"></div>
    </div>
  );
};

export default Login;
