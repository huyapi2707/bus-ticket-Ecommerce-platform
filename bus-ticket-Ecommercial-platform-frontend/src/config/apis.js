import axios from 'axios';

const BASE_URL = 'http://localhost:8080';
const apiVersion = '/api/v1';
const baseEndpoints = {
  auth: '/auth',
  user: '/users',
  company: '/bus_companies',
  route: '/routes',
  trip: '/trips',
  ticket: '/tickets',
  paymentMethod: '/payment_methods',
  site: '/sites',
};

const siteEndpoints = {
  list: `${apiVersion + baseEndpoints['site'] + '/'}`,
};

const paymentMethodEndpoints = {
  list: `${apiVersion + baseEndpoints['paymentMethod'] + '/'}`,
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
  loginWithGoogle: `${apiVersion + baseEndpoints['auth'] + '/oauth2/google'}`,
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
  paymentMethods: paymentMethodEndpoints,
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
