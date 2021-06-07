import stockAxios from './configs/stockAxios'

import {RequestParams, RequestMaker} from './types'
import { AxiosResponse } from 'axios'

export const defaultRequestMaker : RequestMaker = 
function callExtenalAPI(params: RequestParams) : Promise<AxiosResponse> {
    return stockAxios.get("/query", {"params" : params})
}