# NCCN 
This app contains following questionnaires: Distress Thermometer, HADSD Questionnaire and 'EORTC QLQ-C30 Quality Of Life'

## Distress Thermometer
The German version of the NCCN Distress-Thermometer Empirical examination of a screening instrument for the detection of psychosocial load for cancer patients.
Check the [example PDF](http://www.uniklinik-duesseldorf.de/fileadmin/Datenpool/einrichtungen/klinik_fuer_gastroenterologie_hepatologie_und_infektiologie_id6/Darmzentrum/Formulare_deutsch/distressthermometer.de.pdf)

# Elasticsearch
1. Documentation [Version 5.1](https://www.elastic.co/guide/en/elasticsearch/reference/5.1/index.html)

2. location:
``` bash 
cd /software/elasticsearch-5.1.2/bin
``` 

3. Create Index 
```curl
curl -XPUT 'http://localhost:9200/questionnaires/' -d '{
    "settings" : {
        "index" : {
            "number_of_shards" : 1, 
            "number_of_replicas" : 0
        }
    }
}'

// now check the indexes created
curl 'localhost:9200/_cat/indices?v'
```
