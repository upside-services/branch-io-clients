import requests
import logging


class BranchClient(object):

    base_url = "https://api.branch.io/v1"

    def __init__(self,
                 branch_key,
                 branch_secret,
                 upside_url='https://upsideappbeta.app.link',  # default to alpha
                 session_factory=requests.session):
        self.branch_key = branch_key
        self.branch_secret = branch_secret
        self.upside_url = upside_url

        self.session = session_factory()

        self.session.mount('http://', requests.adapters.HTTPAdapter(max_retries=3))
        self.session.mount('https://', requests.adapters.HTTPAdapter(max_retries=3))

    def get_deep_link(self, deep_link):
        # $ curl 'https://api.branch.io/v1/url?url=https%3A%2F%2Fupside.app.link/THOMAS688&branch_key=xxx' | jq

        url = '{branchUrl}/url'.format(branchUrl=self.base_url)
        query_params = {
            'url': '{upsideUrl}/{deepLink}'.format(upsideUrl=self.upside_url, deepLink=deep_link),
            'branch_key': self.branch_key
        }

        logging.info('Sending to url {} query params: {}'.format(url, query_params))

        response = self.session.get(
            url=url,
            params=query_params
        )

        logging.info('url = {}'.format(response.url))

        return response.json()

    def update_deep_link(self, deep_link, **data_map):
        """
        :param deep_link: The 'deep_link' to update data for.  In Upside lingo, this is a 'promoCode' like 'THOMAS688'
        :param kwargs: The dataMap we want to update the deep_link with.  Note that the default branch.io data that
        exists on the deep link will remain intact after an update, but any and all non-branch.io data will be
        replaced with the contents of the
        :return: The response from the server
        """
        # e.g. https://api.branch.io/v1/url?url=https%3A%2F%2Fupside.app.link/THOMAS688
        branch_url = '{branchUrl}/url?url={upsideUrl}/{deepLink}'.format(branchUrl=self.base_url,
                                                                         upsideUrl=self.upside_url,
                                                                         deepLink=deep_link)

        payload = {
            'branch_key': self.branch_key,
            'branch_secret': self.branch_secret,
            'data': data_map
        }

        logging.info('sending payload {}'.format(payload))

        response = self.session.put(
            url=branch_url,
            json=payload
        )

        logging.info('url = {}'.format(response.url))

        return response.json()

