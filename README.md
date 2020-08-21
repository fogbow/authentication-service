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

First of all, you need to create a directory named _private_ at `src/main/resources`, it will be holding your private settings (managed clouds, username and password for the clouds, etc.).

#### private/private.key, private/public.key

If you don't know how to create a public/private key, take a look at [this guide](https://docs.oracle.com/cd/E19683-01/806-4078/6jd6cjru7/index.html).

After the keys are created, you must put them in `src/main/resources/private/` folder.

#### private/as.conf

Check out `src/main/resources/templates/as.conf` for a full template.

**Example of some fields:**

```conf
...
public_key_file_path=src/main/resources/private/public.key
private_key_file_path=src/main/resources/private/private.key
system_identity_provider_plugin_class=cloud.fogbow.as.core.systemidp.plugins.openstack.v3.OpenStackSystemIdentityProviderPlugin
...
```

Note that you must complete more than these fields and the template has comments for each field.

### Starting the service

You can skip step 3 if you have added the authentication service and common as modules previously (for example, in the Resource Allocation Service configuration).

1. Start your IDE (IntelliJ, Eclipse, etc);
2. Open the Authentication Service (AS) project;
3. Add/import common as module in the AS project;
4. Run the AS application.

### Accessing the API documentation

You can access the API docs to check the available endpoints for this service, the path is `/swagger-ui.html`

**Example:** if the service is running at <http://localhost:8081> then you should go to <http://localhost:8081/swagger-ui.html>

### Optional tools

- Postman, for REST requests.

## Contributing

For instructions about how to contribute, check out our [contributor's guide](https://github.com/fogbow/authentication-service/blob/master/CONTRIBUTING.md).
