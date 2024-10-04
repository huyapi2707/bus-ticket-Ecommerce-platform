import {BsFillRecordCircleFill} from 'react-icons/bs';
import {FaArrowRight} from 'react-icons/fa';
import {IoLocationSharp} from 'react-icons/io5';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';
import vi from 'date-fns/locale/vi';
import {MdDateRange} from 'react-icons/md';
import {useState, useEffect} from 'react';
import './styles.css';
import {apis, endpoints} from '../../config/apis';

const RouteSearch = ({setRouteSearchKw}) => {
  const [sites, setSites] = useState([]);
  const [searchRouteData, setSearchRouteData] = useState({
    startDate: new Date(),
  });
  const fetchSites = async () => {
    const response = await apis.get(endpoints['site']['list']);
    if (response) {
      setSites(response['data']);
    }
  };
  const handleChangeSearchData = (keyName, payload) => {
    setSearchRouteData((searchRouteData) => {
      return {
        ...searchRouteData,
        [keyName]: payload,
      };
    });
  };

  const handleSubmitSearch = () => {
    setRouteSearchKw({
      data: searchRouteData,
      key: new Date().getTime(),
    });
  };
  useEffect(() => {
    fetchSites();
  }, []);
  return (
    <div className="d-flex justify-content-center shadow py-5 rounded search-route px-1 w-75 search-container">
      <div className="d-flex border p-4 justify-content-around">
        <div className="d-flex">
          <div className="mx-3 d-flex align-items-center">
            <BsFillRecordCircleFill size={27} color="#0D6EFD" />
          </div>
          <div className="location">
            <div className="mb-2">
              <label
                className="text-muted lcation-label"
                htmlFor="fromLocation"
              >
                Nơi xuất phát
              </label>
            </div>
            <div>
              <select
                value={searchRouteData['fromStation']}
                onFocus={(event) => {
                  event.target.size = 7;
                }}
                onBlur={(event) => {
                  event.target.size = 1;
                }}
                onChange={(event) => {
                  event.target.size = 1;
                  event.target.blur();
                  handleChangeSearchData('fromStation', event.target.value);
                }}
                className="form-select border-0"
                name="fromLocation"
                id="fromLocation"
              >
                <option>--------------------------</option>
                {sites.map((site, index) => {
                  return (
                    <option key={index} className="p-2 " value={site['id']}>
                      {site['name']}
                    </option>
                  );
                })}
              </select>
            </div>
          </div>
        </div>
        <div className="mx-4 d-flex justify-content-center align-items-center">
          <FaArrowRight size={20} />
        </div>
        <div className="d-flex">
          <div className="mx-3 d-flex align-items-center">
            <IoLocationSharp size={30} color="red" />
          </div>
          <div className="location">
            <div className="mb-2">
              <label className="text-muted lcation-label" htmlFor="toLocation">
                Nơi đến
              </label>
            </div>
            <div>
              <select
                value={searchRouteData['toStation']}
                onFocus={(event) => {
                  event.target.size = 5;
                }}
                onBlur={(event) => {
                  event.target.size = 1;
                }}
                onChange={(event) => {
                  event.target.size = 1;
                  event.target.blur();
                  handleChangeSearchData('toStation', event.target.value);
                }}
                className="form-select  border-0"
                name="toLocation"
                id="toLocation"
              >
                <option>--------------------------</option>
                {sites.map((site, index) => {
                  return (
                    <option key={index} className="p-2" value={site['id']}>
                      {site['name']}
                    </option>
                  );
                })}
              </select>
            </div>
          </div>
        </div>
      </div>
      <div className="border p-4">
        <div className="d-flex">
          <div className="mx-3 d-flex align-items-center">
            <MdDateRange size={27} color="#0D6EFD" />
          </div>
          <div>
            <div className="mb-2">
              <label htmlFor="startDate" className="text-muted lcation-label">
                Ngày đi
              </label>
            </div>
            <div>
              <DatePicker
                dateFormat="EEEE - dd/MM/YYYY"
                id="startDate"
                selected={searchRouteData['startDate']}
                onChange={(date) => handleChangeSearchData('startDate', date)}
                wrapperClassName="location-date-picker"
                locale={vi}
              />
            </div>
          </div>
        </div>
      </div>
      <div className="d-flex align-items-center mx-2">
        <button onClick={handleSubmitSearch} className="btn btn-warning btn-lg">
          Tìm kiếm
        </button>
      </div>
    </div>
  );
};

export default RouteSearch;
