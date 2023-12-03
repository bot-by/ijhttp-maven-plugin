#!/bin/bash

[ "${1:-0}" -gt 0 ] && echo "error output" >&2 || echo "standard output"

exit "${1:-0}"