export type Price = {
    price : String
}

export enum FUNCTION {
    GLOBAL_QUOTE = 'GLOBAL_QUOTE'
}

export type RequestParams = {
    function : FUNCTION,
    symbol ?: string
}

export type StringToString = {
    [name : string] : string
}

export type GlobalQuoteParams = {symbol : string} & RequestParams

export type RequestMakerResponse = {data : any}
export type RequestMaker = (params : RequestParams) => Promise<RequestMakerResponse>
