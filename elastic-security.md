## Basic Auth using NGIX
Whenever you create a new server, just place them into your conf.d directory:
````
nano /usr/local/etc/nginx/conf.d/mywebsite.conf
````
you can then put a specific server config into that file like so:
````
server {
  listen *:443 ;
  server_name 134.99.218.19;
 
  access_log /var/log/nginx/search.access.log;
 
  location / {
    auth_basic           "Elastic Search Artur";
    auth_basic_user_file /etc/nginx/conf.d/search.htpasswd;
 
    # Send everything to the Elasticsearch endpoint
    try_files @elasticsearch /dev/null =404;
  }
 
  # Endpoint to pass Elasticsearch queries to
  location @elasticsearch {
    proxy_pass http://134.99.218.19:9200;
    proxy_read_timeout 90;
  }
}
````
