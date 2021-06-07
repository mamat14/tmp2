import app from '../src/app'
import request from 'supertest'

describe('RESTlike API v1', () => {
    describe('/api/v1/prices/?company=MSFT', () => {

        const company_ = 'MSFT'
        const unknownCompanySymbol = 'UNKNOWN' + Date.now().toString()

        it('should have 200 status', async () => {
            let res = await request(app)
                .get('/api/v1/prices')
                .query({company : company_})

            expect(res.status).toBe(200)
        })
    
        it('should return price', async () => {
            let res = await request(app)
                .get('/api/v1/prices')
                .query({company : company_})

            expect(res.body).toHaveProperty('price')
        })

        it('should return price as string', async () => {
            let res = await request(app)
                .get('/api/v1/prices')
                .query({company : company_})
                
            expect(res.body).toHaveProperty('price', expect.any(String))
        })

        it('should return 404 when queried with unknown symbol', async () => {
            let res = await request(app)
                .get('/api/v1/prices')
                .query({company : unknownCompanySymbol})

            expect(res.status).toBe(404)
        })

        it('should return 400 when queried without params', async () => {
            let res = await request(app)
                .get('/api/v1/prices')

            expect(res.status).toBe(400)
        })
    })
})