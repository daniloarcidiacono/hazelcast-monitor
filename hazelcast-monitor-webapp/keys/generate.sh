#!/bin/bash
rm *.pem

# Generates a self-signed SSL certificate for localhost (DO NOT use in production!)
openssl req -x509 -nodes -subj '/CN=localhost' -days 365000 -newkey rsa:4096 -keyout key.pem -out cert.pem
