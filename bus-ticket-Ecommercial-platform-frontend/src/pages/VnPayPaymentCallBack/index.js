import './styles.css';
import { useContext, useState, useEffect } from "react"
import { useSearchParams } from "react-router-dom"
import LoadingPage from "../LoadingPage"
import { Link } from "react-router-dom"
import { FaCheck } from 'react-icons/fa';
import { VscError } from 'react-icons/vsc';
import { authenticatedApis, endpoints } from '../../config/apis';
import { AuthenticationContext } from '../../config/context';
const VnPayPaymentCallBack = () => {

    const responseConstant = {
        success: {
            title: "Thành công",
            icon: () => <FaCheck size={180} color="#34A853" />,
            message: "Bạn đã thanh toàn thành công"
        },
        failure: {
            title: "Thất bại",
            icon: () => <VscError color={'#E74033'} size={180} />,
            message: 'Đã xảy ra lỗi trong thanh toán của bạn'
        }
    }

    const [params, setParams] = useSearchParams()

    const {user} = useContext(AuthenticationContext)

    const [state, setState] = useState("LOADING")
    const code = params.get("vnp_TxnRef")

    const fetchResult = async () => {
      
      if (!code) {
            
        setState("failure")
        return;
    }

    try {
        const requestUrl = endpoints['payment'].vnPayPaymentResult(code, user['id'])
        const response = await authenticatedApis().get(requestUrl)
        const data = response['data']
        console.log(response) 
        setState(data ? "success" : "failure")
    }
    catch (e) {
        console.log(e)
    }
    }

    useEffect(() => {
      fetchResult()
      }, [])

    if (state === "LOADING") {
        return <LoadingPage/>
    }

    else {
        return (
            <div className="container d-flex flex-column align-items-center my-5">
            <div className="">
              <h1 className="title text-uppercase fw-bold ">{responseConstant[state]['title']}</h1>
            </div>
            <div className="my-5">{responseConstant[state]['icon']()}</div>
            <div>
              <p className="fs-1">{responseConstant[state]['message']}</p>
            </div>
            <div className="row">
              <Link to={'/'} className="btn btn-primary my-2">
                Trang chủ
              </Link>
              <Link to={'#'} className="btn btn-outline-success my-2">
                Vé xe đã mua
              </Link>
            </div>
          </div>
        )
    }
}

export default VnPayPaymentCallBack