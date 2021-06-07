import express from 'express'
import client, {makeCachingClient, StockPriceClient} from './stockPriceClient'

export function makeApp(client: StockPriceClient) {
   const app = express()

   app.get('/api/v1/prices',async (req, res) => {
      try{
         const company = req.query.company
         if(typeof company === 'undefined') {
            res.status(400).end('No company query param. Use /api/v1/prices?company=XXX syntax.')
         } else {
            res.json(await client.getPrice(company))
         }
      } catch(error) {
         res.status(404).end('Could not find company data.')
      }
   })

   return app
}

const defaultApp = makeApp(makeCachingClient(client))
export default defaultApp