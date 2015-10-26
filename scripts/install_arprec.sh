#!/bin/sh

PKG="arprec-2.2.17"
FILE="$PKG.tar.gz"
URL="http://crd-legacy.lbl.gov/~dhbailey/mpdist/$FILE"
TARGET="c_modules"

CURL_COMMAND="curl $URL -o $FILE"
TAR_COMMAND="tar -xzvf $FILE"
CP_SRC_COMMAND="cp ../../src/arprec/arprec_js.cc ."
CONFIGURE_COMMAND="emconfigure ./configure --disable-assembly --build none"
MAKE_LIB_COMMAND="emconfigure make -j8"
COMPILE_COMMAND="emcc -O3 --closure 0 --memory-init-file 0 --bind -s ASSERTIONS=1 -s NO_EXIT_RUNTIME=1 -I include/ -o arprec.js arprec_js.cc src/.libs/libarprec.a"
CLOSURE_COMMAND="closure-compiler -O ADVANCED --js_output_file arprec.min.js arprec.js"
CP_JS_COMMAND="cp arprec.js arprec.min.js ../../resources/public/js/vendor/"

cd `git rev-parse --show-toplevel` &&
mkdir -p $TARGET &&
cd c_modules &&

if [ ! -f $FILE ]; then
    echo $CURL_COMMAND;
    ${CURL_COMMAND};
fi

echo $PWD
echo $TAR_COMMAND;
${TAR_COMMAND} &&

cd $PKG &&

echo $PWD

echo $CP_SRC_COMMAND;
${CP_SRC_COMMAND} &&

echo $CONFIGURE_COMMAND;
${CONFIGURE_COMMAND} &&

echo $MAKE_LIB_COMMAND;
${MAKE_LIB_COMMAND} > arprec.min.js &&

echo $COMPILE_COMMAND;
${COMPILE_COMMAND} &&

echo $CLOSURE_COMMAND;
${CLOSURE_COMMAND} &&

echo $CP_JS_COMMAND;
${CP_JS_COMMAND}
