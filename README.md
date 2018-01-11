# NCCN 
This app contains following questionnaires: Distress Thermometer, HADSD Questionnaire and 'EORTC QLQ-C30 Quality Of Life'

## Distress Thermometer
The German version of the NCCN Distress-Thermometer Empirical examination of a screening instrument for the detection of psychosocial load for cancer patients.
Check the [example PDF](http://www.uniklinik-duesseldorf.de/fileadmin/Datenpool/einrichtungen/klinik_fuer_gastroenterologie_hepatologie_und_infektiologie_id6/Darmzentrum/Formulare_deutsch/distressthermometer.de.pdf)

## UI Demo

<table sytle="border: 0px;">
<tr>
<td><img width="200px" src="https://user-images.githubusercontent.com/7879175/34819122-329324f0-f6bd-11e7-926e-c0bede40aab0.png" /></td>
<td><img width="200px" src="https://user-images.githubusercontent.com/7879175/34819123-32af120a-f6bd-11e7-9778-2b02955349a2.png" /></td>
<td><img width="200px" src="https://user-images.githubusercontent.com/7879175/34819120-325a6a8e-f6bd-11e7-84bc-24166161e264.png" /></td>
<td><img width="200px" src="https://user-images.githubusercontent.com/7879175/34819118-3239c388-f6bd-11e7-81c0-c06de2ad98d8.png" /></td>
<td><img width="200px" src="https://user-images.githubusercontent.com/7879175/34819121-32742a46-f6bd-11e7-8041-c5787b1f5e16.png" /></td>
</tr>

</table>

## Download APK
* [APK (release 2.2)](https://github.com/lidox/nccn-distress-thermometer/files/1622230/app-release-2-2.zip)
* [APK (release 2.3)](https://github.com/lidox/nccn-distress-thermometer/files/1622453/app-release-2-3.zip)

## Elasticsearch
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

## Kibana
![kibana](https://cloud.githubusercontent.com/assets/7879175/23508805/a22f0020-ff53-11e6-994d-7c4de58d3603.JPG)

