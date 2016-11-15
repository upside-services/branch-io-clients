# branch-io-java-client
Client for Branch.io HTTP API.

Running tests
=============

```
BRANCH_API_KEY='a valid branch api key'; gradle test
```

Integrating Client
==================

Upside primarily uses Dropwizard as a service framework so this example leans on that fact, but the Clients are written without any dependencies on Dropwizard.

Our production configuration YAML:

```
branchIO:
    apiKey: <our key>
    apiSecret: <our secret>
    clientType: REAL   # alternatively, "MOCK", in which case you don't need to specify a Key or Secret
```

Our configuration POJO:

```
import com.upside.branch_io.client.BranchClientFactory;

public class MyConfiguration extends io.dropwizard.Configuration {

    @JsonProperty("branchIO")
    private BranchClientFactory branchClientFactory;

    // setter & getter, etc

```

Our guice module:
```
    @Provides
    @Inject
    public BranchClientFactory provideBranchClientFactory(MyConfiguration configuration) {
        return configuration.getBranchClientFactory();
    }

    @Provides
    @Inject
    public BranchClient provideBranchClient(MyConfiguration configuration) {
        return configuration.getBranchClientFactory().build();
    }
```