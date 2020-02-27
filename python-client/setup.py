#!/usr/bin/env python

from setuptools import setup, find_packages
import branch_io

with open("requirements.txt") as infile:
    requires = list(map(lambda x: x.strip(), infile.readlines()))

setup_options = dict(
    name='branch-client',
    version=branch_io.__version__,
    description='Python client for branch.io.',
    long_description='Python client for branch.io.',
    author='Upside Services, Inc',
    url='https://github.com/upside-services/branch-io-clients',
    scripts=[],
    packages=find_packages(exclude=['tests*']),
    install_requires=requires
)

setup(**setup_options)