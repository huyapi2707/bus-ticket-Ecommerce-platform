import {useRef, useState} from 'react';
import './styles.css';
const RegistCompany = () => {
  const formAttributes = {
    name: {
      label: 'Tên công ty',
      type: 'text',
    },
    phone: {
      label: 'Số điện thoại',
      type: 'phone',
    },
    email: {
      label: 'Email',
      type: 'email',
    },
    avatar: {
      label: 'Ảnh đại diện',
    },
  };
  const [registData, setRegistData] = useState({
    name: '',
    phone: '',
    email: '',
  });
  const [currentAvart, setCurrentAvatar] = useState(null);
  const avatar = useRef(null);
  const onChangeAvatar = (file) => {
    const newAvatarUrl = URL.createObjectURL(file);
    setCurrentAvatar(newAvatarUrl);
  };
  const onChangeRegistData = (key, payload) => {
    setRegistData((registData) => {
      return {
        ...registData,
        [key]: payload,
      };
    });
  };
  const removeAvatar = () => {
    setCurrentAvatar('');
    avatar.current.value = '';
  };
  const handleRegist = () => {
    const formData = new FormData();
    Object.keys(registData).map((key) => {
      formData.append(key, registData[key]);
    });
    formData.append('avatar', avatar.current.files[0]);
  };
  return (
    <div className="container mt-5">
      <div className="mb-3">
        <h4>Đăng ký công ty</h4>
      </div>
      <div className="row">
        <div className="col-md-5 pe-5">
          {Object.keys(registData).map((key) => {
            return (
              <div key={key} className="form-group mb-3">
                <label htmlFor={key} className="form-label">
                  {formAttributes[key]['label']}
                </label>
                <input
                  onChange={(event) =>
                    onChangeRegistData(key, event.target.value)
                  }
                  type={formAttributes[key]['type']}
                  value={registData[key]}
                  className="form-control"
                  id={key}
                  name={key}
                />
              </div>
            );
          })}
          <div>
            <button onClick={handleRegist} className="btn btn-primary">
              Đăng ký
            </button>
          </div>
        </div>
        <div className="col-md-7 pe-5">
          <div className="mb-3">
            <img
              className="img-thumbnail mx-auto d-block"
              width={300}
              height={300}
              alt="avatar"
              src={currentAvart}
            />
            <div>
              <label htmlFor="avatar">Ảnh đại diện</label>
              <input
                onChange={(event) => onChangeAvatar(event.target.files[0])}
                ref={avatar}
                className="form-control"
                type="file"
                id="avatar"
                name="avatar"
                accept=".png,.jpeg"
              ></input>
            </div>
          </div>
          <div>
            <button onClick={removeAvatar} className="btn btn-danger">
              Xóa ảnh
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};
export default RegistCompany;
