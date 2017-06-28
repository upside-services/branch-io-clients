from upside_core.services import create_public_service
from promo_service.client import PromoService
from pprint import pprint
import jsondiff
import json
import sys
import time
import logging
from BranchClient import BranchClient

# from https://docs.python.org/2/howto/logging-cookbook.html#multiple-handlers-and-formatters
logger = logging.getLogger()
logger.setLevel(logging.INFO)
# create file handler which logs even debug messages
fh = logging.FileHandler('fix_branch_io.log')
fh.setLevel(logging.INFO)
# create console handler with a higher log level
ch = logging.StreamHandler()
ch.setLevel(logging.WARN)
# create formatter and add it to the handlers
formatter = logging.Formatter('%(asctime)s - %(name)s - %(levelname)s - %(message)s')
ch.setFormatter(formatter)
fh.setFormatter(formatter)
# add the handlers to logger
logger.addHandler(ch)
logger.addHandler(fh)

config = {
    'alpha': {
        'key': 'key_live_ifzjOfbrf1gG6VPN0qSOnpjkBwfGxCF6',
        'secret': 'secret_live_UacuxlWRATQkCSVey2Zcy4jvEfK9PxYr',
        'url': 'https://upsideappbeta.app.link'
    },
    'prod': {
        'key': 'key_live_lpAkPglukSH5VuTvgjti5lokDBjIm0Kn',
        'secret': 'secret_live_An8B1VirdA8nPucN7JHClDxII5hjuTZd',
        'url': 'https://upside.app.link'
    }
}


def fix_all(tier, program_id, limit):
    """
    :param tier: The tier (config key, really) to fix
    :param program_id: The program id to fix (REF_01, REF_02, etc)
    :param limit: Set to 0 for 'all', otherwise to some int value to limit the execution to just that many
    :return:
    """
    rb = create_public_service(tier, PromoService)
    all_referral_codes = rb.get_all_referral_codes(program_id)
    logger.warn('Found {} referral codes'.format(len(all_referral_codes)))

    start_at = 9580
    all_referral_codes = all_referral_codes[start_at:]

    current = 1
    for rc in all_referral_codes:
        code = rc['referralCode']['code']
        logger.warn('Fixing code {}'.format(code))
        if config[tier]['url'] in rc['referralCode']['shareLink']:
            safe_fix(code, tier, program_id)
            time.sleep(0.25)
        else:
            logger.warn('Skipping code {} because it does not have a branch.io share_link'.format(code))

        if limit > 0 and current >= limit:
            logger.warn('Terminating execution because we reached out limit of {}'.format(limit))
            sys.exit(0)
        else:
            current += 1


def safe_fix(code, tier='alpha', program_id='REF_01'):

    branch_client = BranchClient(config[tier]['key'], config[tier]['secret'], config[tier]['url'])

    original = branch_client.get_deep_link(code)
    logger.info('before PUTting a fix for code {}: {}'.format(code, original))

    if original['data'].get('upsidePromotionCode', None):
        modified = fix_referral_code(branch_client, program_id, code)

        logger.info('after fix: {}'.format(modified))

        # actual_diff should be something like {u'data': {delete: [u'upsidePromotionCode'], u'promoCode': u'AAAA782'}}
        actual_diff = jsondiff.diff(original, modified)

        logger.info('diff between original and modified: {}'.format(actual_diff))

        # I need the throw-away "x":1 here because otherwise jsondiff's output uses the
        # keyword 'replace' instead of 'delete'
        expected_diff = jsondiff.diff({'data': {'x': 1, 'upsidePromotionCode': code}},
                                      {'data': {'x': 1, 'promoCode': code}})

        logger.info('expected_diff: {}'.format(expected_diff))

        diffcheck = jsondiff.diff(actual_diff, expected_diff)
        if diffcheck:
            logger.error('When fixing code {} I encountered this difference that I was not expecting; exiting the '
                          'whole program to make sure we do not screw 100% of our codes up: {}'.format(code, diffcheck))
            sys.exit(1)
        else:
            logger.warn('      fixed {}'.format(code))

    else:
        logger.warn('Skipping code {} because it does not have an data.upsidePromotionCode value'.format(code))


def fix_referral_code(branch_client, programId, code):
    new_data = {
        'promoCode': code,
        'programId': programId
    }
    return branch_client.update_deep_link(code, **new_data)
