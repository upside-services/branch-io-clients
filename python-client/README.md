# Branch.io client

The client is invoked like:

```
from upside_core.tiers import load_configuration
from branch_io.client import BranchClient

config = load_configuration('prod', key='branch_io.json', bucket='credentials.upside-services.com')

branch_client = BranchClient(config['key'], config['secret'], config['url'])

# Get remote branch.io information about our "uberdc" referral code
branch_client.get_deep_link('uberdc')

```

## Releasing

This artifact is a weird one-off in that it's both a java-client and a python-client.  Our repo's ../infra directory sets up the java client but not (yet) the python client, so a temporary localhost release work-around is `python setup.py sdist bdist_wheel upload -r local`
