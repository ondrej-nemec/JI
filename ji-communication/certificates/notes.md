# Pro server

## key
winpty openssl genrsa -des3 -out server.private.key 2048
## sign
winpty openssl req -new -key server.private.key -out server.signrequest.csr
winpty openssl x509 -req -days 9999 -in server.signrequest.csr -signkey server.private.key -out server.certificate.pem

## to JKS
winpty openssl pkcs12 -export -in server.certificate.pem -inkey server.private.key -name localhost -out server.certificate.p12
"/c/Program Files/Java/java-1.8.0-openjdk/bin/keytool.exe" -importkeystore -destkeystore jks-server-certificate.jks -srckeystore server.certificate.p12 -srcstoretype PKCS12 -deststoretype pkcs12

## public
winpty openssl req -x509 -new -nodes -key server.private.key -sha256 -days 1825 -out server.public.pem

"/c/Program Files/Java/java-1.8.0-openjdk/bin/keytool.exe" -import -destkeystore jks-client-accept.jks -file server.public.pem -srcstoretype PKCS12 -alias 'localhost'

*********************

(winpty)

openssl genrsa -des3 -out private.key 2048   ## generate private key
// openssl req -x509 -new -nodes -key private.key -sha256 -days 1825 -out public.pem   ## ???

openssl req -new -key private.key -out request.csr  ## sign request
openssl x509 -req -days 9999 -in request.csr -signkey private.key -out server.cert.pem  ## sign certificate

openssl pkcs12 -export -in server.cert.pem -inkey private.key -name localhost -out certificate-PKCS-12.p12
"/c/Program Files/Java/java-1.8.0-openjdk/bin/keytool.exe" -importkeystore -destkeystore server-keystore.jks -srckeystore certificate-PKCS-12.p12 -srcstoretype PKCS12


neni ssl
cert:
	je
	není
trust:
	je
	vše
	není


