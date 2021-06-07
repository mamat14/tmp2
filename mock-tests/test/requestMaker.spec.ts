import {defaultRequestMaker} from '../src/requestMaker'
import {FUNCTION} from '../src/types'
import nock from 'nock'

describe('defaultRequestMakerv1', () => {
    describe('url that request maker uses', () => {
        afterEach(() => {
            nock.cleanAll()
        })

        afterAll(() => nock.restore())
        it('should add apikey to request params', async () => {
            nock(/.*/)
            .get(/apikey=[^&]*/)
            .reply(200, {})
            
            
            return expect(defaultRequestMaker({function : FUNCTION.GLOBAL_QUOTE}))
                .resolves.toBeDefined()
        })

        it('should have /query path', async () => {
            nock(/.*/)
            .get(/\/query/)
            .reply(200, {})

            return expect(defaultRequestMaker({function : FUNCTION.GLOBAL_QUOTE}))
                .resolves.toBeDefined()

        })

        it('should have https://www.alphavantage.co baseURL', () => {
            nock('https://www.alphavantage.co')
            .get(/.*/)
            .reply(200, {})

            return expect(defaultRequestMaker({function : FUNCTION.GLOBAL_QUOTE}))
                .resolves.toBeDefined()
        })
    })
})