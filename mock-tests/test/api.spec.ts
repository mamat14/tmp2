/**
 * @jest-environment node
 */

import {makeApp} from '../src/app'
import request from 'supertest'
import {StockPriceClient} from '../src/stockPriceClient'

describe('RESTlike API v1', () => {

    const knownStockSymbol = Date.now().toString()
    const unknownStockSymbol = 'UNKNOWN' + Date.now().toString()

    const clientMock : StockPriceClient = {
        getPrice : async (symbol, restParams) => {
            if(symbol === knownStockSymbol) {
                return {price: '1234'}
            } else {
                throw new Error()
            }
        }
    }

    const mockedApp = makeApp(clientMock)


    describe('/api/v1/prices', () => {
        it('should have 200 status', async () => {
            let res = await request(mockedApp)
                .get('/api/v1/prices')
                .query({company : knownStockSymbol})

            expect(res.status).toBe(200)
        })
    
        it('should return price', async () => {
            let res = await request(mockedApp)
                .get('/api/v1/prices')
                .query({company : knownStockSymbol})

            expect(res.body).toHaveProperty('price')
        })

        it('should return price as string', async () => {
            let res = await request(mockedApp)
                .get('/api/v1/prices')
                .query({company : knownStockSymbol})
                
            expect(res.body).toHaveProperty('price', expect.any(String))
        })

        it('should return 404 when queried with unknown symbol', async () => {
            let res = await request(mockedApp)
                .get('/api/v1/prices')
                .query({company : unknownStockSymbol})

            expect(res.status).toBe(404)
        })

        it('should return 400 when queried without params', async () => {
            let res = await request(mockedApp)
                .get('/api/v1/prices')

            expect(res.status).toBe(400)
        })
    })
})