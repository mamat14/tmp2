import {makeCachingClient, StockPriceClient} from '../src/stockPriceClient'

describe('cached client', () => {
    const countingMock = jest.fn()

    const clientMock : StockPriceClient = {
        getPrice : (symbol, restParams) => {
            countingMock()
            return Promise.resolve({price : '1234'})
        }
    }

    const cachingClient = makeCachingClient(clientMock)

    afterEach(() => {
        countingMock.mockClear()
    })

    it('should have called client only one time after ' + 
        'two same calls with symbol parameter',async () => {
        const symbol = 'SOME SYMBOL'
        await cachingClient.getPrice(symbol)
        await cachingClient.getPrice(symbol)

        expect(countingMock).toBeCalledTimes(1)
    })

    it('should have called client two time after two different calls',async () => {
        await cachingClient.getPrice('HELLO')
        await cachingClient.getPrice('WORLD')

        expect(countingMock).toBeCalledTimes(2)
    })
})