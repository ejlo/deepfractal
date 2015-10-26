#!/bin/sh

SYS="$(uname -a)";
cd `git rev-parse --show-toplevel`;

if [[ $SYS == *"NixOS"* ]]; then
    nix-shell --command scripts/install_arprec.sh scripts/emscripten.nix;
else
    scripts/install_arprec.sh;
fi
