import axios from 'axios';

const BASE_URL = 'https://localhost:8080';
const apiVersion = '/api/v1';
const baseEndpoints = {
  auth: '/auth',
  user: '/users',
  company: '/bus_companies',
  route: '/routes',
  trip: '/trips',
  ticket: '/tickets',
  payment: '/payments',
  site: '/sites',
};

const siteEndpoints = {
  list: `${apiVersion + baseEndpoints['site'] + '/'}`,
};

const paymentEndpoints = {
  list: `${apiVersion + baseEndpoints['payment'] + '/'}`,
  vnPayPaymentResult: (code, userId) => `${apiVersion + baseEndpoints['payment'] + '/VNPAY/result?' + "paymentCode=" + code + "&userId=" + userId}`
};

const ticketEndpoints = {
  cart: `${apiVersion + baseEndpoints['ticket'] + '/cart'}`,
  create: (paymentMethodId) =>
    `${
      apiVersion +
      baseEndpoints['ticket'] +
      '/?paymentMethodId=' +
      paymentMethodId
    }`,
  delete: (id) => `${apiVersion + baseEndpoints['ticket'] + '/' + id}`,
};

const authEndpoints = {
  authenticate: `${apiVersion + baseEndpoints['auth'] + '/authenticate'}`,
  register: `${apiVersion + baseEndpoints['auth'] + '/register'}`,
  createGoogleLoginUrl: (state) => `${apiVersion + baseEndpoints['auth'] + '/oauth2/google?state=' + state}`,
  verifyGoogleLogin: (code) => `${apiVersion + baseEndpoints['auth'] + '/oauth2/google/verify?code=' + code}`
};

const companyEndpoints = {
  list: `${apiVersion + baseEndpoints['company'] + '/'}`,
  retrieve: (id) => `${apiVersion + baseEndpoints['company'] + '/' + id}`,
  routes: (id) =>
    `${apiVersion + baseEndpoints['company'] + '/' + id + '/routes'}`,
};

const userEndpoints = {
  self: `${apiVersion + baseEndpoints['user'] + '/self'}`,
  tickets: (id) =>
    `${apiVersion + baseEndpoints['user'] + '/' + id + '/tickets'}`,
  company: (id) =>
    `${apiVersion + baseEndpoints['user'] + '/' + id + '/managed_company'}`,
};

const routeEndpoints = {
  list: `${apiVersion + baseEndpoints['route'] + '/'}`,
  search: `${apiVersion + baseEndpoints['route'] + '/search'}`,
  retrieve: (id) => `${apiVersion + baseEndpoints['route'] + '/' + id}`,
  trips: (id) => `${apiVersion + baseEndpoints['route'] + '/' + id + '/trips'}`,
};

const tripEndpoints = {
  seats: (id) => `${apiVersion + baseEndpoints['trip'] + '/' + id + '/seats'}`,
};

const endpoints = {
  auth: authEndpoints,
  company: companyEndpoints,
  user: userEndpoints,
  route: routeEndpoints,
  trip: tripEndpoints,
  ticket: ticketEndpoints,
  payment: paymentEndpoints,
  site: siteEndpoints,
};

const apis = axios.create({
  baseURL: BASE_URL,
});

const authenticatedApis = () => {
  const accessToken = localStorage.getItem('accessToken');
  if (accessToken)
    return axios.create({
      baseURL: BASE_URL,
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    });
};

export {apis, authenticatedApis, endpoints};
