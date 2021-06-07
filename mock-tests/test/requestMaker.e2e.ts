import {defaultRequestMaker} from '../src/requestMaker'
import {FUNCTION, GlobalQuoteParams } from '../src/types'

describe('defaultRequestMakerv1', () => {
    describe('real call with  params : function = GLOBAL_QUOTE, symbol = MSFT', () => {
        const params : GlobalQuoteParams = {
            "function" : FUNCTION.GLOBAL_QUOTE,
            "symbol" : 'MSFT'
        }

        describe('format', () => {
            it('should contain "Global Quote" property', () => {
                return expect(defaultRequestMaker(params).then(res => res.data))
                    .resolves.toHaveProperty('Global Quote')
            })

            it('should contain "Global Quote"."05. price" property with string value', () => {
                return expect(defaultRequestMaker(params).then(res => res.data))
                    .resolves.toHaveProperty(['Global Quote', '05. price'], expect.any(String))
            })
        })
    })
})