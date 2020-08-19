# Authentication Service

The authentication service is responsible for implementing several user authentication mechanisms in a multi-cloud environment, including implementations based on LDAP, Openstack Keystone and Shibboleth. 

It provides a token required for build requests to the REST API of many fogbow services.

## How to use

In this section the installation explanation will be facing a possible contributor. If you are interested in deployment, please take a look at [fogbow-deploy](https://github.com/fogbow/fogbow-deploy).

### Dependencies

- Java 8
- Maven
- [Common module](https://github.com/fogbow/common/), which is a dependency for most fogbow service.

### Installing

First of all, create a directory to organize all fogbow modules/services then clone the required repositories.

```bash
mkdir fogbow && cd fogbow

git clone https://github.com/fogbow/common.git
cd common  
git checkout develop && mvn install -DskipTests

git clone https://github.com/fogbow/authentication-service.git
cd authentication-service
git checkout develop && mvn install -DskipTests
```

### Configuration

This service requires some initial configuration. Most of them will have a template for help you to get started.

First of all, you need to create a directory named *private* at `src/main/resources`, it will be holding your private settings (managed clouds, username and password for the clouds, etc.).

#### private/private.key, private/public.key

:pushpin: Insert here information about public/private key.

#### private/as.conf

Check out `src/main/resources/templates/as.conf` for a file template. Here you need to configurate some required fields. Let's take a look at each of them.

- **xmpp_jid:** :pushpin: Insert here information about this field
- **public_key_file_path:** the path to the public key
- **private_key_file_path:** the path to the private key
- **system_identity_provider_plugin_class:** :pushpin: Insert here information about this field

### Starting the service

You can skip step 3 if you have added the authentication service and common as modules previously (for example, in the Resource Allocation Service configuration).

1. Start your IDE (IntelliJ, Eclipse, etc);
2. Open the Authentication Service (AS) project;
3. Add/import common as module in the AS project;
4. Run the AS application.

### Accessing the API documentation

You can access the API docs to check the available endpoints for this service. If the service is running at localhost:8081 then you should go to <http://localhost:8081/swagger-ui.html>

### Optional tools

- Postman, for REST requests.

## Contributing

For instructions about how to contribute, check out our [contributor's guide](https://github.com/fogbow/authentication-service/blob/master/CONTRIBUTING.md).

## Help

:pushpin: Insert here information about how to get help when needed. Email, discord or any communication channel.
