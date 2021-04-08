# JI - Client/Server Communication

[![](https://jitpack.io/v/ondrej-nemec/javainit.svg)](https://jitpack.io/#ondrej-nemec/javainit)
[![MIT License](http://img.shields.io/badge/license-MIT-green.svg) ](https://github.com/ondrej-nemec/javainit/blob/master/LICENSE)

Provide server/client communication (secured or not secured) using java sockets. Implements web server too. 

* [Download](#include-in-your-project)
* [Create Server](#create-server)
	* [Servant](#servant)
	* [HTTP Server](#http-server)
* [Create Client](#create-client)
	* [HTTP Client](#http-client)
* [Secured connection](#secured-connection)
	* [Server credentials](#server-credentials)
	* [Client credentials](#client-credentials)
	* [Generate self-signed certificate](#generate-self-signed-certificate)

## Include in your project

JI uses for publication <a href="https://jitpack.io/">JitPack</a>. It allows you to include this project by using Gradle or Maven.

### Include using Gradle

Add this line to repositories section
```gradle
maven { url 'https://jitpack.io' }
```
And this line to dependencies
```gradle
implementation 'com.github.ondrej-nemec:javainit:ji-communication:Tag'
```

## Create Server

For waiting on client, there is `Server` class. This class bind port you specify. After calling method `start` starts waiting on client and ends after `end` method. Behaviour of server after client is connected, is specified in `Servant` interface (see [Servant](#servant)). This interface you have to implement.

`Server` constructor parameters:

* `int port` - port where `Server` will listen
* `int threadPool` - how much clients (= threads) can serve at once
* `long readTimeout` - read timeout
* `Servant servant` - this you have to implement
* `Optional<ServerSecuredCredentials> config` - if empty not secured connection used, otherwise secured. See [Server credentials](#server-credentials)
* `String charset` - charset of connection
* `Logger logger` - logger

### Servant

Every time client connect to server, `Server` call `serve` method on `Servant` and pass `Socket` and charset as parameters. After this everything is up to you. From `Socket` you can get for example input and output streams and IP address of client. The connection is open until you read from or write to one of buffers/streams. 

```
try (BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), charset));
           	 BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), charset));
           	 BufferedInputStream is = new BufferedInputStream(socket.getInputStream());
           	 BufferedOutputStream os = new BufferedOutputStream(socket.getOutputStream());) {
      String IPaddress = socket.getInetAddress().toString();
      // ... your logic
 }
```

### HTTP Server

JI Communication provide one implementation of `Servant` -> `RestApiServer`. For using this, create `Server` with factory method `Server.createWebServer`. Parameters:

* `int port` - port where `Server` will listen
* `int threadPool` - how much clients (= threads) can serve at once
* `long readTimeout` - read timeout
* `RestApiResponseFactory` - this you have to implement, see (RestApiServerResposeFactory)(#RestApiServerResposeFactory)
* `Optional<ServerSecuredCredentials> config` - if empty not secured connection used, otherwise secured. See [Server credentials](#server-credentials)
* `int maxUploadFileSize` - maximal allowed uploaded file size in bytes
* `Optional<List<String>> allowedFileContentTypes` - which file types can be uploaded, `Optional.empty` means all files, `Optional` of empty `List` means no files.
* `String charset` - charset of connection
* `Logger logger` - logger

#### RestApiServerResposeFactory

This interface provide you information about request (URL, headers, [parameters](#request-parameters), IP,...). You have to return `RestApiResponse` ([see](#restapiresponse)).

#### RestApiResponse

`RestApiResponse` instance can be created with two factory methods. Both need `StatusCode` - status of your response, `List<String>` response headers (can be empty `List`) and `ThrowingConsumer` (works like normal one, but allow throws `Exception`) that provide you `Writter`/`Stream`.

Fist is for simple text, like HTML:

```
RestApiResponse response = RestApiResponse.textResponse(StatusCode statusCode, List<String> header,
			ThrowingConsumer<BufferedWriter, IOException> textContent)
```

The second one is for binary response, f. e. for pictures:

```
RestApiResponse response = RestApiResponse.binaryResponse(
			StatusCode statusCode, List<String> header,
			ThrowingConsumer<BufferedOutputStream, IOException> binaryContent);
```

#### Request Parameters

Most of values are stored as String (although you can get value as f.e. integer) except:

* `List<?>` - If inputs names look like: `input-name[]`
* `Map<?>` - If inputs names look like: `input-name[first]`, `input-name[second]`
* `UploadedFile` - If input is file. During upload, JI Communication upload file bytes to memory (but if bytes length overflow allowed size, Exception is thrown). `UploadedFile` provide you *Name of file*, *Type of file* and *Content*. The content you can get in `ByteArrayOutputStream` or directly save with `save` method on given path.

**You can have List in Map. The opposite way is not working.**

## Create Client

For connecting to some server, JI Communication provide `SpeakerClient`. Constructor parameters:

* `String ip` - server IP address
* `int port` - port
* `int connectionTimeout` - connection timeout
* `int readTimeout` - read timeout
* `Optional<ClientSecuredCredentials>` - if `Optional.empty` then connection will be unsecured. See [Client credentials](#client-credentials)
* `String charset` - charset
* `Logger logger`

After that you can `connect` or `disconnect`. For communication with server - between this two methods - use `communicate` method. This method required `ThrowingBiConsumer` that provide you `BufferedReader` and `BufferedWriter`. You can read from one and write to other one.

### HTTP Client

If you wish make Rest API request on server, use `RestApiClient`. Constructor parameters:

* `String serverUrl` - server URL or IP, f.e. `http://example.com` or `http://127.0.0.1:12`
* `Optional<ClientSecuredCredentials>` - if `Optional.empty` then connection will be unsecured. See [Client credentials](#client-credentials)
* `String charset` - charset
* `Logger logger`

`RestApiClient` has 4 methods: `get`, `post`, `put`, `delete`. All need same parameters: `String uri` - f.e. `/api/some/data`, `Properties header` - request headers and `Properties params` - request parameters.

This four methods return `RestApiResponse`. It contains response code, response message and content.

## Secured connection

### Server credentials

`ServerSecuredCredentials` required path to key store and key store password. In that key store will be saved your server certificate.

If you wish use user certificates (for connection to your server, user need certificate) fill client trust store and client trust store password. Otherwise let it `Optional.empty`.

### Client credentials

Well, sorry. This needs refactoring. So please use `Optional.empty`.

### Generate self-signed certificate

Before you need `keytool` program. Is in `java-path/bin/keytool`.

```
openssl genrsa -des3 -out private.key 2048   ## generate private key

openssl req -new -key private.key -out request.csr  ## sign request
openssl x509 -req -days 9999 -in request.csr -signkey private.key -out server.cert.pem  ## sign certificate

openssl pkcs12 -export -in server.cert.pem -inkey private.key -name localhost -out certificate-PKCS-12.p12
--path-to-keytool-program-- -importkeystore -destkeystore server-keystore.jks -srckeystore certificate-PKCS-12.p12 -srcstoretype PKCS12
```

