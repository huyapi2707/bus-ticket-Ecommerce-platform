import './styles.css';
import Navbar from '../../components/Navbar';
import Footer from '../../components/Footer';
import {Outlet} from 'react-router-dom';
const Home = () => {
  return (
    <>
      <Navbar />

      <Outlet />

      <Footer />
    </>
  );
};

export default Home;
