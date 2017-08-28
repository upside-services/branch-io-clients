#!/usr/bin/env python

from setuptools import setup, find_packages

import upside_core

requires = ['boto3', 'requests']

setup_options = dict(
    name='branch-client',
    version='1.0.1',
    description='Python client for branch.io.',
    long_description='Python client for branch.io.',
    author='Upside Services, Inc',
    url='https://github.com/upside-services/branch-io-clients',
    scripts=[],
    packages=find_packages(exclude=['tests*']),
    install_requires=requires
)

setup(**setup_options)
