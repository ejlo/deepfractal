with import <nixpkgs> {}; {
  sdlEnv = stdenv.mkDerivation {
    name = "emscripten";

    buildInputs = with nodePackages; [
    m4
    nodejs
    emscripten
    closurecompiler
    uglify-js
  ];

  enableParallelBuilding = true;
   };
 }
