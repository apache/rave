#!/bin/sh
# {{ ansible_managed }}

export M3_HOME={{ maven_install_dir }}/latest
export M3=$M3_HOME/bin
export MAVEN_OPTS="-Xms{{ maven_min_memory }} -Xmx{{ maven_max_memory }}"
export PATH=$M3:$PATH
~