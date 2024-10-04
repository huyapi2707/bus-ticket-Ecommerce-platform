import './styles.css';
import Navbar from '../../components/Navbar';
import Footer from '../../components/Footer';
import {Outlet} from 'react-router-dom';
import CartIcon from '../../components/CartIcon';
const Home = () => {
  return (
    <>
      <Navbar />
      <CartIcon />
      <Outlet />

      <Footer />
    </>
  );
};

export default Home;
