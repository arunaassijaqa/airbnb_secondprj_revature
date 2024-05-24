import { BrowserRouter, Route, Routes } from 'react-router-dom';
import './App.css';
import OwnersPage from './ui/owners-page/index';
import RentersPage from './ui/renters-page';
import Navigation from './ui/nav/Navigation';
import Home from './ui/home-page/index';
import LoginPage from './ui/login-page';
import RegisterPage from './ui/register-page';
import LogoutPage from './ui/logout-page';
import OwnerListings from './ui/owners-page/OwnerListings';
import RenterRequestedListings from './ui/renters-page/RenterRequestedListings';

function App() {
  return (
    <>
      <BrowserRouter>
        <Navigation />
        <Routes>
          <Route path='/' element={<Home />} />

          {/* create mylistings if logged in user is an owner */}
          {(localStorage.hasOwnProperty("role") && localStorage.getItem("role") === "owner") && <Route path="/listings" element={<OwnerListings />} />}

          {/* create mylistings if logged in user is an owner */}
          {(localStorage.hasOwnProperty("role") && localStorage.getItem("role") === "renter") && <Route path="/listings" element={<RenterRequestedListings />} />}
          
          {/* show register if no user logged in */}
          {!localStorage.hasOwnProperty("user") && <Route path='/register' element={<RegisterPage />} />}

          {/* create login path when no user logged in */}
          {!localStorage.hasOwnProperty("user") && <Route path='/login' element={<LoginPage />} />}

          {/* create logout path if user is logged in */}
          {localStorage.hasOwnProperty("user") && <Route path='/logout' element={<LogoutPage />} />}

          <Route path='*' element={<h1>404 Not Found</h1>}></Route>
        </Routes>
      </BrowserRouter>
    </>
  );
}

export default App;
