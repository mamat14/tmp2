import {RequestMaker, FUNCTION, Price, StringToString} from './types'
import {defaultRequestMaker} from './requestMaker'
import LRU from 'lru-cache'

export interface StockPriceClient {
    getPrice(symbol: string, restParams ?: any) : Promise<Price>
}

class StockPriceClientImpl implements StockPriceClient{
    constructor(private requestMaker : RequestMaker) {}

    async getPrice(symbol: string, restParams ?: StringToString) : Promise<Price> {
        const baseParams= {
            "function" : FUNCTION.GLOBAL_QUOTE,
            "symbol": symbol,
        }

        const params = Object.assign(baseParams, restParams)
        const res = await this.requestMaker(params)
        return {price : res.data["Global Quote"]["05. price"]}
    }
}

class CachingStockPriceClient implements StockPriceClient {
    private lruCache : LRU<string, Price> = new LRU(50)

    constructor(private client: StockPriceClient) {}

    getPrice(symbol: string, restParams?: any): Promise<Price> {
        let key = JSON.stringify(arguments)
        let cachedRes = this.lruCache.get(key)
        if(cachedRes !== undefined) {
            return Promise.resolve(cachedRes)
        } else {
            return this.client.getPrice(symbol, restParams)
                .then((res) => {
                    this.lruCache.set(key, res)
                    return res
                })
        }
    }
}

export function makeCachingClient(stockPriceClient : StockPriceClient) {
    return new CachingStockPriceClient(stockPriceClient)
}

export function makeClient(requestMaker : RequestMaker) {
    return new StockPriceClientImpl(requestMaker)
}

const defaultClient : StockPriceClient = makeClient(defaultRequestMaker)
export default defaultClient;