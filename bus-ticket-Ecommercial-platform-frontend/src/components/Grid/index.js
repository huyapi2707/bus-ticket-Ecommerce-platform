import {useContext, useEffect, useRef, useState} from 'react';
import './styles.css';
import {apis, endpoints} from '../../config/apis';
import {LoadingContext} from '../../config/context';
import CompanyItem from './CompanyItem';
import RouteItem from './RouteItem';
import RouteSearch from '../RouteSearch';
const Grid = ({title, breadcrumb, dataEndpoint}) => {
  const [page, setPage] = useState(1);
  const [data, setData] = useState([]);
  const {setLoading} = useContext(LoadingContext);
  const [pageTotal, setPageTotal] = useState(0);
  const [kw, setKw] = useState('');
  const isKwChanged = useRef(false);
  const isSearchKwChanged = useRef(false);
  const [routeSearchKw, setRouteSearchKw] = useState({});
  const handleChangeKw = (value) => {
    setKw(value);
    isKwChanged.current = true;
  };
  const fetchData = async () => {
    try {
      setLoading('flex');
      let requestUrl = null;

      if (
        title === 'Tuyến xe' &&
        routeSearchKw != {} &&
        routeSearchKw['data'] &&
        Object.keys(routeSearchKw['data']).length > 0
      ) {
        requestUrl = `${endpoints['route']['search']}?page=${page}`;
        Object.keys(routeSearchKw['data']).map((key) => {
          if (routeSearchKw['data'][key] && routeSearchKw['data'][key] > 0) {
            if (key === 'startDate') {
              requestUrl += `&${key}=${routeSearchKw['data'][key].getTime()}`;
            } else {
              requestUrl += `&${key}=${routeSearchKw['data'][key]}`;
            }
          }
        });
      } else {
        requestUrl = `${dataEndpoint}?page=${page}`;
        if (kw !== '') {
          requestUrl += `&name=${kw}`;
        }
      }

      const response = await apis.get(requestUrl);

      if (response) {
        setData(response['data']['results']);
        if (pageTotal !== response['data']['totalPage']) {
          setPageTotal(response['data']['totalPage']);
        }
      }
    } catch (ex) {
      console.error(ex);
    } finally {
      setLoading('none');
    }
  };

  useEffect(() => {
    if (isSearchKwChanged.current) {
      setKw('');
      setPage(1);
    } else if (isKwChanged.current && page === 1) {
      isKwChanged.current = false;
      fetchData();
    } else if (isKwChanged.current && page !== 1) {
      setPage(1);
      isKwChanged.current = false;
    } else {
      fetchData();
    }
  }, [page, kw, routeSearchKw['key']]);

  return (
    <div className="container-fluid px-5 mt-5 border-bottom">
      <nav
        aria-label="breadcrumb"
        className="row d-flex justify-content-between px-5"
      >
        <div className="col-md-6">
          <h2 className="px-3 fw-bolder">{title}</h2>
          <ol className="breadcrumb px-3">
            {breadcrumb.map((b, index) => {
              return (
                <li key={index} className="breadcrumb-item">
                  {b}
                </li>
              );
            })}
            <li className="breadcrumb-item active">{page}</li>
          </ol>
        </div>
        <div className="col-3 d-flex align-items-center">
          {' '}
          <form className="w-100">
            <input
              className="form-control me-2"
              type="search"
              value={kw}
              onChange={(e) => handleChangeKw(e.target.value)}
              placeholder="Tìm kiếm tên"
              aria-label="Tìm kiếm tên"
            />
          </form>
        </div>
        {title === 'Tuyến xe' && (
          <RouteSearch setRouteSearchKw={setRouteSearchKw} />
        )}
      </nav>
      <div className="row ">
        <div className="grid-data">
          {data.map((value) => {
            if (title === 'Công ty') {
              return <CompanyItem key={value['id']} value={value} />;
            } else if (title === 'Tuyến xe') {
              return <RouteItem key={value['id']} value={value} />;
            }
          })}
        </div>
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

export default Grid;
