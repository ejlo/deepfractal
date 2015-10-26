#!/bin/sh

# to be run after "lein bower install"

mkdir -p resources/public/js/vendor/ &&
mkdir -p resources/public/css/vendor/ &&
cp bower_components/bootstrap-css-only/css/bootstrap.min.css resources/public/css/vendor/
