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

# Kibana
![kibana](https://cloud.githubusercontent.com/assets/7879175/23508805/a22f0020-ff53-11e6-994d-7c4de58d3603.JPG)


# Quality of life papers
1. Veränderung der Lebensqualität bei Patienten mit
Prostatakarzinom nach endoskopischer extraperitonealer
radikaler Prostatektomie (EERPE): http://www.diss.fu-berlin.de/diss/servlets/MCRFileNodeServlet/FUDISS_derivate_000000013685/diss_f.jurke.pdf

2. The interpretation of scores from the EORTC quality
of life questionnaire QLQ-C30 
https://www.researchgate.net/profile/Madeleine_King2/publication/14215310_The_interpretation_of_scores_from_the_EORTC_quality_of_life_questionnaire_QLQ-C30/links/54f0e97a0cf24eb87941628e.pdf

3. Interpreting the Significance of Changes in Health-Related
Quality-of-Life Scores
https://www.researchgate.net/profile/Madeleine_King2/publication/14215310_The_interpretation_of_scores_from_the_EORTC_quality_of_life_questionnaire_QLQ-C30/links/54f0e97a0cf24eb87941628e.pdf

