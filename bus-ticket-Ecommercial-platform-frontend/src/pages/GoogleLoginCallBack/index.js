import { useEffect, useContext } from "react"
import { Navigate, useSearchParams } from "react-router-dom"
import { apis, endpoints } from "../../config/apis"
import { AuthenticationContext } from "../../config/context"
import LoadingPage from "../LoadingPage"
const GoogleLoginCallBack = () => {
    const [searchParams, setSearchParams] = useSearchParams()
    const {user, setUser} = useContext(AuthenticationContext);
    const verifyCode = async () => {
        const code = searchParams.get("code")
        if (!code) return;
        try {
            const response = await apis.get(endpoints['auth'].verifyGoogleLogin(code))
            const data = response['data']
            localStorage.setItem("accessToken", data['accessToken'])
            setUser(data['userDetails'])
        
        } catch (error) {

            console.log(error)
        }
    }
    useEffect(() => {
        try {
            const requestState = searchParams.get("state")
            const localState = localStorage.getItem("oauth2State")
    
            if (requestState === localState) {
                verifyCode()
            }
        } catch (error) {
            console.log(error)
        }
        finally {
            localStorage.removeItem("oauth2State")
        }
    }, [])
    if (user) return <Navigate to="/" />
    else return <LoadingPage/>
}

export default GoogleLoginCallBack