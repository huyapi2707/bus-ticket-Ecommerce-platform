import {Link, useParams} from 'react-router-dom';
import './styles.css';
import {useContext, useEffect, useState} from 'react';
import * as utils from '../../config/utils';
import {apis, endpoints} from '../../config/apis';
import {CartContext, LoadingContext} from '../../config/context';
import moment from 'moment';
import 'moment/locale/vi';
import {toast} from 'react-toastify';

const RouteInfo = () => {
  let {id} = useParams();
  const [route, setRoute] = useState(null);
  const {setLoading} = useContext(LoadingContext);
  const {cart, cartDispatcher} = useContext(CartContext);
  const [arrSelectedSeats, setArrSelectedSeats] = useState([]);
  const [trips, setTrips] = useState([]);
  const [tripId, setTripId] = useState(null);
  const [seatDetails, setSeatDetails] = useState([]);

  const addToCart = () => {
    if (arrSelectedSeats.length === 0) {
      toast.warning('Hãy chọn ghế bạn muốn', {
        position: 'top-center',
        autoClose: 4000,
        closeOnClick: true,
        pauseOnHover: true,
        draggable: true,
        progress: undefined,
        theme: 'colored',
      });
      return;
    }
    const payload = arrSelectedSeats.map((seat) => {
      return {
        tripId: tripId,
        seatInfo: seat,
      };
    });

    cartDispatcher({
      type: 'ADD_TO_CART',
      payload: payload,
    });
  };

  useEffect(() => {
    if (cart['data'].length === 0) {
      setArrSelectedSeats([]);
    } else if (tripId && cart['data'].length > 0) {
      const arrCart = [];
      cart['data'].forEach((item) => {
        if (item['tripId'] === tripId) {
          arrCart.push(item['seatInfo']);
        }
      });
      setArrSelectedSeats(arrCart);
    }
  }, [cart['key'], tripId]);

  const checkedSeat = (seatId) => {
    return arrSelectedSeats.find((item) => item['id'] === seatId);
  };

  const handleToggleSeat = (event) => {
    const target = {
      id: parseInt(event['target']['value']),
      code: event['target']['id'],
      luggage: 1,
    };

    if (event.target.checked) {
      setArrSelectedSeats((arrSelectedSeats) => [...arrSelectedSeats, target]);
    } else {
      setArrSelectedSeats((arrSelectedSeats) =>
        arrSelectedSeats.filter((item) => item['id'] !== target['id']),
      );
    }
  };

  const setLuggage = (value, index) => {
    const newArr = arrSelectedSeats;
    newArr[index]['luggage'] = value;
    setArrSelectedSeats([...newArr]);
  };

  const fetchAllTrips = async () => {
    setLoading('flex');
    try {
      const response = await apis.get(endpoints.route.trips(id));
      if (response) {
        setTrips(response['data']);
        setTripId(response['data'][0]['id']);
      }
    } catch (ex) {
      console.error(ex);
    } finally {
      setLoading('none');
    }
  };

  const fetchRouteInfo = async () => {
    try {
      setLoading('flex');
      const response = await apis.get(endpoints.route.retrieve(id));
      const data = response['data'];

      setRoute(data);
    } catch (ex) {
      console.error(ex);
    } finally {
      setLoading('none');
    }
  };

  const fetchTripSeatDetails = async () => {
    setLoading('flex');
    try {
      const response = await apis.get(endpoints.trip.seats(tripId));
      const availableSeats = response['data']['availableSeats'];
      const unAvailableSeats = response['data']['unAvailableSeats'];

      let seats = availableSeats.map((a) => {
        a['available'] = true;
        return a;
      });
      seats = seats.concat(
        unAvailableSeats.map((a) => {
          a['available'] = false;
          return a;
        }),
      );
      seats.sort((a, b) => {
        return a['id'] - b['id'];
      });

      setSeatDetails(seats);
    } catch (ex) {
      console.error(ex);
    } finally {
      setLoading('none');
    }
  };

  useEffect(() => {
    fetchRouteInfo();
    fetchAllTrips();
  }, []);

  useEffect(() => {
    if (tripId !== null) {
      fetchTripSeatDetails();
    }
  }, [tripId]);

  return (
    <div className="container py-5">
      <div className="row">
        {route && (
          <div className="col-md-6 p-3">
            <div className="border-bottom mb-4">
              <Link
                className="fs-2 text-decoration-none"
                to={`/company/${route['company']['id']}`}
              >
                {route['company']['name']}
              </Link>
            </div>
            <div>
              <ul className="list-unstyled">
                <li className="mb-2">
                  Mã tuyến: <span className="fw-bold">{route['name']}</span>
                </li>
                <li className="mb-2">
                  Giá vé:{' '}
                  <span className="text-primary">
                    {utils.formatToVND(route['seatPrice'])}
                  </span>
                </li>

                <li className="row mb-2">
                  <div className="col-md-6">
                    <p>
                      Bắt đầu từ: <span>{route['fromStation']['name']}</span>
                    </p>
                    <iframe
                      src={route['fromStation']['mapLocation']}
                      width="300"
                      height="300"
                      allowFullScreen={true}
                      loading="lazy"
                      referrerPolicy="no-referrer-when-downgrade"
                    ></iframe>
                  </div>
                  <div className="col-md-6">
                    <p>
                      Điểm đến: <span>{route['toStation']['name']}</span>
                    </p>
                    <iframe
                      src={route['toStation']['mapLocation']}
                      width="300"
                      height="300"
                      allowFullScreen={true}
                      loading="lazy"
                      referrerPolicy="no-referrer-when-downgrade"
                    ></iframe>
                  </div>
                </li>
              </ul>
            </div>
            <div className="mt-5 d-flex align-items-center">
              <button onClick={addToCart} className="btn btn-primary">
                Thêm vào giỏ vé
              </button>
            </div>
            <div className="mt-5 ">
              <div>
                {arrSelectedSeats.length > 0 && (
                  <p>Khối lượng hàng hóa mang theo</p>
                )}
              </div>
              <div>
                {arrSelectedSeats.map((k, index) => {
                  return (
                    <div key={index} className="my-2 row">
                      <label
                        htmlFor={'luggage_' + k['code']}
                        className="col-sm-2 col-form-label"
                      >
                        Ghế số {k['code']}:
                      </label>
                      <div className="col-sm-3">
                        <input
                          placeholder="kg"
                          type="number"
                          className="form-control"
                          id={'luggage_' + k['code']}
                          value={arrSelectedSeats[index]['luggage']}
                          onChange={(event) =>
                            setLuggage(event.target.value, index)
                          }
                        />
                      </div>
                    </div>
                  );
                })}
              </div>
            </div>
          </div>
        )}
        <div className="col-md-6 p-3">
          <div>
            <h5 className="text-center">Các chuyến</h5>
            <div className="mt-4 border-bottom pb-3 d-flex flex-direction-column">
              {trips.map((trip) => {
                return (
                  <div key={trip['id']} className="form-check my-2">
                    <input
                      className="form-check-input"
                      type="radio"
                      name="tripId"
                      id={trip['id']}
                      onChange={() => {
                        setTripId(trip['id']);
                      }}
                      checked={trip['id'] === tripId}
                    />
                    <label
                      className="form-check-label"
                      htmlFor="flexRadioDefault2"
                    >
                      Khởi hành lúc:{' '}
                      <span className="fw-bold">
                        {moment(trip['departAt']).format('LLL')}
                      </span>
                    </label>
                  </div>
                );
              })}
            </div>
            <div className="mt-4 d-flex align-items-center flex-column">
              <h5>Ghế</h5>
              <div className="mt-3 seat-grid">
                {seatDetails.map((seat) => {
                  return (
                    <div key={seat['id']} className="form-check w-100 p-0">
                      <input
                        value={seat['id']}
                        type="checkbox"
                        className="btn-check"
                        id={seat['code']}
                        autoComplete="off"
                        onChange={(event) => handleToggleSeat(event)}
                        disabled={!seat['available']}
                        checked={checkedSeat(seat['id'])}
                      />
                      <label
                        className={[
                          'btn',
                          seat['available']
                            ? 'btn-outline-primary'
                            : 'btn-danger',
                        ].join(' ')}
                        htmlFor={seat['code']}
                      >
                        {seat['code']}
                      </label>
                    </div>
                  );
                })}
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default RouteInfo;
