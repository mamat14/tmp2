
import axios, { AxiosRequestConfig } from 'axios'
const config : AxiosRequestConfig = {  
    // `method` is the request method to be used when making the request
    //method: 'get', // default
  
    // `baseURL` will be prepended to `url` unless `url` is absolute.
    // It can be convenient to set `baseURL` for an instance of axios to pass relative URLs
    // to methods of that instance.
    baseURL: 'https://www.alphavantage.co',
  
    // `transformRequest` allows changes to the request data before it is sent to the server
    // This is only applicable for request methods 'PUT', 'POST', 'PATCH' and 'DELETE'
    // The last function in the array must return a string or an instance of Buffer, ArrayBuffer,
    // FormData or Stream
    // You may modify the headers object.
    transformRequest: [function (data, headers) {
      // Do whatever you want to transform the data
      return data;
    }],
  
    // `transformResponse` allows changes to the response data to be made before
    // it is passed to then/catch
    transformResponse: [function (data) {
      // Do whatever you want to transform the data
      return JSON.parse(data)
    }],
  
    // `headers` are custom headers to be sent
    // headers: {'X-Requested-With': 'XMLHttpRequest'},
  
    // `timeout` specifies the number of milliseconds before the request times out.
    // If the request takes longer than `timeout`, the request will be aborted.
    timeout: 1000, // default is `0` (no timeout)
  
    // `withCredentials` indicates whether or not cross-site Access-Control requests
    // should be made using credentials
    withCredentials: false, // default
  
    // `responseType` indicates the type of data that the server will respond with
    // options are: 'arraybuffer', 'document', 'json', 'text', 'stream'
    //   browser only: 'blob'
    responseType: 'json', // default

    validateStatus: function (status : any) {
      return status >= 200 && status < 300; // default
    },
  
    // `maxRedirects` defines the maximum number of redirects to follow in node.js.
    // If set to 0, no redirects will be followed.
    maxRedirects: 5, // default
  }

const stockAxios = axios.create(config)

const apiKeysPool = 
['V7B7ADD30ZM4R787',
  'E07BQ5EKJNYH61P6',
  'NDRPQOGC8L8QUJGF',
  '5SPCBQWODUSF2RZH',
  'BPJ7ST4YUFPJWCW2',
  'NPS7ZSTP3VXUMRH0']

const apiKeysCount = apiKeysPool.length
let curApiKeyIndex = Math.floor((Math.random() * 10)) % 5

function getApiKey() : string {
  curApiKeyIndex = (curApiKeyIndex + 1) % apiKeysCount
  return apiKeysPool[curApiKeyIndex]
}

stockAxios.interceptors.request.use(function (config) {
  // Do something before request is sent
  const cfg = config
  const params = cfg.params
  params["apikey"] = getApiKey()
  config.params = params  
  return config;
});

stockAxios.defaults.adapter = require('axios/lib/adapters/http')

export default stockAxios