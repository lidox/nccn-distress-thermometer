## Install NGIX
See [tutorial!](https://www.digitalocean.com/community/tutorials/how-to-install-nginx-on-ubuntu-14-04-lts)

## Set Up Basic HTTP Authentication With Nginx
See [tutorial!](https://www.digitalocean.com/community/tutorials/how-to-set-up-basic-http-authentication-with-nginx-on-ubuntu-14-04)

## Basic Auth using NGIX
Whenever you create a new server, just place them into your conf.d directory:
````
nano /etc/nginx/sites-available/default
````
you can then put a specific server config into that file like so:
````
server {
  listen *:80 ;
  server_name YOUR_IP;
 
  access_log /var/log/nginx/search.access.log;
     
  # proxy
  location ~ / {
    auth_basic           "Artur Private";
    auth_basic_user_file /etc/nginx/.htpasswd;

    # Send everything to the Elasticsearch endpoint
    proxy_bind 127.0.0.1;
    proxy_pass http://YOUR_IP:YOUR_PORT;
  }

}
````
## HTTPS
See [tutorial!](https://www.digitalocean.com/community/tutorials/how-to-secure-nginx-with-let-s-encrypt-on-debian-8)
````
server {
  listen *:80 ;
  server_name 134.99.218.19;
  root /var/www/html;
  access_log /var/log/nginx/search.access.log;

  listen 443 ssl default_server;
  listen [::]:443 ssl default_server;
  include snippets/ssl-params.conf;

 
  ssl_certificate /etc/letsencrypt/live/ukdapp.tk/fullchain.pem;
  ssl_certificate_key /etc/letsencrypt/live/ukdapp.tk/privkey.pem;
  
  # classical redirect
  location ~ /test {
     return 301 http://127.0.0.1:9200;
  }

  location ~ /.well-known {
     allow all;
     auth_basic off;
   }
   
   
  # proxy to intern ip
  location ~ / {
    auth_basic           "Artur Private";
    auth_basic_user_file /etc/nginx/.htpasswd;

    # Send everything to the Elasticsearch endpoint
    proxy_bind 127.0.0.1;
    proxy_pass http://134.99.218.19:9200;
  }
}
````
