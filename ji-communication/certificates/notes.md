# Pro server
openssl genrsa -des3 -out private.key 2048   ## generate private key
// openssl req -x509 -new -nodes -key private.key -sha256 -days 1825 -out public.pem   ## ???

openssl req -new -key private.key -out request.csr  ## sign request
openssl x509 -req -days 9999 -in request.csr -signkey private.key -out server.cert.pem  ## sign certificate

openssl pkcs12 -export -in server.cert.pem -inkey private.key -name localhost -out certificate-PKCS-12.p12
"/c/Program Files/Java/java-1.8.0-openjdk/bin/keytool.exe" -importkeystore -destkeystore server-keystore.jks -srckeystore certificate-PKCS-12.p12 -srcstoretype PKCS12



