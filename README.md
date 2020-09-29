# gatekeeper-sparrow
Gatekeeper SPARROW POC implementation

## Application Configuration Properties

## Example
Gatekeeper Sparrow is completely in-memory application now. Any restart causes all data loss

 1. Setup DSP bidding model
```
POST <gatekeeper>/sparrow/api/v1/dsp/bidding-model?dspId=100
Body:
{
	"biddingModel": [
		{
			"interest": "cycling",
			"bidding": {
				"price": 3.21,
    			"ad": {
    				"creativeUrl": "https://my-ad.com/ad/1234.js",
    				"advertiser": "shoes.com"
    			},
    			"domains": [ "localhost", "bar.com" ]
			}
		}
	]
}
```

 2. Optional. Setup SSP inventory rules
```
POST <gatekeeper>:8080/sparrow/api/v1/ssp/inventory-rules?sspId=100
{
	"inventoryRules": [
		{
	    	"rule": {
	    	"zoneId": 4321,
	    	"dsps": [ "tdd" ]
	    	},
	    	"block": true
		},
		{
    		"rule": {
    			"zoneId": 4321,
    			"advertisers": [ "shoes.com" ]
    		},
    		"floor": 1
		}
	]
}
```

 3. Trigger ssp request to gatekeeper. You should either get empty or meaningful bid response if step 4 is executed within timeout 
```
POST <gatekeeper>:8080/sparrow/api/v1/ssp/interest-group-bid-request?sspId=100
Body:
{
	"id": "bi-request-id",
	"imp": {
		"id": "impression-id",
		"banner": {
			"w": 100,
			"h": 100,
			"pos": 0,
			"topframe": 0
		}
	}
}
```

 4. Trigger ad request within expected timeout. Currently set to 10 seconds
```
GET <gatekeeper>:8080/sparrow/api/v1/browser/interest-group-ad-request?impressionId=impression-id&contextual=localhost&interestGroup=cycling
```
