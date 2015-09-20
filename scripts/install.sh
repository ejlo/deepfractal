#!/bin/sh

# to be run after "lein bower install"

mkdir -p resources/public/js/vendor/ &&
mkdir -p resources/public/css/vendor/ &&
cp bower_components/decimal.js/decimal.min.js bower_components/d3/d3.min.js resources/public/js/vendor/ &&
cp bower_components/bootstrap-css-only/css/bootstrap.min.css resources/public/css/vendor/
