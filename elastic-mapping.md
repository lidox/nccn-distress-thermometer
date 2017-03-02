````JSON
PUT questionnaire-app 
{
  "mappings": {
    "scores": { 
      "_all":       { "enabled": false  }, 
      "properties": { 
        "user-name":    { "type": "text"  }, 
        "creation-date":  {
          "type":   "date", 
          "format": "yyyy-MM-dd HH:mm:ss.SSS||yyyy-MM-dd||epoch_millis"
        },
        "DT-distress-score":     { "type": "integer"}, 
        "DT-has-distress":     { "type": "boolean"}, 
        "DT-update-date":  {
          "type":   "date", 
          "format": "yyyy-MM-dd HH:mm:ss.SSS||yyyy-MM-dd||epoch_millis"
        },
        "HADS-D-depression-score":     { "type": "integer"}, 
        "HADS-D-anxiety-score":     { "type": "integer"}, 
        "HADS-D-has-depression":     { "type": "boolean"}, 
        "HADS-D-has-anxiety":     { "type": "boolean"}, 
        "HADS-D-update-date":     {
          "type":   "date", 
          "format": "yyyy-MM-dd HH:mm:ss.SSS||yyyy-MM-dd||epoch_millis"
        },
        "BN20-update-date":     {
          "type":   "date", 
          "format": "yyyy-MM-dd HH:mm:ss.SSS||yyyy-MM-dd||epoch_millis"
        },
        "BN20-bladder-control":     { "type": "double"}, 
        "BN20-weakness-of-legs":     { "type": "double"}, 
        "BN20-itchy-skin":     { "type": "double"}, 
        "BN20-hair-loss":     { "type": "double"}, 
        "BN20-drowsiness":     { "type": "double"}, 
        "BN20-seizures":     { "type": "double"}, 
        "BN20-headaches":     { "type": "double"}, 
        "BN20-communication-deficit":     { "type": "double"}, 
        "BN20-motor-dysfunction":     { "type": "double"}, 
        "BN20-visual-disorder":     { "type": "double"}, 
        "BN20-future-uncertainty":     { "type": "double"}, 
        "QLQC30-update-date":     {
          "type":   "date", 
          "format": "yyyy-MM-dd HH:mm:ss.SSS||yyyy-MM-dd||epoch_millis"
        },
        "QLQC30-financial-difficulties":     { "type": "double"}, 
        "QLQC30-diarrhoea":     { "type": "double"}, 
        "QLQC30-constipation":     { "type": "double"}, 
        "QLQC30-appetite-loss":     { "type": "double"}, 
        "QLQC30-insomnia":     { "type": "double"}, 
        "QLQC30-dyspnoea":     { "type": "double"}, 
        "QLQC30-pain":     { "type": "double"}, 
        "QLQC30-nausea-and-vomiting":     { "type": "double"}, 
        "QLQC30-fatigue":     { "type": "double"}, 
        "QLQC30-social-functioning":     { "type": "double"}, 
        "QLQC30-cognitive-functioning":     { "type": "double"}, 
        "QLQC30-emotional-functioning":     { "type": "double"}, 
        "QLQC30-role-functioning":     { "type": "double"}, 
        "QLQC30-physical-functioning":     { "type": "double"}, 
        "QLQC30-global-health-status":     { "type": "double"}
      }
    }
  }
}
````
